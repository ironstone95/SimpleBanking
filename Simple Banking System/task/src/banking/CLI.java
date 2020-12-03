package banking;

import java.util.Scanner;

public class CLI {
    private final Session session;
    private final Scanner scanner;
    private boolean isRunning = true;

    private CLI(String dbFilename, Scanner scanner) {
        this.scanner = scanner;
        this.session = new Session(dbFilename);
    }

    public void run() {
        while (isRunning) {
            showMenu();
            String input = scanner.nextLine();
            acceptInput(input);
        }
        session.logOut();
        System.out.println("\nBye!");
    }

    private void acceptInput(String input) {
        if (session.isLoggedIn()) {
            accountMenuEvent(input);
        } else {
            mainMenuEvent(input);
        }
    }

    private void mainMenuEvent(String input) {
        switch (input) {
            case "1":
                createAccount();
                break;
            case "2":
                logIntoAccount();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Unknown Command");
        }
    }

    private void accountMenuEvent(String input) {
        switch (input) {
            case "1":
                showBalance();
                break;
            case "2":
                addIncome();
                break;
            case "3":
                doTransfer();
                break;
            case "4":
                closeAccount();
                break;
            case "5":
                logOut();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Unknown Command");
        }
    }

    private void exit() {
        isRunning = false;
    }

    private void createAccount() {
        String info = session.createAccount();
        System.out.println(info);
    }

    private void logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        boolean credentialsCorrect = CardValidator.isCardValid(cardNumber) && session.logIn(cardNumber, pin);
        if (credentialsCorrect) {
            System.out.println("\nYou have successfully logged in!\n");
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void showBalance() {
        System.out.println("Balance: " + session.getCardBalance());
    }

    private void addIncome() {
        System.out.println("Enter income: ");
        while (true) {
            try {
                int income = Integer.parseInt(scanner.nextLine());
                session.addIncome(income);
                System.out.println("Income was added!");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Error: You must enter an integer for income.");
            }
        }
    }

    private void doTransfer() {
        System.out.println("Transfer\nEnter card number:");
        String cardNumber = scanner.nextLine();
        if (!CardValidator.isCardValid(cardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again.");
        }

        if (!session.isCardExists(cardNumber)) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer.");
        try {
            int amount = Integer.parseInt(scanner.nextLine());
            if (amount < 0) {
                System.out.println("Enter an integer greater than 0.");
                return;
            }
            if (session.isCardNumberEquals(cardNumber)) {
                System.out.println("You can't transfer money to same account!");
                return;
            }
            boolean transferSuccess = session.transfer(cardNumber, amount);
            if (transferSuccess) {
                System.out.println("Success!");
            } else {
                System.out.println("Not enough money!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong input. Try an integer");
        }
    }

    private void closeAccount() {
        session.closeAccount();
        System.out.println("The account has been closed!");
    }

    private void logOut() {
        boolean isLoggedOut = session.logOut();
        if (isLoggedOut) {
            System.out.println("\nYou have successfully logged out!\n");
        } else {
            System.out.println("\nAn error occurred while logging out.\n");
        }
    }

    private void showMenu() {
        if (session.isLoggedIn()) {
            showAccountMenu();
        } else {
            showMainMenu();
        }
    }

    private void showMainMenu() {
        System.out.println("\n1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n");
    }

    private void showAccountMenu() {
        System.out.println("\n1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close Account\n" +
                "5. Log out\n" +
                "0. Exit\n");
    }

    public static CLI create(String dbFilename, Scanner scanner) {
        return new CLI(dbFilename, scanner);
    }
}
