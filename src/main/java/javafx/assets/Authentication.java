package javafx.assets;

public class Authentication {

    public static boolean doesPasswordComplieToPasswordRules(String newPassword) {
        return isPW8DigitsLong(newPassword) && doesPWContainRightLetters(newPassword) && doesPWContainSpecialCharacters(newPassword);
    }

    private static boolean doesPWContainSpecialCharacters(String password) {
        char[] passwordChar = password.toCharArray();

        for (char letter : passwordChar) {
            if (!Character.isAlphabetic(letter) && !Character.isDigit(letter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesPWContainRightLetters(String password) {
        return (!password.equals(password.toLowerCase()) && !password.equals(password.toUpperCase()));
    }

    private static boolean isPW8DigitsLong(String password) {
        char[] passwordLength = password.toCharArray();
        return passwordLength.length >= 8;
    }
}
