package comic.ali.com.comicv2.model;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by AliPro on 3/15/2015.
 */
public class Tools {
    private static final String publickey = "gHEutds==DHtehhapdd";
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String encrypt(final String s) {
        byte[] data = null;
        data = s.getBytes();
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        data = (base64 + publickey).getBytes();
        String res = Base64.encodeToString(data, Base64.NO_WRAP);
        return res;
    }
}
