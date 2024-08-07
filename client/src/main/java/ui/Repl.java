package ui;

import java.util.Scanner;

public class Repl {
    //UI instances
    private static PreLoginUI preLoginUI;
    private static PostLoginUI postLoginUI;
    private static GameplayUI gameplayUI;

    public static State state;

    public Repl() {
        preLoginUI = new PreLoginUI();
        postLoginUI = new PostLoginUI();
        gameplayUI = new GameplayUI();

        state = State.LOGGED_OUT;
    }

    public void run() {
        System.out.printf("%s%n%s%s%n", EscapeSequences.ERASE_SCREEN,
                String.format("%s%s", EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_TEXT_COLOR_MAGENTA),
                "Welcome to the CS 240 Chess Client! Type \"help\" to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                switch (state) {
                    case State.LOGGED_OUT -> result = preLoginUI.eval(line);
                    case State.LOGGED_IN -> result = postLoginUI.eval(line);
                    case State.IN_GAME -> result = gameplayUI.eval(line);
                }
                System.out.printf("%s%n", result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.printf("%s[%s] >>> ", EscapeSequences.SET_TEXT_COLOR_WHITE, state);
    }

}
