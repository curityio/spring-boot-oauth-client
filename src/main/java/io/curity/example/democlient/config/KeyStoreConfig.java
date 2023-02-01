package io.curity.example.democlient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client.authentication.asymmetric-key")
public record KeyStoreConfig(
    String keyStoreFileName,
    String keyStorePassword,
    String keyStoreAlias,
    String keyStoreType
) {}
