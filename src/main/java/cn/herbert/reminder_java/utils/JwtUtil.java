package cn.herbert.reminder_java.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    //令牌有效时间
    private static final long EXPIREE_TIME = 60 * 60 * 1000;

    private static final String SECRECT = "herbert";

    //获得令牌
    public static String getToken(String userId) {
        Date date = new Date(System.currentTimeMillis() + EXPIREE_TIME);
        JWTCreator.Builder builder = JWT.create();
        //为令牌赋值:用户
        builder.withAudience(userId);
        //为令牌赋值:失效时间
        builder.withExpiresAt(date);
        //为令牌加密
        Algorithm algorithm = Algorithm.HMAC256(SECRECT);
        String sign = builder.sign(algorithm);
        //为令牌赋值:用户
        return sign;
    }

    //获得令牌的归属用户
    public static String getUserId(String token) {
        String userId = JWT.decode(token).getAudience().get(0);
        return userId;
    }

    //校验通行证是否正确
    public static boolean checkToken(String token) {
        // 1. 基础校验
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        try {
            // 2. 解析并验证令牌
            Algorithm algorithm = Algorithm.HMAC256(SECRECT);
            JWTVerifier verifier = JWT.require(algorithm).build();

            // 3. 验证令牌签名和过期时间
            DecodedJWT jwt = verifier.verify(token);

            // 5. 验证通过
            return true;
        } catch (JWTVerificationException ex) {
            // 捕获各种JWT验证异常(签名无效、过期等)
            return false;
        } catch (Exception ex) {
            // 捕获其他意外异常(如算法不支持等)
            return false;
        }

    }
}
