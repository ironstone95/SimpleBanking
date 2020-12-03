package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class CardDao {
    private final SQLiteDataSource dataSource;

    public CardDao(String dbFileName) {
        String url = "jdbc:sqlite:" + dbFileName;
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        createCardTableIfNotExists();
    }

    public Card getCard(String cardNumber) {
        Card card = null;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM card WHERE number = " + cardNumber)) {
                    if (resultSet.next()) {
                        card = CardUtils.createCardFromResult(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public boolean isCardExists(String cardNumber) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT number FROM card WHERE number = " + cardNumber)) {
                    if (resultSet.next()) {
                        String foundNumber = resultSet.getString("number");
                        if (foundNumber != null) {
                            return foundNumber.equals(cardNumber);
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertCard(Card card) {
        if (isCardExists(card.getCardNumber())) {
            return false;
        }
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                int id = -1;
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM card")) {
                    resultSet.next();
                    id = resultSet.getInt(1);
                }
                card.setId(id);
                String query = String.format("INSERT INTO card VALUES(%d,%s,%s,%d)", card.getId(), card.getCardNumber(), card.getPin(), card.getBalance());
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createCardTableIfNotExists() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                String query = "CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0" +
                        ");";
                statement.execute(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIncome(int cardId, int income) {
        String updateQuery = "UPDATE card " +
                "SET balance = (balance + ?) " +
                "WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setInt(1, income);
                statement.setInt(2, cardId);
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean transfer(int cardId, String cardNumber, int amount) {
        String withdrawalQuery = "UPDATE card SET balance = (balance - ?) WHERE id = ?";
        String depositQuery = "UPDATE card SET balance = (balance + ?) WHERE number = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement withdraw = connection.prepareStatement(withdrawalQuery);
                 PreparedStatement deposit = connection.prepareStatement(depositQuery)) {

                withdraw.setInt(1, amount);
                withdraw.setInt(2, cardId);
                withdraw.executeUpdate();

                deposit.setInt(1, amount);
                deposit.setString(2, cardNumber);
                deposit.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeAccount(int cardId) {
        String deleteQuery = "DELETE FROM card WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setInt(1, cardId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
