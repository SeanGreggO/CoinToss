package CoinToss;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Login {

    private static final String FILE_NAME = "UserDatabase.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to the Coin Toss Game ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("1") || choice.equals("register")) {
            registerUser(scanner);
        } else if (choice.equals("2") || choice.equals("login")) {
            loginUser(scanner);
        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }

    private static void registerUser(Scanner scanner) {
        try {
            System.out.print("Enter a username: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                return;
            }

            if (userExists(username)) {
                System.out.println("Username already taken. Try again.");
                return;
            }

            String password = readPassword("Enter a password: ", scanner);
            if (password == null || password.isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }

            String hash = hashPassword(password);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(username + "," + hash);
                writer.newLine();
            }

            System.out.println("Registration successful!");
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void loginUser(Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            String password = readPassword("Enter password: ", scanner);
            if (password == null) {
                System.out.println("Unable to read password.");
                return;
            }

            String hash = hashPassword(password);

            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("No users registered yet.");
                return;
            }

            boolean success = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hash)) {
                        success = true;
                        break;
                    }
                }
            }

            if (success) {
                System.out.println("Login successful!\n");
                CoinTossGame.startGame();
            } else {
                System.out.println("Invalid username or password.");
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static boolean userExists(String username) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String readPassword(String prompt, Scanner scanner) {
        Console console = System.console();
if (console != null) {
    char[] pwd = console.readPassword(prompt);
    return new String(pwd);
}
 else {
            System.out.print(prompt + " (input visible): ");
            return scanner.nextLine();
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
