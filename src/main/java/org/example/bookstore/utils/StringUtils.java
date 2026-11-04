package org.example.bookstore.utils;

import lombok.experimental.UtilityClass;
import org.example.bookstore.constant.Constant;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.example.bookstore.constant.Constant.CHARACTER_ALPHABET;

@UtilityClass
public class StringUtils {

    private static final String EMAIL_REGEX = "^([a-zA-Z0-9_.+-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern STAFF_CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isStringNull(String str) {
        return "null".equals(str);
    }

    public static boolean isNotEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static String generateRandomString(int lengthString) {
        StringBuilder result = new StringBuilder();
        int charactersLength = CHARACTER_ALPHABET.length();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < lengthString; i++) {
            int randomIndex = random.nextInt(charactersLength);
            result.append(CHARACTER_ALPHABET.charAt(randomIndex));
        }
        return result.toString();
    }

    public static String getStringValue(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key))
                .map(Object::toString)
                .orElse(null);
    }

    public static String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        return Optional.ofNullable(map.get(key))
                .map(Object::toString)
                .orElse(defaultValue);
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static String getString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    public static boolean validStaffCode(String staffCode) {
        if (staffCode == null || staffCode.isBlank()) {
            return false;
        }
        return STAFF_CODE_PATTERN.matcher(staffCode).matches();
    }

    public static int compareVersion(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (num1 < num2) {
                return -1;
            } else if (num1 > num2) {
                return 1;
            }
        }
        return 0;
    }

    public static String toFullWidthNumber(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '0' && c <= '9') {
                // '０' (U+FF10) là số 0 full-width
                sb.append((char) (c - '0' + '０'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toHalfWidthNumber(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '０' && c <= '９') {
                // '０' (U+FF10) -> '0' (U+0030)
                sb.append((char) (c - '０' + '0'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String checkAndReplaceIfNull(String str, String valueDefault) {
        if (str == null || "null".equals(str) || Constant.EMPTY_STR.equals(str)) {
            return valueDefault;
        }
        return str;
    }

    public static String padLeftZero(long number, int totalDigits) {
        return String.format("%0" + totalDigits + "d", number);
    }

    public static String toEmptyIfNull(String str) {
        if (str == null) {
            return Constant.EMPTY_STR;
        }
        return str;
    }

}
