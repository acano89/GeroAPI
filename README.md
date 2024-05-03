# REST API per el projecte CentreEnXarxa


## Default Data:

* #### La classe GeroApiApplication implementa la interface CommandLineRunner, amb la qual cada cop que arranca es crean els rols necessaris si cal (ROLE_AMDIN, ROLE_USER, ROLE_CARE, ROLE_INACTIVE) a mes d'un usuari per defecte "root" amb le rol ROLE_ADMIN, que permet canviar els rols dels usuaris registrats.

* #### Qualsevol nou usuari te el rol ROLE_USER per defecte


## Endpoints:

### Auth:

#### Register a new user:

* /api/v1/auth/signup

        Create new user with the follow items:

        {
            "username": "username"
            "email": "email"
            "password": "password"
        }

#### User login:

* api/v1/auth/signin

        Login a user with the follow items:
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

### User:

#### Admin_user add new role

* api/v1/user/add-role/{username}/{newRole}

        Requires ADMIN token.
        Put new rol in an user needs username and role to create.

#### Admin_user remove a role

* api/v1/user/remove-role/{username}/{removeRole}

        Requires ADMIN token.
        Delete a role from an user needs username and role to create.

#### User changes the name

* api/v1/user/change-name

        {
        "currentName": "currentName"
        "newName": "newName"
        }

        Change name to another, needs current and new name.

#### User changes the password

* api/v1/user/change-password/{username}
    
        {
        "currentPassword": "currentPassword"
        "newPassword": "newPassword"
        }

        Change encoded password to another encoded password,
        needs current and new password, and the username.

#### User changes the email

* api/v1/user/change-email

        {
        "username": "currentPassword"
        "newEmail": "newEmail"
        }

        Change the email from the user, needs username
        and new email.

#### Admin_user get a users list

* api/v1/user/users-list

        Requires ADMIN token.
        Get users list.

### Service:

#### Admin/Care_user sets a Service

* api/v1/service/set-Service

      {
      "name": "name",
      "breakfast": "breakfast",
      "lunch": "lunch",
      "snack": "snack",
      "diaperNum": diaperNum,
      "shower": "shower",
      "urination": urination,
      "deposition": deposition
      }
* 
       Put new data in an service.

### User get a service

* api/v1/service/get-Service

        {
        "date": date,
        "name": "name",
        }

        Uses gets info of the service with the name and date.

### Familiar:

#### Admin_user set a Familiar

        


    
