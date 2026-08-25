package com.wxprogrem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

import static com.wxprogrem.constants.Constants.KEY;

public class Jwtutils {


    public static String getToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims",claims)  //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*1))  //添加过期时间
                .sign(Algorithm.HMAC256(KEY)); //指定算法配置密钥
    }
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY)) //解密算法和密钥
                .build() //生成验证器
                .verify(token) //验证token，生成一个解析后的jwt对象
                .getClaim("claims") //获取名为claims的载荷
                .asMap();
    }

}