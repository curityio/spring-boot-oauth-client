package io.curity.example.democlient.keystore;

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

    public static KeyPair getKeyPair(String keyStoreType, String keyStoreName, String keyStorePassword, String alias) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
     KeyStore keyStore = KeyStore.getInstance(keyStoreType);
     try (InputStream ksInputStream = ClassLoader.getSystemResourceAsStream(keyStoreName);) {
         keyStore.load(ksInputStream, keyStorePassword.toCharArray());
     }

     Certificate clientCert = keyStore.getCertificate(alias);
     PublicKey publicKey = clientCert.getPublicKey();
     PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());

     KeyPair keyPair = new KeyPair(publicKey, privateKey);
     return keyPair;

    }
}
