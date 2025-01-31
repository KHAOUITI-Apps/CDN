/**
 * Copyright © KHAOUITI Apps 2025 | https://www.khaouitiapps.com/
 *
 * Author: KHAOUITI ABDELHAKIM (Software Engineer from ENSIAS)
 *
 * Any use, distribution, or modification of this code must be explicitly allowed by the owner.
 * For permissions, contact me or visit my LinkedIn:
 * https://www.linkedin.com/in/khaouitiabdelhakim/
 */

package com.khaouitiapps.CDN;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class URLSigner {
    private static final String SECRET_KEY = "6PEV9Kyiw_y9O7orD7UezgNgYedhWmjY2Kf_u1T-mKmR3S-gylF1ztsA-FA0j1LRvaagvflyVm14rVNWUF93l2A";

    public static String generateSignedUrl(String videoId, String ipAddress) throws Exception {
        long expiryTime = Instant.now().getEpochSecond() + (2 * 60 * 60); // 2 hours in seconds
        String token = generateToken(videoId, expiryTime, ipAddress);

        return "/video/stream/" + videoId + "?token=" + token + "&expires=" + expiryTime + "&ip=" + ipAddress;
    }

    public static boolean validateSignedUrl(String videoId, String token, long expires, String ipAddress) throws Exception {
        long currentTimestamp = Instant.now().getEpochSecond();
        System.out.println("Current Time: " + currentTimestamp);
        System.out.println("Token Expiry Time: " + expires);
        System.out.println("Client IP: " + ipAddress);

        if (currentTimestamp > expires) {
            System.out.println("Token has expired.");
            return false;
        }

        // Generate expected token using the same logic
        String expectedToken = generateToken(videoId, expires, ipAddress);

        System.out.println("Expected Token: " + expectedToken);
        System.out.println("Received Token: " + token);

        boolean isValid = expectedToken.equals(token);
        System.out.println("Token Valid: " + isValid);

        return isValid;
    }

    private static String generateToken(String videoId, long expires, String ipAddress) throws Exception {
        String data = videoId + ":" + expires + ":" + ipAddress;

        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(
                sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))
        );
    }
}
