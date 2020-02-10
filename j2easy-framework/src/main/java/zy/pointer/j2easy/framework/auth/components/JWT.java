package zy.pointer.j2easy.framework.auth.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.framework.auth.jwt.JWTModel;

import javax.crypto.SecretKey;

@Component
public class JWT implements InitializingBean {

    /** JWT 签名密钥 */
    @Value("${jwt.signature.secretkey}") String secretKey;

    private SecretKey key;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化 JWT 签名密钥
        key = Keys.hmacShaKeyFor( secretKey.getBytes() );
    }

    public String issue( JWTModel jwtModel ){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(jwtModel);
            return Jwts.builder().setSubject( payload ).signWith(key).compact();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JWTModel parse( String jwt ){
        ObjectMapper mapper = new ObjectMapper();
        try{
            String json = Jwts.parser().setSigningKey(key)
                    .parseClaimsJws( jwt ).getBody().getSubject();
            return mapper.readValue( json , JWTModel.class );
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

//        System.out.println( UUID.randomUUID().toString(true) );
//        String uuid = UUID.randomUUID().toString(true);

        String uuid = "8db801f3edf24d318f7bba30420e0b61";
        SecretKey key = Keys.hmacShaKeyFor( uuid.getBytes() );
        JWT JWT = new JWT();
        JWT.key = key;

        String jwt = JWT.issue( new JWTModel( "1" , "path" , 10 ) );
        System.out.println(jwt);
        System.out.println(  JWT.parse(jwt) );
    }

}
