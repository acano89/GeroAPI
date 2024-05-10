package al3solutions.geroapi.controller;

import al3solutions.geroapi.exception.TokenRefreshException;
import al3solutions.geroapi.model.ERole;
import al3solutions.geroapi.model.RefreshToken;
import al3solutions.geroapi.model.Role;
import al3solutions.geroapi.model.User;
import al3solutions.geroapi.payload.request.LoginRequest;
import al3solutions.geroapi.payload.request.SignupRequest;
import al3solutions.geroapi.payload.response.MessageResponse;
import al3solutions.geroapi.payload.response.UserInfoResponse;
import al3solutions.geroapi.repository.RoleRepository;
import al3solutions.geroapi.repository.UserRepository;
import al3solutions.geroapi.security.jwt.JwtUtils;
import al3solutions.geroapi.security.service.RefreshTokenService;
import al3solutions.geroapi.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    //Register Endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        String username = signupRequest.getUsername();

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username " + username + " is already taken!"));
        }

        String email = signupRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email " + email + " is already taken!"));
        }

        Role role = roleRepository.findByName(ERole.ROLE_USER).get();
        Set<Role> roles = Set.of(role);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().body(user);
    }

    // Login endpoint
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        Optional<RefreshToken> existing = refreshTokenService.findByUserId(userDetails.getId());

        RefreshToken refreshToken;

        // Recover db stored refresh token if it's not expired
        if(existing.isPresent() && existing.get().getExpiryDate().isAfter(Instant.now())){
            refreshToken = existing.get();

        } else {
            // Otherwise create a new one
            refreshTokenService.deleteByUserId(userDetails.getId());
            refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        }

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(UserInfoResponse
                        .builder()
                        .id(userDetails.getId())
                        .username(userDetails.getUsername())
                        .email(userDetails.getEmail())
                        .roles(roles)
                        .build());
    }

    // Delete refresh token on manual logout
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       String userName = null;
        if (!Objects.equals(principal.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principal).getId();
             userName = ((UserDetailsImpl) principal).getUsername();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse("Adeu "+ userName +", fins aviat!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse("Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }

    //Consulta de dades propi usuari
    @PostMapping("/info")
    public ResponseEntity<?> consultInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long id = null;
        String userName = null;
        String email = null;
        List<String> roles = null;

        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails){

                UserDetailsImpl userDetails = (UserDetailsImpl) principal;

                id = userDetails.getId();
                userName = userDetails.getUsername();
                email = ((UserDetailsImpl) principal).getEmail();
                roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
            }
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(UserInfoResponse
                        .builder()
                        .id(id)
                        .username(userName)
                        .email(email)
                        .roles(roles)
                        .build());
    }
}


