package zy.pointer.j2easy.framework.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 *  演示原生使用 JWT 和使用第三方实现使用JWT
 *  第三方 github : https://github.com/jwtk/jjwt
 */
public class JWTRunCase {

    public static void main(String[] args) throws  Exception{
        nativeJWT();
    }

    /** 原生生成JWT */
    static void nativeJWT() throws Exception{

        // 定义 header  信息
        String header = "{\"alg\":\"HS256\"}";
        // 定义 payload 信息
        String payload = "{\"sub\":\"Joe\"}";

        // URLEncoding & Base64
        header = Base64.getUrlEncoder().encodeToString( header.getBytes() ) .replace( "=" , "")   ;
        payload = Base64.getUrlEncoder().encodeToString( payload.getBytes() ) .replace( "=" , "") ;

        // 即将进行签名的数据.
        String content = header + "." + payload ;

        // 签名 [ HMAC-SHA256 ]
        // 签名密钥的长度应大于256个字节以满足一定的安全性.
        String secretKey = "Hello-Hmacsha2561111111111111134444444444";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec( secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        // 执行数据签名
        byte[] bytes = sha256_HMAC.doFinal(content.getBytes());

        // 将签名后的数据进行 URLEncode 编码
        String signature = Base64.getUrlEncoder().encodeToString( bytes ).replace("=","");

        // JWT => header + "." + payload + "." + signature
        String jwt = header + "." + payload + "." + signature;
        System.out.println("jwt:"+jwt);

        // 同样的内容我们使用 第三方工具也走一遍.
        SecretKey key = Keys.hmacShaKeyFor( secretKey.getBytes() );
        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
        System.out.println("jws:" + jws);
    }

    static void simpleJWT(){
        // 生成JWT
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("Key:" + key.getFormat());
        String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
        System.out.println(jws);
        // 验证JWT
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(jws).getBody().getSubject();
        System.out.println("Subject-->" + subject);
        try{
            Jwts.parser().setSigningKey(key)
                    .parseClaimsJws( jws + "abcd" );
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("don't trust the JWT!");
        }
    }


}
