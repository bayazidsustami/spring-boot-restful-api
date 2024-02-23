# User API Spec

## Register User
Endpoint : POST ``/api/users``

Request Body :

```json
{
  "username": "baybay",
  "password": "rahasia",
  "name": "bayazid"
}
```

Response Body(Success) :

```json
{
  "data": "Ok"
}
```

Response Body(Failed) :

```json
{
  "errors": "username must not blank"
}
```

## Login User
Endpoint : POST ``/api/auth/login``

Request Body :

```json
{
  "username": "baybay",
  "password": "rahasia"
}
```

Response Body(Success) :

```json
{
  "data": {
    "token": "token",
    "expireAt": "12345677"
  }
}
```

Response Body(Failed, 401) :

```json
{
  "errors": "username or password wrong"
}
```
## Get User
Endpoint : GET ``/api/users/current``

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :

```json
{
  "data": {
    "username": "baybay",
    "name": "bayazid"
  }
}
```

Response Body(Failed, 401) :

```json
{
  "errors": "Unauthorized"
}
```

## Update User
Endpoint : PATCH ``/api/users/current``

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :

```json
{
  "name": "bayazid",
  "password": "rahasia baru"
}
```

Response Body(Success) :

```json
{
  "data": {
    "username": "baybay",
    "name": "bayazid"
  }
}
```

Response Body(Failed, 401) :

```json
{
  "errors": "Unauthorized"
}
```

## Logout User

Endpoint : DELETE ``/api/auth/logout``

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :

```json
{
  "data": "Ok"
}
```