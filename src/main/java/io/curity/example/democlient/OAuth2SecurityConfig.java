package io.curity.example.democlient;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import io.curity.example.democlient.config.KeyStoreConfig;
import io.curity.example.democlient.keystore.KeyStoreLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.endpoint.NimbusJwtClientAuthenticationParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableConfigurationProperties(KeyStoreConfig.class)
public class OAuth2SecurityConfig {

    private KeyPair clientKeys;

    public OAuth2SecurityConfig(KeyStoreConfig config) throws UnrecoverableKeyException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException {
        clientKeys = KeyStoreLoader.getKeyPair(config.getKeyStoreType(), config.getKeyStoreFileName(), config.getKeyStorePassword(), config.getKeyStoreAlias());
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/","/error").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2Login(withDefaults());
        return http.build();
    }

    @Bean
    public WebClientReactiveAuthorizationCodeTokenResponseClient webClientReactiveAuthorizationCodeTokenResponseClient() {
        var tokenResponseClient = new WebClientReactiveAuthorizationCodeTokenResponseClient();
        // This is the default implementation.
        // It adds the required parameters for JWT based client authentication (`client_assertion_type`, `client_assertion`) to the authorization request.
        // For that, it creates the JWT from the keys provided by this application
        var converter = new NimbusJwtClientAuthenticationParametersConverter<OAuth2AuthorizationCodeGrantRequest>(jwkResolver);
        converter.setJwtClientAssertionCustomizer((context) -> {
            // You can customize the JWT here. For example, you can change a claim.
            // Change the audience of the JWT.
            // The Curity Identity Server accepts JWTs that have an `aud` claim with either the token endpoint or the token issuer name
            //context.getClaims().claim("aud","https://localhost:8443/oauth/v2/oauth-token"); //token endpoint
            context.getClaims().claim("aud","https://localhost:8443/oauth/v2/oauth-anonymous"); // token issuer name

        });
        tokenResponseClient.addParametersConverter(converter);
        return tokenResponseClient;
    }

    // Provides the JSON web key that is used to sign the client JWT assertion
    Function<ClientRegistration, JWK> jwkResolver = (clientRegistration) -> {
        if (clientRegistration.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.PRIVATE_KEY_JWT)) {
            // This client uses RSA keys and signatures
            PublicKey publicKey = clientKeys.getPublic();
            PrivateKey privateKey = clientKeys.getPrivate();
            return new RSAKey.Builder((RSAPublicKey) publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        }
        return null;
    };



}
