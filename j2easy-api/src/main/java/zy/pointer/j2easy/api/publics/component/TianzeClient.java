package zy.pointer.j2easy.api.publics.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zy.pointer.j2easy.api.publics.dto.UserVO;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class TianzeClient implements InitializingBean {

    @Value("${tianze.api.endpoint}")
    private String endpoint;

    private OkHttpClient okHttpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(endpoint);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(  5 , TimeUnit.SECONDS) 	//连接超时
                .readTimeout(5 , TimeUnit.SECONDS) 		//读取超时
                .build();
    }

    public UserVO getUserMsg( String code ){
        Request req = new Request.Builder()
                .url( endpoint + "/Account/GetUserMsg_usNe?code=" + code )
                .get()
                .build();
        Call call = okHttpClient.newCall(req);
        Response response = null;
        try {
            response = call.execute();
            if ( response.isSuccessful() ){
                String responseBody = response.body().string();
                System.out.println(responseBody);
                ObjectMapper mapper = new ObjectMapper();
                TZApiResponse res =  mapper.readValue(responseBody , TZApiResponse.class);
                if ( res.getResult() instanceof String ){
                    return null;
                }
                UserVO userVO = mapper.readValue(
                        mapper.writeValueAsString( res.getResult() ) , UserVO.class
                );
                return userVO;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
