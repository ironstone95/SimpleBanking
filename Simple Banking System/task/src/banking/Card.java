package banking;

public class Card {
    private int id;
    private final String cardNumber;
    private final String pin;
    private int balance;

    public Card(int id, String cardNumber, String pin, int balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public Card(String cardNumber, String pin) {
        this.id = -1;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void addIncome(int amount) {
        balance += amount;
    }
}
