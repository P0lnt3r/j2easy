package zy.pointer.j2easy.api.publics;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import okhttp3.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.publics.component.TZApiResponse;
import zy.pointer.j2easy.framework.web.model.RequestContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/publics")
@Api("PUBLICS")
public class PublicsController {

    @PostMapping("/test")
    public Object test(){
        String headers = RequestContext.ThreadLocal.get().getHeaders();
        String body    = RequestContext.ThreadLocal.get().getBody();
        System.out.println("Headers:\r\n" + headers);
        System.out.println("Body:\r\n" + body);
        return 1;
    }


    @PostMapping("/checkCode")
    @ApiOperation("远程检测 Code 合法性")
    @ApiImplicitParams({
            @ApiImplicitParam()
    })
    public Object checkCode( String code ){
        JSONObject obj = new JSONObject();
        obj.put("jwt","JWT:有效JWT");
        return obj;
    }

    public static void main(String[] args) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(  5 , TimeUnit.SECONDS) 	//连接超时
                .readTimeout(5 , TimeUnit.SECONDS) 		//读取超时
                .build();
        Request req = new Request.Builder()
                .url( "http://cloud.sztzjy.com/Account/GetUserMsg_usNe?code=tzs005" )
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
                System.out.println( res.getResult() );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
