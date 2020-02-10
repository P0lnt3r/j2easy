package zy.pointer.j2easy.framework.web.interceptors;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import zy.pointer.j2easy.framework.auth.components.JWT;
import zy.pointer.j2easy.framework.auth.jwt.JWTModel;
import zy.pointer.j2easy.framework.exception.jwt.JWTExpireException;
import zy.pointer.j2easy.framework.exception.jwt.JWTInvalidException;
import zy.pointer.j2easy.framework.exception.jwt.JWTKickOutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 身份识别拦截器
 *  通过对请求头中的 Authorization : JWT 这种方式对用户的身份进行识别.
 *      识别过程中包括 1.> JWT 合法性判断   : 是否符合 JWT 标准
 *                     2.> JWT 有效性判断   : 判断时间是否有效
 *                     3.> JWT 过期时间延长 : 如果有效,则延长有效时间
 */
@Slf4j
@Component
public class IdentificationInterceptor implements HandlerInterceptor {

    private static final String AUTH_KEY = "Authorization";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JWT JWT;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader( AUTH_KEY );
        if ( authorization != null ){
            JWTModel jwtModel = JWT.parse( authorization );
            if ( jwtModel == null ){
                log.warn( "[身份识别拦截器] JWT 信息不合法 , JWT = {}" , authorization );
                throw new JWTInvalidException();
            }

            String jwt = authorization;
            // 同一个 PATH 下,只能存储一个 JWTModel.uid 对应的缓存.
            // 首先以 jwt:/ {path} / {uid} 为 key 从缓存中抓取对应的缓存
            // 如果这个缓存不存在,则以本次的数据为准,更新到缓存中去.
            // 如果这个缓存存在,则比对两个的时间,如果缓存中 JWT 的颁发时间更晚一些,则当前的JWT相当于被挤掉了.
            String key = "jwt:/" + jwtModel.getPath() + "/" + jwtModel.getUid();
            String _jwt = null;
            if ( redisTemplate.opsForValue().get( key ) != null ){
                _jwt = redisTemplate.opsForValue().get( key ).toString();
                JWTModel _jwtModel = JWT.parse( _jwt );
                if ( jwtModel.getIssuedTime().before( _jwtModel.getIssuedTime() ) ){    // 如果当前的JWT颁发时间早于缓存中存在的JWT , 则说明被别人挤下去了.
                    log.warn( "[身份识别拦截器] 身份失效! 存在更新的JWT数据,即当前登陆状态被另一个终端给挤掉了");
                    throw new JWTKickOutException();
                }
            }
            redisTemplate.opsForValue().set( key , jwt );

            // 检查缓存中是否存在这个 JWT 对应的缓存,如果存在,则刷新它在缓存中的失效时间.
            key = "jwt:/" + jwt;
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString( jwtModel );
            boolean cacheResult = redisTemplate.opsForValue().setIfPresent( key , value , 30 , TimeUnit.MINUTES );
            if ( !cacheResult ){
                if ( jwtModel.getExpireTime().before( new Date() ) ){
                    log.warn("[身份识别拦截器] 身份失效! 当前颁发的JWT已过期");
                    throw new JWTExpireException();
                }
                redisTemplate.opsForValue().set( key , value );
            }
        }
        return true;
    }

}
