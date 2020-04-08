package zy.pointer.j2easy.endecrypt;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import zy.pointer.j2easy.framework.endecrypt.RSAUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class RSA {

    private static final String COMMENT_BEGIN_FLAG = "-----";
    private static final String RETURN_FLAG_R = "\r";
    private static final String RETURN_FLAG_N = "\n";

    private static String PRIVATE_KEY;
    private static String PUBLIC_KEY;

    static {
        /*
         Load the RAS-KEYS from classpath:resources
         */
        // 公钥 RAW
        InputStream pubIn = RSA.class.getClassLoader().getResourceAsStream("endecrypt"+ File.separator+"rsa"+ File.separator +"public_key.pem");
        try {
            PUBLIC_KEY = extractFromPem(inputStreamToString(pubIn));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 私钥 RAW
        InputStream privIn = RSA.class.getClassLoader().getResourceAsStream("endecrypt"+ File.separator+"rsa"+ File.separator +"private_key_pkcs8.pem");
        try {
            PRIVATE_KEY = extractFromPem(inputStreamToString(privIn));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decrypt( String data ) throws Exception {
       return RSAUtils.privateDecrypt( data ,  PRIVATE_KEY );
    }

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        String s = RSAUtils.privateDecrypt( "lPLClwL1m/x1LX4Qll/6pum6YqQerXSJmdxyT75klpbGXpWgkEhAHIV2nnSfiB0UHqfzF3n8jk0fdst+Cd5leKqDFlTgUGYPgHKMbo1FW7GJ7jK2a4Nf12WYF4w3hvHoUps2YTZAMwdiV4GhTvuNDbJxIYG4TI/X/KA6vaSXHm8="
          , PRIVATE_KEY);
        System.out.println(s);
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line+"\n");
        }
        return buffer.toString();
    }

    /**
     * 将 pem 原始文件进行解析获取 去格式化后的 密钥信息
     * @param rawKey
     * @return
     */
    private static String extractFromPem(String rawKey){
        String result = null;
        try {
            if(!Strings.isNullOrEmpty(rawKey)){
                String tmp = rawKey.replaceAll(RETURN_FLAG_R, "");
                List<String> validItemList = Lists.newArrayList();
                Iterable<String> splitIter = Splitter.on(RETURN_FLAG_N).trimResults().omitEmptyStrings().split(tmp);
                for(String str : splitIter) {
                    if(str != null && !str.startsWith(COMMENT_BEGIN_FLAG)){
                        validItemList.add(str);
                    }
                }
                Joiner joiner = Joiner.on("").skipNulls();
                result = joiner.join(validItemList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
