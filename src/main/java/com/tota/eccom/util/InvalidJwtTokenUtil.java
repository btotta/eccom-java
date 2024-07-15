package com.tota.eccom.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InvalidJwtTokenUtil {

    /*
        This class is used to store the invalid tokens in a cache.
        The cache is used to prevent the same token from being used after it has been invalidated.

        This can be replaced with a Redis cache so it don't lose invalid tokens when the application restarts.

        Fore now, it's more than enough for this project.
     */


    private static final Set<String> invalidTokensCache = ConcurrentHashMap.newKeySet();

    private InvalidJwtTokenUtil() {
    }

    public static void addToken(String token) {
        invalidTokensCache.add(token.replace("Bearer ", ""));
    }

    public static boolean isTokenInvalid(String token) {
        return invalidTokensCache.contains(token.replace("Bearer ", ""));
    }
}
