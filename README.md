# REST API per el projecte CentreEnXarxa


## Default Data:

* #### La classe GeroApiApplication implementa la interface CommandLineRunner, amb la qual cada cop que arranca es crean els rols necessaris si cal (ROLE_AMDIN, ROLE_USER, ROLE_CARE, ROLE_INACTIVE) a mes d'un usuari per defecte "root" amb le rol ROLE_ADMIN, que permet canviar els rols dels usuaris registrats.

* #### Qualsevol nou usuari te el rol ROLE_USER per defecte


## Endpoints:

### Auth:

#### Register a new user:

* /api/v1/auth/signup

        Create new user with the follow body:

        {
            "username": "username"
            "email": "email"
            "password": "password"
        }

#### User login:

* api/v1/auth/signin

        Login a user with the follow body:
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

#### Consult info from authenticated user:

* /api/v1/auth/info

        User get info stored to consult.

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

        Change user's name to another with the follow body.

        {
        "currentName": "currentName"
        "newName": "newName"
        }

        

#### User changes the password

* api/v1/user/change-password/{username}
    
        Change encoded password to another encoded password,
        with the follow body, needs the username.

        {
        "currentPassword": "currentPassword"
        "newPassword": "newPassword"
        }

        

#### User changes the email

* api/v1/user/change-email
        
      Change the email from the user with the follow body.

      {
      "username": "currentPassword"
      "newEmail": "newEmail"
      }


#### Admin_user get a users list

* api/v1/user/users-list

        Requires ADMIN token.
        Get users list.

#### Admin_user delete a user

* api/v1/user/delete/{username}

        Requipres ADMIN token.
        Admin user delete stored in ddbb.

#### Admin_user update selected user info.

* api/v1/user/updateUser/{username}

        Requires ADMIN token.
        Admin update name and mail info stored.
        Uses the follow body:

        {
          "newName": "newName",
          "newEmail": "newEmail"
        }

#### Admin_user consult selected user info.

* api/v1/user/infoUser/{username}

        Requires ADMIN token.
        Admin user consults info stored from
        selected user.

### Service:

#### Admin/Care_user sets a Service

* api/v1/service/set-Service

      Create a service with the follow body.

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

       

### User get a service

* api/v1/service/get-Service

        User gets info of the service with the follow body.

        {
        "date": date,
        "name": "name",
        }


### Familiar:

#### Admin_user set a Familiar

* api/v1/familiar/set-Familiar

        Admin_user create a new Familiar with the follow body

        {
        "name": "name",
        "familiarsUserName": "familiarsUserName",
        "familiarMail": "familiarMail",
        "state": "state",
        "reason": "reason",
        "place": "place",
        "state": "dayTrip",
        "reason": "shower",
        "place": "pickup"
        }


#### User get a Familiar

* api/v1/familiar/get-Familiar

         User get Familiar's info with the follow body

        {
        "name": "name",
        "familiarsUserName": "familiarsUserName",
        }


    
