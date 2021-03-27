package net.oldhaven.customs.util;

import com.google.gson.JsonObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SkinUtil {
    /**
     * Some code from here to help with doing signature stuff:
     * https://github.com/Steveice10/MCAuthLib/blob/master/src/main/java/com/github/steveice10/mc/auth/data/GameProfile.java
     */
    private static final PublicKey SIGNATURE_KEY;
    static {
        try(InputStream in = SkinFix.class.getResourceAsStream("/yggdrasil_session_pubkey.der")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int length = -1;
            while((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }

            out.close();

            SIGNATURE_KEY = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(out.toByteArray()));
        } catch(Exception e) {
            throw new ExceptionInInitializerError("Missing/invalid yggdrasil public key.");
        }
    }

    static boolean verifySig(JsonObject obj) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(SIGNATURE_KEY);
            String value = obj.get("value").getAsString();
            String selfSig = obj.get("signature").getAsString();
            sig.update(value.getBytes());
            return sig.verify(
                    Base64.getDecoder().decode(
                        selfSig.getBytes(StandardCharsets.UTF_8)
                    )
            );
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return false;
    }

    static JsonObject readJsonFromURL(String ur) {
        try {
            URL url = new URL(ur);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.append(line);
                }
            }
            in.close();
            return MMUtil.gson.fromJson(lines.toString(), JsonObject.class);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
