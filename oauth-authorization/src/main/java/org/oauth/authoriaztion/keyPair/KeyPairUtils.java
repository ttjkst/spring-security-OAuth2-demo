package org.oauth.authoriaztion.keyPair;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class KeyPairUtils {
    private  static KeyPair ref;

    static {
        ref = generate();
    }

   private static KeyPair generate(){
        KeyPair keyPair=null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
            keyPairGenerator .initialize(1024, secureRandom);
            keyPair = keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
         ;
        }
        return  keyPair;
    }

    public static  KeyPair getKey(){
        if(ref==null){
            throw new  UnsupportedOperationException("due to  ref is null check generateKey ErrorMSg");
        }
        return ref;
    }
}
