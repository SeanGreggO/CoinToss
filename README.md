CoinTossGame.java

package CoinToss;

import java.util.Random;
import java.util.Scanner;

public class CoinTossGame {

    public static void startGame() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int score = 0;
        String playAgain = "yes";

        System.out.println("Welcome to the Coin Toss Game!");

        while (playAgain.equalsIgnoreCase("yes")) {
            System.out.print("Guess 'heads' or 'tails': ");
            String userGuess = scanner.nextLine().toLowerCase();

            System.out.print("Flipping the coin");
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(700);
                    System.out.print(".");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();

            int toss = random.nextInt(2);
            String coinResult = (toss == 0) ? "heads" : "tails";

            System.out.println("The coin landed on: " + coinResult);

            if (userGuess.equals(coinResult)) {
                System.out.println("You win this round!");
                score++;
            } else {
                System.out.println("Sorry, you lose this round!");
            }

            System.out.println("Your current score: " + score);
            System.out.print("Play again? (yes/no): ");
            playAgain = scanner.nextLine().toLowerCase();
            System.out.println();
        }

        System.out.println("Thanks for playing! Final score: " + score);
    }
}

