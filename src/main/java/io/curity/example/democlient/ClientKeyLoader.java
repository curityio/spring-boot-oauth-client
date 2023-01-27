package io.curity.example.democlient;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ClientKeyLoader {

    //TODO: Load keys from keystore instead
    String privateKeyB64 = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCTElzeY3/iCJALBN5YsYMItqqig9KfixZCh6anHLJGDYAs5WQKpfHXEvhegrrXgDC+jW2ThBrjYSi0GPMrKm3B8M2I7KaNUMhelXcLghrkUwO29qGUW6FkRDE/TdO89XL7nGC6pdHDOieitRUs1czLe2Sz2diVPyaIu+mRQVtPrqHh9nJlIQuAw7A1BGjD6dw5/0AshBOemT0HVprXlTQPWEiA4KpwFCrK6npOAXVZwg222U3XSS44hrGQUz7uaVCPW0RYUkV/r91P0GndCs5+pmSLJpSqO2P4kE/nnDrGwDntH3xGnvkqOeSKR3e9eW+Zg4MKS5tTkDDmwsBvJ5mFAgMBAAECggEAI4kNTWKnNJszCqaLfsytR36zbf8B9jCqGTsjhj9Fx+1dKfvnFzePXpMj1doqgQVlQbV0Y29I++Wu6w0YPDbNPE4Nq2bau0xojYn5sJsoGkEApcWjtvAI5+weJhiM75wywx6+tHp6W3yCtSqhyiqCiCvxYg9h6HXQvrsArWsoP/EpKp5X4YscxegZEjKiblVxrORPqu6HDoEn2z48OVlqOucR7hy3TQxgMzsMlYhWHnYa97IDk2o3VhCPnb798WP3MQuoWOwoliLxuUMvhx6Eoum8/XjRA0Z8DwuARJHDg+l/N4Sb2WaQYajUlbq2QGQr+kfh56kzKmnlfMZ7zhxc4QKBgQC5t+QY/q9GPd0v1xfvQYqu570yeH22B9DXXIoqb7m8DagYDdtoU4j2Iz4NRUkFXnKZPw/LJy7vvnEvhweoka2jcqZOYOZWJ8L/Of9XPSfcMEfMn56VbsdlhOpmeSWh5rVuo0oxo4ckfKOcHkSuqA297DQPShtItByvR60G280X7QKBgQDKunLKssilN/UmGnLXncKeThp35gJbPoqaI03IDVyhqcCqdOKrw8swhczb/jaf0oB/csaYmmC4A4TacndSx3ee/5fScFcrK/WZjRihSnob+ho9+y8KNTyupO5uQZFaClWH33mi8IZmA2WFJrcsjkrafZrJO0fMtDl0B9A2/MYk+QKBgFPrgojA3RbScZZijZAxf6dVVCrwQ23zzizixhQy9CzDxwdHi7Nyu9rtRebdPgfNC9+vnUVhaVYwKhkd3RoQ6fOHpRqZsruPsA+Ad59GJTLcJjWSkyhXLIb1Lq4rZBPqmmVqJ+aSTEjygUPUcZiym7MMfuHBzN5ndObHbqpAvEZpAoGAMuHjJ+8Bnz5RQHQI+Qc2rscJq6hGVSJ4ZUGRJzH7WvQhUGcv8U6eNBqRFXJTwX9zEwn6wJiWx/jSIoXyXwuDgZvDPmVF66cH6LdaRDlONrFqXr4py1UbWvY4heHdGo1XdFfpzrSx9u7JT0ctWHiKXxaBgBpp5f4iIXZIk1SUYvkCgYAlF71Qnu/YD4oY76q/qpxVOR0ZkTOLpmaZWddiYPn+9Tyrch0GTr2cgB5GfDzt8C4G1zEUbyYho0gb0s+fDHsw5RWlpxEFNlCFN7QuYdxMO5Ho5BWlUMNQ0dERmp2r6q1uSb0Tlk5R84xPdF+X3gdJpZijga22Lh9/UCyCeqcHzg==";
    String publicKeyB64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkxJc3mN/4giQCwTeWLGDCLaqooPSn4sWQoempxyyRg2ALOVkCqXx1xL4XoK614Awvo1tk4Qa42EotBjzKyptwfDNiOymjVDIXpV3C4Ia5FMDtvahlFuhZEQxP03TvPVy+5xguqXRwzonorUVLNXMy3tks9nYlT8miLvpkUFbT66h4fZyZSELgMOwNQRow+ncOf9ALIQTnpk9B1aa15U0D1hIgOCqcBQqyup6TgF1WcINttlN10kuOIaxkFM+7mlQj1tEWFJFf6/dT9Bp3QrOfqZkiyaUqjtj+JBP55w6xsA57R98Rp75Kjnkikd3vXlvmYODCkubU5Aw5sLAbyeZhQIDAQAB";

    protected PublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyB64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    protected PrivateKey getRSAPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyB64);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));
    }
}
