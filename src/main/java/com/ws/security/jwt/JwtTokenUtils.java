package com.ws.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description:    //TODO jwt工具类
 * @Author:         john
 * @CreateDate:     2020/3/8 15:45
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 15:45
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */

@Component
public class JwtTokenUtils {

    private Clock clock = DefaultClock.INSTANCE;
    //public static final String TOKEN_HEADER = "Authorization";
    //public static final String TOKEN_PREFIX = "Bearer ";

    // private static final String SECRET = "jwtsecretdemo";
    //  private static final String ISS = "echisan";

    //从application.yml中获取jwt.secret的值，注入到Secret中
    @Value("${jwt.secret:jwtsecret}")
    private String secret;

    @Value("${jwt.expiration:604800L }")
    private Long expiration;
    @Value("${jwt.tokenHead:Bearer}")
    private String tokenHead;
    // 过期时间是3600秒，既是1个小时
    private static final long DEFAULT_EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
   // private static final long EXPIRATION_REMEMBER = 604800L;

    // 创建token
    public String createToken(String username, boolean isRememberMe) {
        long expiration = isRememberMe ? this.expiration : DEFAULT_EXPIRATION;
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuer(tokenHead)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    // 是否已过期
    public boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    public Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
