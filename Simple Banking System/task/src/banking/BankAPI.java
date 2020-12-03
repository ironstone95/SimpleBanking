package banking;

public class BankAPI {
    private final CardDao cardDao;

    public BankAPI(String dbFileName) {
        this.cardDao = new CardDao(dbFileName);
    }

    public String createNewAccount() {
        while (true) {
            Card card = CardUtils.createCard();
            if (cardDao.insertCard(card)) {
                return String.format("\nYour card has been created\n" +
                        "Your card number:\n%s\n" +
                        "Your card PIN: \n%s\n", card.getCardNumber(), card.getPin());
            }
        }

    }

    public Card login(String cardNumber, String pin) {
        Card card = cardDao.getCard(cardNumber);
        if (card != null) {
            if (card.getPin().equals(pin)) {
                return card;
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean isCardExists(String cardNumber) {
        return cardDao.isCardExists(cardNumber);
    }


    public void addIncome(Card loggedInCard, int income) {
        loggedInCard.addIncome(income);
        cardDao.addIncome(loggedInCard.getId(), income);
    }

    public boolean transfer(Card loggedInCard, String cardNumber, int amount) {
        if (!areResourcesSufficient(loggedInCard, amount)) {
            return false;
        }
        boolean success = cardDao.transfer(loggedInCard.getId(), cardNumber, amount);
        if (success) {
            loggedInCard.withdraw(amount);
        }
        return success;
    }

    private boolean areResourcesSufficient(Card card, int amount) {
        return amount <= card.getBalance();
    }

    public void closeAccount(Card loggedInCard) {
        cardDao.closeAccount(loggedInCard.getId());
    }
}
