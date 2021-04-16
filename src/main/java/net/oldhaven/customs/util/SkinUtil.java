package net.oldhaven.customs.util;

import com.google.gson.JsonObject;
import net.oldhaven.devpack.SingleCallback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SkinUtil {
    static Executor executor = Executors.newCachedThreadPool();

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

    static void getSkinFromUrl(String pngUrl, SingleCallback<BufferedImage> callback) {
        if(pngUrl == null || pngUrl.isEmpty()) {
            callback.run(null);
            return;
        }
        executor.execute(() -> {
            BufferedImage image = null;
            try {
                URL url = new URL(pngUrl);
                image = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                callback.run(image);
            }
        });
    }
}
