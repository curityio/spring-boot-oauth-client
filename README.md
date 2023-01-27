# OAuth 2.0 Client with Spring Boot

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This repository contains an example implementation that demonstrate how to use Spring Boot and Spring Security to create an OAuth 2.0 Client that authenticates users through the Curity Identity Server.

This example demonstrates two different ways for client authentication:

1. Basic authentication with a client-id and a shared client secret
2. JWT client assertion authentication with a client-id, private and public key

## Configure the Client

### Using Basic Authentication
Create a client `demo-basic-client` with the code flow capability. Register the following redirect URI for your client: `http://localhost:9090/login/oauth2/code/demo-basic-client`. In this case we assume that the application is hosted on `localhost`, adapt accordingly. The redirect URI is the path of the application where the Curity Identity Server will redirect to after the user was authenticated. This is an endpoint that Spring Boot sets up.

Choose the authentication method `secret` and enter a secret. Under **Authorization** add the scopes `openid` and `profile`.

### Using Private Key JWT
Assume, there is a key pair that the client uses to create a self-signed JWT. The public key of the key pair needs to be uploaded to the Curity Identity Server. In the **Facilities** menu, under **Key and Cryptography**, select **Signing** and create a new **Signature Verification Key**. Enter a name, select `asymmetric` as type and upload an existing (public) key.

Create a client `demo-private-jwt-client` with the code flow capability. 
Register the following redirect URI for your client: `http://localhost:9090/login/oauth2/code/demo-private-jwt-client`. In this case we assume that the application is hosted on `localhost`, adapt accordingly. The redirect URI is the path of the application where the Curity Identity Server will redirect to after the user was authenticated. This is an endpoint that Spring Boot sets up.

Choose the authentication method `asymmetric-key` and select the signature verification key created above. This is the public key that corresponds to the private key that the client uses to sign the JWT to authenticate itself.

Under **Authorization** add the scopes `openid` and `profile`.

## Configure application.yml
Update the client registration and provider to fit your settings.

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          demo-basic-client:
            client-name: Login with the Curity Identity Server (Basic Client)
            client-id: demo-basic-client
            client-secret: Secr3t
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile
            provider: idsvr
          demo-private-jwt-client:
            client-name: Login with the Curity Identity Server (Private JWT Client)
            client-id: demo-private-jwt-client
            client-authentication-method: private_key_jwt
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile
            provider: idsvr
        provider:
          idsvr:
            issuer-uri: https://idsvr.example.com/oauth/anonymous
```

## Run the application
To start the application run 

```bash
./gradlew bootRun
```

Open `http://localhost:9090` in your browser. Click on the link to log in. Open one of the different options to fetch an access and ID token from the Curity Identity Server. After successful login the page displays the username and the name of the client used to integrate with the Cutiry Identity Server.

### Configuring the Truststore

The application must trust the HTTPS server certificate of the Curity Identity Server (from the `provider.issuer-uri`). Place the issuing CA certificate of the server certificate in a truststore. Use `keytool` for that purpose:

```bash
keytool -importcert -keystore localhost.truststore -file issuing-ca-cert.pem
```

Start the application with the truststore using JVM arguments:

```bash
./gradlew bootRun -Djavax.net.ssl.trustStore=/path/to/localhost.truststore -Djavax.net.ssl.trustStorePassword=changeit
```

## More Information
More information about OAuth 2.0, OpenID Connect and the Curity Identity Server can be found here:

* [The Curity Identity Server](https://curity.io)
* [OAuth 2.0](https://curity.io/resources/oauth/)
* [OpenID Connect](https://curity.io/resources/openid-connect/)

Check out the related tutorial of this repository:
* [OIDC Client with Spring Security](https://curity.io/resources/tutorials/howtos/writing-clients/oidc-spring-boot/)

## Licensing

This software is copyright (C) 2020 Curity AB. It is open source software that is licensed under the [Apache 2 license](LICENSE).
