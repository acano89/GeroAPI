# REST API per el projecte CentreEnXarxa


## Default Data:

* #### La classe GeroApiApplication implementa la interface CommandLineRunner, amb la qual cada cop que arranca es crean els rols necessaris si cal (ROLE_AMDIN, ROLE_USER, ROLE_CARE, ROLE_INACTIVE) a mes d'un usuari per defecte "root" amb le rol ROLE_ADMIN, que permet canviar els rols dels usuaris registrats.

* #### Qualsevol nou usuari te el rol ROLE_USER per defecte


## Endpoints:

### Auth:

#### Register a new user:

* /api/v1/auth/signup

        {
            "username": "username"
            "email": "email"
            "password": "password"
        }

#### User login:

* api/v1/auth/signin

        {
            "username": "username"
            "password": "password"
        }

#### User logout:

* /api/v1/auth/signout

      Logout current user

#### Refresh an expired JWT token:

* /api/v1/auth/refreshtoken

        Uses stored refresh token to generate a new 
        JWT token when the current one is expired.

### User;

#### Admin user add new role

* api/v1/user/add-role/{username}/{newRole}

        Requires ADMIN token.
        Put new rol in an user.

#### Admin user get DDBB's users list

* api/v1/user/users-list

        Requires ADMIN token.
        Get users list.

#### Admin/Care user sets a Service

* api/v1/user/set-Service

        Requires ADMIN token.
        Put new data in an service.

### User change the password

* api/v1/user/change-password({username)

        {
        "currentPassword": "currentPassword"
        "newPassword": "newPassword"
        }
        Uses change stored encoded password with
        a new encoded password 

        


    
