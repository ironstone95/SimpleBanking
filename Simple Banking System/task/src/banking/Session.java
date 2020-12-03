package banking;

public class Session {
    private Card loggedInCard;
    private final BankAPI bankAPI;
    private boolean isLoggedIn = false;

    public Session(String dbFileName) {
        this.bankAPI = new BankAPI(dbFileName);
    }

    public String createAccount() {
        return bankAPI.createNewAccount();
    }

    public boolean logIn(String cardNumber, String pin) {
        Card card = bankAPI.login(cardNumber, pin);
        if (card != null) {
            this.loggedInCard = card;
            this.isLoggedIn = true;
            return true;
        } else {
            this.isLoggedIn = false;
            return false;
        }
    }

    public boolean logOut() {
        loggedInCard = null;
        this.isLoggedIn = false;
        return true;
    }

    public String getCardBalance() {
        return String.valueOf(loggedInCard.getBalance());
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public void addIncome(int income) {
        bankAPI.addIncome(loggedInCard, income);
    }

    public boolean isCardExists(String cardNumber) {
        return bankAPI.isCardExists(cardNumber);
    }

    public boolean isCardNumberEquals(String cardNumber) {
        return this.loggedInCard.getCardNumber().equals(cardNumber);
    }

    // Be sure that card number exists in the database before using this method.
    public boolean transfer(String cardNumber, int amount) {

        return bankAPI.transfer(loggedInCard, cardNumber, amount);
    }

    public void closeAccount() {
        bankAPI.closeAccount(loggedInCard);
        logOut();
    }
}
