# Contact API Spec

## Create Contact
Endpoint : POST : ```/api/contacts```

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :

```json
{
  "firstName": "bay",
  "lastName": "yazid",
  "email": "bay@mail.com",
  "phone": "0854332421"
}
```

Response Body(Success) : 

```json
{
  "data": {
    "id": "rand string",
    "firstName": "bay",
    "lastName": "yazid",
    "email": "bay@mail.com",
    "phone": "0854332421"
  }
}
```

Response Body(Failed) :

```json
{
  "errors": "Email format invalid, phone format invalid, ..."
}
```

## Update Contact
Endpoint : PUT ```/api/contacts/{idContact}```

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :

```json
{
  "firstName": "bay",
  "lastName": "yazid",
  "email": "bay@mail.com",
  "phone": "0854332421"
}
```

Response Body(Success) :

```json
{
  "data": {
    "id": "rand string",
    "firstName": "bay",
    "lastName": "yazid",
    "email": "bay@mail.com",
    "phone": "0854332421"
  }
}
```

Response Body(Failed) :

```json
{
  "errors": "Email format invalid, phone format invalid, ..."
}
```

## Get Contact
Endpoint : GET ```/api/contacts/{idContact}```

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :

```json
{
  "data": {
    "id": "rand string",
    "firstName": "bay",
    "lastName": "yazid",
    "email": "bay@mail.com",
    "phone": "0854332421"
  }
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "Contact is not found"
}
```

## Search Contact
Endpoint : GET ```/api/contacts```

Request Header :
- X-API-TOKEN : Token(mandatory)

Query Param :
- name: String, contact firstName or , using like query(optional)
- phone: String, contact phone, using like query(optional)
- email: String, contact email, using like query(optional)
- page: Integer, start from 0, default 0
- size: Integer, default 10

Response Body(Success) :
```json
{
  "data": [
    {
      "id": "rand string",
      "firstName": "bay",
      "lastName": "yazid",
      "email": "bay@mail.com",
      "phone": "0854332421"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body(Failed, 401) :

```json
{
  "errors": "Unauthorized"
}
```


## Remove Contact
Endpoint : DELETE ```/api/contacts/{idContact}```

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :
```json
{
  "data": "Ok"
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "Contact is not found"
}
```