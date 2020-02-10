package zy.pointer.j2easy.framework.auth.jwt;

import zy.pointer.j2easy.framework.commons.DateUtil;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * JWT 在 缓存中的存储方案:
 *
 *  jwt:/{path}/uid => JWT 密文数据
 *  jwt:/ JWT 密文数据 => JSON
 */
public class JWTModel {

    private String uid;

    private String path;

    private Date issuedTime;

    private Date expireTime;

    public JWTModel(){}

    public JWTModel(String uid, String path, long minutes) {
        this.uid = uid;
        this.path = path;
        LocalDateTime now = LocalDateTime.now();
        issuedTime = DateUtil.localDateTimeToDate(now);
        expireTime = DateUtil.localDateTimeToDate( now.plusMinutes(minutes) );
    }

    public String getUid() {
        return uid;
    }

    public String getPath() {
        return path;
    }

    public Date getIssuedTime() {
        return issuedTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }
}
