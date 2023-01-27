package io.curity.example.democlient;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.endpoint.NimbusJwtClientAuthenticationParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class OAuth2SecurityConfig {

    private ClientKeyLoader clientKeys = new ClientKeyLoader();

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
        NimbusJwtClientAuthenticationParametersConverter<OAuth2AuthorizationCodeGrantRequest> converter = new NimbusJwtClientAuthenticationParametersConverter<>(jwkResolver);
        converter.setJwtClientAssertionCustomizer((context) -> {
            // You can customize the JWT here. For example, you can change a claim.
            // Change the audience of the JWT from the token endpoint to token issuer name of the authorization server.
            context.getClaims().claim("aud","https://localhost:8443/oauth/v2/oauth-anonymous");
        });
        tokenResponseClient.addParametersConverter(converter);
        return tokenResponseClient;
    }

    // Provides the JSON web key that is used to sign the client JWT assertion
    Function<ClientRegistration, JWK> jwkResolver = (clientRegistration) -> {
        if (clientRegistration.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.PRIVATE_KEY_JWT)) {
            // This client uses RSA keys and signatures
            try {
                PublicKey publicKey = clientKeys.getRSAPublicKey();
                PrivateKey privateKey = clientKeys.getRSAPrivateKey();
                return new RSAKey.Builder((RSAPublicKey) publicKey)
                        .privateKey(privateKey)
                        .keyID(UUID.randomUUID().toString())
                        .build();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    };

}
