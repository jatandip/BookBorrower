package com.example.vivlio;

public class ValidateISBN {

    public boolean verify(String code) {
        if (code.length() == 13 && isIsbn13(code)) {
            return true;
        } else if (code.length() == 10 && isIsbn10(code)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isIsbn13(String code) {
        return checkDigit(code, 13);
    }

    private boolean isIsbn10(String code) {
        return checkDigit(code, 10);
    }

    private boolean checkDigit(String code, int length) {
        int len = length - 1;
        int[] digits = new int[len];
        int digitSum = 0;
        int lastDigit;

        if (length == 13) {
            lastDigit = Integer.parseInt(String.valueOf(code.charAt(len)));
            for (int i = 0; i < len; i++) {
                int digit = Integer.parseInt(String.valueOf(code.charAt(i)));

                if (i % 2 == 0) {
                    digits[i] = digit;
                } else {
                    digits[i] = digit * 3;
                }
            }

            for (int i = 0; i < digits.length; i++) {
                digitSum = digitSum + digits[i];
            }

            if (10 - (digitSum % 10) == lastDigit) {
                return true;
            } else if ((digitSum % 10) == 0 && lastDigit == 0) {
                return true;
            } else {
                return false;
            }

        } else if (length == 10) {
            if (String.valueOf(code.charAt(len)).equals("X") ||
                    String.valueOf(code.charAt(len)).equals("x")) {
                lastDigit = 10;
            } else {
                lastDigit = Integer.parseInt(String.valueOf(code.charAt(len)));
            }

            int weight = 11;
            for (int i = 0; i < len; i++) {
                int digit = Integer.parseInt(String.valueOf(code.charAt(i)));
                weight--;
                digits[i] = digit * weight;
            }

            for (int i = 0; i < digits.length; i++) {
                digitSum = digitSum + digits[i];
            }

            if (11 - (digitSum % 11) == lastDigit) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }
}
