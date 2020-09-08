# OAuth 2.0 Client with Spring Boot

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This repository contains an example implementation that demonstrate how to use Spring Boot and Spring Security to create an OAuth 2.0 Client that authenticates users through the Curity Identity Server.

There are only two things to consider when configuring the client in the Curity Identity Server:

* choose the authentication method `secret` and enter a secret. 
* register the following redirect uri for your client: `http://localhost:8080/login/oauth2/code/idsvr`. 

The redirect uri is the path of the application where the Curity Identity Server will redirect to after the user was authenticated. In this case we assume that this example will be hosted on `localhost`. 

## Configure application.yml
Update the client registration and provider to fit your settings.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          idsvr:
            client-name: Login with the Curity Identity Server
            client-id: demo-client
            client-secret: Secr3t
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile
        provider:
          idsvr:
            issuer-uri: https://idsvr.example.com/oauth/anonymous
```

## Run the application
To start the application run 

```bash
./gradlew bootRun
```

Open `http://localhost:8080` in your browser. Click on the link to login and fetch an access and ID token from the Curity Identity Server.

## More Information
More information about OAuth 2.0, OpenID Connect and the Curity Identity Server can be found here:

* [The Curity Identity Server](https://curity.io)
* [OAuth 2.0](https://curity.io/resources/oauth/)
* [OpenID Connect](https://curity.io/resources/openid-connect/)

Check out the related tutorial of this repository:
* [OIDC Client with Spring Security](https://curity.io/resources/tutorials/howtos/writing-clients/oidc-spring-boot/)

## Licensing

This software is copyright (C) 2020 Curity AB. It is open source software that is licensed under the [Apache 2 license](LICENSE).
