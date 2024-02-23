# Address API Spec

## Create Address
Endpoint : POST : ```/api/contacts/{idContact}/adresses```

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :

```json
{
  "street": "Jl. jalan",
  "city": "Kota",
  "province": "provinsi",
  "country": "negara",
  "postalCode": "11122"
}
```

Response Body(Success) :

```json
{
  "data": {
    "id": "rand string",
    "street": "Jl. jalan",
    "city": "Kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "11122"
  }
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "contact is not found"
}
```

## Update Address
Endpoint : PUT ```/api/contacts/{idContact}/addresses/{idAddress}```

Request Header :
- X-API-TOKEN : Token(mandatory)

Request Body :

```json
{
  "street": "Jl. jalan",
  "city": "Kota",
  "province": "provinsi",
  "country": "negara",
  "postalCode": "11122"
}
```

Response Body(Success) :

```json
{
  "data": {
    "id": "rand string",
    "street": "Jl. jalan",
    "city": "Kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "11122"
  }
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "address is not found"
}
```

## Get Address
Endpoint : GET ```/api/contacts/{idContact}/adresses/{idAddress}```

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :

```json
{
  "data": {
    "id": "rand string",
    "street": "Jl. jalan",
    "city": "Kota",
    "province": "provinsi",
    "country": "negara",
    "postalCode": "11122"
  }
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "address is not found"
}
```

## Remove Address
Endpoint : DELETE ```/api/contacts/{idContact}/addresses/{idAddress}```

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
  "errors": "address is not found"
}
```

## List Address
Endpoint : GET ```/api/contacts/{idContact}/addresses```

Request Header :
- X-API-TOKEN : Token(mandatory)

Response Body(Success) :

```json
{
  "data": [
    {
      "id": "rand string",
      "street": "Jl. jalan",
      "city": "Kota",
      "province": "provinsi",
      "country": "negara",
      "postalCode": "11122"
    }
  ]
}
```

Response Body(Failed, 404) :

```json
{
  "errors": "contact is not found"
}
```