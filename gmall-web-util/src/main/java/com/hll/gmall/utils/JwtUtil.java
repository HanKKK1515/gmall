package com.hll.gmall.utils;

import io.jsonwebtoken.*;

import java.util.Map;

public class JwtUtil {

    public static String encode(String key, Map<String, Object> param, String salt){
        if(salt != null){
            key += salt;
        }

        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256, key);
        jwtBuilder = jwtBuilder.setClaims(param);

        return jwtBuilder.compact();
    }

    public static Map<String, Object> decode(String token, String key, String salt) {
        if (salt != null) {
            key += salt;
        }

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
           return null;
        }

        return  claims;
    }
}
