package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = null;
        for (int i = 0; i < args.length - 1; i += 2) {
            if (args[i].equals("-fileName")) {
                fileName = args[i + 1];
            }
        }
        if (fileName == null || fileName.charAt(0) == '-') {
            System.out.println("You must pass a file name for database by -fileName prefix.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            CLI cli = CLI.create(fileName, scanner);
            cli.run();
        }
    }

    private static void createAccounts(String fileName) {
        BankAPI bankAPI = new BankAPI(fileName);
        for (int i = 0; i < 10000; i++) {
            String info = bankAPI.createNewAccount().split("\n")[3];
            System.out.println(info);
        }
    }
}