package banking;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardUtils {
    public static Card createCardFromResult(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String number = resultSet.getString("number");
        String pin = resultSet.getString("pin");
        int balance = resultSet.getInt("balance");
        return new Card(id, number, pin, balance);
    }

    public static Card createCard() {
        StringBuilder builder = new StringBuilder(16);
        int iin = 400000;
        builder.append(iin)
                .append(generateCustomerNumber());
        int sumCheck = CardValidator.generateSumCheck(builder.toString());
        builder.append(sumCheck);
        return new Card(builder.toString(), generateRandomPin());
    }

    private static int generateCustomerNumber() {
        int customerNumber = 0;
        while (customerNumber == 0) {
            customerNumber = (int) (Math.random() * 10);
        }
        for (int i = 0; i < 8; i++) {
            customerNumber *= 10;
            customerNumber += (int) (Math.random() * 10);
        }
        return customerNumber;
    }

    private static String generateRandomPin() {
        int pin = (int) (Math.random() * 10000);
        StringBuilder builder = new StringBuilder(4);
        if (pin < 10) {
            builder.append("000");
        } else if (pin < 100) {
            builder.append("00");
        } else if (pin < 1000) {
            builder.append("0");
        }
        builder.append(pin);
        return builder.toString();
    }
}
