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

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static String generateSecretKey(int length) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[length];
        random.nextBytes(key);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(key);
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey(64); // Generates a 64-byte secret key
        System.out.println("Generated Secret Key: " + secretKey);
    }
}

