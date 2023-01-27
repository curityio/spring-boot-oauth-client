package io.curity.example.democlient;

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

public class ClientKeyLoader {

    public static KeyPair fromKeyStore(String keyStoreName, String password, String keyName) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
     KeyStore keyStore = KeyStore.getInstance("JKS");
     try (InputStream ksInputStream = ClassLoader.getSystemResourceAsStream(keyStoreName);) {
         keyStore.load(ksInputStream, password.toCharArray());
     }

     Certificate clientCert = keyStore.getCertificate(keyName);
     PublicKey publicKey1 = clientCert.getPublicKey();
     PrivateKey privateKey1 = (PrivateKey) keyStore.getKey(keyName, password.toCharArray());

     KeyPair keyPair = new KeyPair(publicKey1, privateKey1);
     return keyPair;

    }
}
