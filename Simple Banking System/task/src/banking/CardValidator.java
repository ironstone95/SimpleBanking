package banking;

public class CardValidator {
    private static int getSumCheck(String cardNumber) {
        return cardNumber.charAt(cardNumber.length() - 1) - 48;
    }

    /**
     * This method is used while generating new cards. To validate a card do not pass last digit.
     *
     * @param cardNumber IIN and Account Number
     * @return sumCheck according to Luhn's Algorithm
     */
    public static int generateSumCheck(String cardNumber) {
        int[] numbers = getNumberArray(cardNumber);
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % 2 == 0) {
                int num = numbers[i] * 2;
                sum += num % 10;
                sum += num / 10;
            } else {
                sum += numbers[i];
            }
        }
        return (10 - (sum % 10)) % 10;
    }

    /**
     * Checks if a card number is valid according to Luhn's algorithm
     *
     * @param cardNumber - card number to be validated
     * @return validation of card number
     */
    public static boolean isCardValid(String cardNumber) {
        int passedSumCheck = getSumCheck(cardNumber);
        int foundSumCheck = generateSumCheck(cardNumber.substring(0, cardNumber.length() - 1));
        return passedSumCheck == foundSumCheck;
    }

    private static int[] getNumberArray(String cardNumber) {
        int[] numbers = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            numbers[i] = cardNumber.charAt(i) - 48;
        }
        return numbers;
    }
}
