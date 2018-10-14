### This module is going to authorize wallet decryption as trusted third party service.

#### Compiling, packaging and building Docker images.

```
# build jars and then build read-to-use Docker images
mvn clean package docker:build

# after we made docker images, we can just turn on the system
docker-compose up -d
```
### After a new version release, you have to change artifact name in Dockerfile, since Dockerfile generator is not implemented yet!

#### Endpoints

User has to be logged in, to use this service. The authorization is configured as default Spring Boot Security and will be changed in the future to OAuth 2.0.


```
# POST, key = secret, type = String in UTF-8
# 409 Conflict if secret is already set
/authorization/secret/set

# returns String with secret
# 404 Not found if wallet has no secret stored
/authorization/secret/get

# works as 'set' but overwrittes secret if there is already one assigned to wallet
/authorization/secret/overwritte

# registers a new wallet
/register

/login
``` 

### Initialization of module sequence diagram:

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

### Unclock module sequence diagram:

![](https://www.websequencediagrams.com/cgi-bin/cdraw?lz=dGl0bGUgQXV0aG9yaXphdGlvbiBTZXJ2ZXIgTW9kdWxlOiB1bmxvY2tpbmcgbQANBQoKTWFuYWdlci0-UlBJACcGACQIVQAnBQAhCAAQDy0-AFsNAGEGOiBMb2dpbiB3aXRoIHNlY3JldCBwYXNzd29yZAoAHRMALhdWYWxpZGF0ZQAzCQBLBlNwcmluZyBTZWN1cml0eQCBOggAQxVSZWRpczogR2V0IHdhbGxldCdzIGVuY3J5cHRlZACBEwhrZXkAJxcAgXsQUmV0dXJuACwfAIIREQCCQRFEZQB9BQB1CwCCJQZyYW5kb20AgiEKAIJbEQCDHgcAgQAJZABDBmVkIHBhcnQgb2YAgUcMCg&s=default)

<details><summary>Sequence diagram code</summary>
<p>

```html
title Authorization Server Module: unlocking module

Manager->RPIServerModule: Unlock module
RPIServerModule->AuthorizationServer: Login with secret password
AuthorizationServer->AuthorizationServer: Validate password with Spring Security module
AuthorizationServer->Redis: Get wallet's encrypted secret key
AuthorizationServer->RPIServerModule: Return wallet's encrypted secret key
RPIServerModule->RPIServerModule: Decrypt secret key with random password
RPIServerModule->Manager: Return decrypted part of secret key


```

</p>
</details>
