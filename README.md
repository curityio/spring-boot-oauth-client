# OAuth 2.0 Client with Spring Boot

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This repository contains an example implementation that demonstrates how to use Spring Boot and Spring Security to create an OAuth 2.0 Client that authenticates users through the Curity Identity Server.

This example demonstrates two different ways for client authentication:

1. Basic authentication with a client-id and a shared client secret
2. JWT client assertion authentication with a client-id and keystore with a key pair

The second option is used to illustrate how to follow the recommendation of [OAuth2.1 on client authentication](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07#section-2.4), which is an updated and consolidated version of OAuth2.0.

## Configure the Client

### Using Basic Authentication

* Create a client `demo-basic-client` with the code flow capability. 
* Register the following redirect URI for your client: `http://localhost:8080/login/oauth2/code/demo-basic-client`. In this case we assume that the application is hosted on `localhost`, adapt accordingly. 
 
The redirect URI is the path of the application where the Curity Identity Server will redirect to after the user was authenticated. This is an endpoint that Spring Boot sets up.

* Choose the authentication method `secret` and enter a secret. 
* Under **Authorization** add the scopes `openid` and `profile`.

### Using Private Key JWT
Assume, there is a key pair that the client uses to create a self-signed JWT. The public key of the key pair needs to be uploaded to the Curity Identity Server.
* In the **Facilities** menu, under **Key and Cryptography** select **Signing**.
* Create a new **Signature Verification Key**. 
* Enter a name.
* Select `asymmetric` as type and upload the existing (public) key.

Now, navigate to the token service profile to set up the client: 

* Create a client `demo-private-jwt-client` with the code flow capability. 
* Register the following redirect URI for your client: `http://localhost:8080/login/oauth2/code/demo-private-jwt-client`. In this case we assume that the application is hosted on `localhost`, adapt accordingly. 
 
The redirect URI is the path of the application where the Curity Identity Server will redirect to after the user was authenticated. This is an endpoint that Spring Boot sets up.

* Choose the authentication method `asymmetric-key` and select the signature verification key created above. This is the public key that corresponds to the private key that the client uses to sign the JWT to authenticate itself.
* Under **Authorization** add the scopes `openid` and `profile`.

## Configure application.yml
Make sure this matches the configuration on the server side. Update the client registration and provider to fit your settings. In particular, check the `client-id` and `client-secret` when using basic authentication:

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
```

For the client that uses a JWT client assertion for authentication, check the `client-id` and `client.keystore` parameters. 

Point to a keystore that contains at least one private key and a corresponding certificate with the public key. 
Provide the password of the keystore and the alias of the key pair. 

The certificate (that is the public key) must be registered with the Curity Identity Server as a **Signature Verification Key**. See [Configure the Client](#configure-the-client).

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          demo-private-jwt-client:
            client-name: Login with the Curity Identity Server (Private JWT Client)
            client-id: demo-private-jwt-client
            client-authentication-method: private_key_jwt
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile
            provider: idsvr

client:
  authentication:
    asymmetric-key:
      key-store-file-name: client_keys.jks
      key-store-password: changeit
      key-store-alias: demo-client
      key-store-type: jks
```

Don't forget to point to the Curity Identity Server as well. Specify the issuer URI (by default, this is the anonymous endpoint of the Curity Identity Server). Spring Boot uses this URI to load the OpenID Connect metadata from the server.

```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          idsvr:
            issuer-uri: https://idsvr.example.com/oauth/v2/oauth-anonymous
```

See [application.yml](src/main/resources/application.yml) for the complete configuration.

## Run the application
To start the application run 

```bash
./gradlew bootRun
```

Open `http://localhost:8080` in your browser. Click on the link to log in. 
Open one of the different options to fetch an access and ID token from the Curity Identity Server. 
After successful login the page displays the username and the name of the client used to integrate with the Curity Identity Server.

### Configuring the Trust Store

The application must trust the HTTPS server certificate of the Curity Identity Server. Place the issuing CA certificate of the server certificate and all intermediate CA certificate as well as the root CA certificate in a trust store. 

Start the application with the truststore using JVM arguments:

```bash
./gradlew bootRun -Djavax.net.ssl.trustStore=/path/to/localhost.truststore -Djavax.net.ssl.trustStorePassword=changeit
```

## Generate Key and Trust Stores

### Key Store for API Client Authentication

When authenticating with `private_key_jwt`, you need to specify keys to issue the self-signed JWT for the authentication. Use `keytool` to generate a key pair:

```bash
keytool -genkey -alias demo-client -keyalg RSA -keystore client_keys.jks -keysize 2048 -dname "CN=Demo Client,O=Example"
```

The subject name of the certificate does not matter in this context. Make sure to remember the alias as it is used to identify the key pair. Put the key store file in the `resources` folder and update the parameters in `application.yml` accordingly.

Export the certificate with the public key:

```bash
keytool -exportcert -keystore client_keys.jks -alias demo-client -file demo-client.cer
```

Upload the certificate file to the Curity Identity Server as a **Signature Verification Key** and configure the client with that key.

### Trust Store for Server Certificates

The `OAuthFilter` assumes that the authorization server's endpoints are served over HTTPS. 
The API must trust the server certificate from the JWKS and OpenID metadata endpoints. 
In test systems the server certificate may not be trusted by default. 
To set up the trust, add the trust chain of the server certificate to a trust store. 
Get hold of the certificate of the CA that issued the server certificate and all intermediate CA certificates up to the root CA ("trust chain"). 
Then use `keytool` to import the certificate(s) in a (new) truststore:

```bash
keytool -importcert -keystore localhost.truststore -file issuing-ca-cert.pem
```

If prompted, trust the certificates. 
If the server certificate is self-signed import only the certificate itself.

## More Information
More information about OAuth 2.0, OpenID Connect and the Curity Identity Server can be found here:

* [The Curity Identity Server](https://curity.io)
* [OAuth 2.0](https://curity.io/resources/oauth/)
* [OpenID Connect](https://curity.io/resources/openid-connect/)

Check out the related tutorial of this repository:
* [OIDC Client with Spring Security](https://curity.io/resources/tutorials/howtos/writing-clients/oidc-spring-boot/)

## Licensing

This software is copyright (C) 2020 Curity AB. It is open source software that is licensed under the [Apache 2 license](LICENSE).
