<img src="docs/logo.png" width="200" align="right">

# AuthorizationServer Wallet Module :key:

This module is simple REST API application, that is used by Raspberry Wallet as
Module, which is trusted third party side.

## Requirements
- Maven 3+
- Java 8

## Installation
```bash
# get repo
git clone https://github.com/RaspberryWallet/Backend.git
cd Backend

# build and install jars, then build docker image with Fabric8 plugin
mvn clean install docker:build

# after we made docker images, we can just turn on the system
docker-compose up -d
```

## Table of contents
- [Requirements](#requirements)
- [Installation](#installation)
- [Table of contents](#table-of-contents)
- [Details](#details)
- [Endpoints](#endpoints)
	- [Register](#register)
	- [Login](#login)
	- [Logout](#logout)
	- [Set secret](#set-secret)
	- [Get secret](#get-secret)
	- [Overwrites existing secret](#overwrites-existing-secret)
- [Old sequence diagrams](#old-sequence-diagrams)
	- [Initialization of module sequence diagram:](#initialization-of-module-sequence-diagram)
	- [Unlock module sequence diagram:](#unlock-module-sequence-diagram)
- [Authors](#authors)
- [Changelog](#changelog)

## Details

This module uses Redis as data storage and Jedis library for connection.
REST endpoints are provided by Spring Boot.  

After a new version release, you have to change jar file name manually, since
it's hardcoded into Dockerfile and there is no Dockerfile generator.

## Endpoints

Endpoints documentation is not actual and will be not updated, since the goal
is to introduce [Spring REST Docs](https://spring.io/projects/spring-restdocs).

Login is done by custom token system implementation.

##### Register
```
POST /authorization/register
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"password":"1234"
}
```
##### Login
```
POST /authorization/login
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"password":"1234"
}

# returns token which is UUID converted to String
```
##### Logout
```
POST /authorization/logout
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"password":"1234"
}
```
##### Set secret
```
POST /authorization/secret/set
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"token":"01badf5d-fb41-4d2a-a029-0bce54bea501",
	"secret":"data"
}
```
##### Get secret
```
POST /authorization/secret/get
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"token":"01badf5d-fb41-4d2a-a029-0bce54bea501"

}

# returns secret as String
```
##### Overwrites existing secret
```
POST /authorization/secret/overwrite
Content-Type: application/json; charset=UTF-8
{
	"walletUUID":"abcd",
	"token":"01badf5d-fb41-4d2a-a029-0bce54bea501",
	"secret":"duplicate"
}
```

## Old sequence diagrams

They are not actual and we left them here, because they will be refreshed soon.

#### Initialization of module sequence diagram:

![](https://www.websequencediagrams.com/cgi-bin/cdraw?lz=dGl0bGUgQXV0aG9yaXphdGlvbiBTZXJ2ZXIgTW9kdWxlOiBpbml0aWFsABcHCgpNYW5hZ2VyLT5SUEkAJQYAIghTZW5kIHNpbmdsZSBwYXJ0IG9mIGEgd2FsbGV0IHNlY3JldCBrZXkKACoPADcTRW5jcnlwACkMIHdpdGggcmFuZG9tIHBhc3N3b3JkADgSAIE9DQCBQwY6IFJlZ2lzdGVyIGEgbmV3AIEKBwAVJ0F1dGhlbnRpY2F0ZSBvbiBzAIIXBmFuZCBzZW5kIGUAgSQGZWQAgVUMAHMTLT5SZWRpczogU3RvcmUAgg4HJ3MAKxUAgjgF&s=default)

<details><summary>Sequence diagram code</summary>
<p>

```html
title Authorization Server Module: initialization

Manager->RPIServerModule: Send single part of a wallet secret key
RPIServerModule->RPIServerModule: Encrypt secret key with random password
RPIServerModule->AuthorizationServer: Register a new wallet
RPIServerModule->AuthorizationServer: Authenticate on server and send encrypted secret key
AuthorizationServer->Redis: Store wallet's encrypted secret key part
```

</p>
</details>

#### Unlock module sequence diagram:

![](https://www.websequencediagrams.com/cgi-bin/cdraw?lz=TWFuYWdlci0-UlBJU2VydmVyTW9kdWxlOiBVbmxvY2sgbQAKBQoAEA8tPkF1dGhvcml6YXRpb24ANAY6IExvZ2luIHdpdGggc2VjcmV0IHBhc3N3b3JkCgAdEwBiE1ZhbGlkYXRlAC8JIGFuZCByZXR1cm4gc2Vzc2lvbiB0b2tlbgBrJ0dldCBlbmNyeXB0ZWQAgQ0Ia2V5AHkXZWRpcwAvBndhbGxldCdzABIsAIIzEFIAgTIGAC0eAIJJEQCCeRFEZQCBOwUAgTMLAIJdBnJhbmRvbQCCWQoAgxMRAINWBwCBAAlkAEMGZWQgcGFydCBvZgCCBQw&s=default)

<details><summary>Sequence diagram code</summary>
<p>

```html
title Authorization Server Module: unlocking module

Manager->RPIServerModule: Unlock module
RPIServerModule->AuthorizationServer: Login with secret password
AuthorizationServer->RPIServerModule: Validate password and return session token
RPIServerModule->AuthorizationServer: Get encrypted secret key
AuthorizationServer->Redis: Get wallet's encrypted secret key
AuthorizationServer->RPIServerModule: Return wallet's encrypted secret key
RPIServerModule->RPIServerModule: Decrypt secret key with random password
RPIServerModule->Manager: Return decrypted part of secret key


```

</p>
</details>

## Authors

[//]: https://tablesgenerator.com/markdown_tables

| Name                                                 | email                     |
|------------------------------------------------------|---------------------------|
| [Patryk Milewski](https://github.com/PatrykMilewski) | patryk.milewski@gmail.com |

## Changelog

[//]: https://tablesgenerator.com/markdown_tables

| Version | Is backward-compatible  | Changes            | Commit ID                                |
|---------|-------------------------|--------------------|------------------------------------------|
| 0.3     | Yes                     | Working version    | 3b73c7c2cd815a07972e5aee06e7a3d9f45d9dc7 |
