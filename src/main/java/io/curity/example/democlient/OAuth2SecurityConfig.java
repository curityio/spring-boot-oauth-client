package io.curity.example.democlient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class OAuth2SecurityConfig {

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
}
