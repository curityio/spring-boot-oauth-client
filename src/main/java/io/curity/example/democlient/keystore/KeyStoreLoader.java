package io.curity.example.democlient.keystore;

import io.curity.example.democlient.config.KeyStoreConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreLoader {

    public static KeyPair getKeyPair(KeyStoreConfig keyStoreConfig) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
     KeyStore keyStore = KeyStore.getInstance(keyStoreConfig.keyStoreType());
     try (InputStream ksInputStream = ClassLoader.getSystemResourceAsStream(keyStoreConfig.keyStoreFileName())) {
         keyStore.load(ksInputStream, keyStoreConfig.keyStorePassword().toCharArray());
     }

     Certificate clientCert = keyStore.getCertificate(keyStoreConfig.keyStoreAlias());
     PublicKey publicKey = clientCert.getPublicKey();
     PrivateKey privateKey = (PrivateKey) keyStore.getKey(
             keyStoreConfig.keyStoreAlias(),
             keyStoreConfig.keyStorePassword().toCharArray()
     );

     KeyPair keyPair = new KeyPair(publicKey, privateKey);
     return keyPair;

    }
}
