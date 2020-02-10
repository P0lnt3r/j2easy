package zy.pointer.j2easy.framework.web.interceptors;


import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import zy.pointer.j2easy.framework.exception.RepeatRequestAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 防重的做法
 *
 *  检查请求参数中 timestamp 与服务器时间差,如果超过2分钟, 则提示访问失效.
 *  如果时间有效 , 则访问 nonce 值 , nonce 值会保存到 服务器缓存中,失效时间3分钟.
 *  所以如果 nonce 值存在与缓存中,那么这个 nonce 值是无效的.
 *
 *  生产环境则必须存在 timestamp 和 nonce
 *  测试环境和本地环境 只在传递了 timestamp 和 nonce 才会进行检查.
 */
@Component
@Slf4j
public class RepeatRequestProtectInterceptor implements HandlerInterceptor {

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${env}") String env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String timestamp = request.getParameter( "timestamp" );
        String nonce = request.getParameter("nonce");
        // 检查 timestamp
        Long Ltimestamp = Convert.toLong( timestamp );

        if ( "prod".equals(env) ){
            if ( Ltimestamp == null ){
                String message = "timestamp为空或不存在";
                log.warn("[防重拦截器] - Timestamp 检查 : {}" , message );
                throw new RepeatRequestAccessException( message );
            }
            // 检查 nonce
            if ( nonce == null ){
                String message = "nonce为空或不存在";
                log.warn( "[防重拦截器] - Nonce 检查 : {}" , message );
                throw new RepeatRequestAccessException( message );
            }
        }
        if ( Ltimestamp != null ){
            long current = System.currentTimeMillis();
            long timeDiffer = Math.abs( current - Ltimestamp );
            if ( timeDiffer > 2 * 60 * 1000 ){  // 如果时间差大于2分钟,则直接屏蔽
                String message = "timestamp 失效";
                log.warn("[防重拦截器] - Timestamp 检查 : timestamp:{} - current{} = {} , 时间差大于2分钟" , timestamp , current , timeDiffer);
                throw new RepeatRequestAccessException( message );
            }
        }
        if ( nonce != null ){
            boolean setResult = redisTemplate.opsForValue().setIfAbsent( "nonce:" +  nonce , "1" , 3 , TimeUnit.MINUTES );
            if ( ! setResult ){
                String message = "nonce已存在";
                log.warn( "[防重拦截器] - Nonce 检查 : nonce 已存在." );
                throw new RepeatRequestAccessException(message);
            }
        }
        return true;
    }
}
