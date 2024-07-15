package com.tota.eccom.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InvalidJwtTokenUtil {

    private InvalidJwtTokenUtil() {
    }

    private static final Set<String> invalidTokensCache = ConcurrentHashMap.newKeySet();

    public static void addToken(String token) {
        invalidTokensCache.add(token.replace("Bearer ", ""));
    }

    public static boolean isTokenInvalid(String token) {
        return invalidTokensCache.contains(token.replace("Bearer ", ""));
    }
}
