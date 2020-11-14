package com.example.demo.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    private static String key="_secret";

    public static String createToken(String subject, Map<String,Object> claims)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256,key)
                .compact();
    }

    public static Claims parseToken(String token)
    {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody();
    }

    public <T> T getType(String token, Function<Claims,T> resolver)
    {
        return resolver.apply(parseToken(token));
    }

    public static  String getSubject(String token)
    {
        return parseToken(token).getSubject();
    }

    public static  String getUid(String token){
        return parseToken(token).get("uid").toString();
    }

    public static Long getTime(String token)
    {
        return (Long)parseToken(token).get("nowTime");
    }
    public static boolean CompareDate(String token1,String token2)
    {
        return getTime(token1)>=getTime(token2);
    }

}
