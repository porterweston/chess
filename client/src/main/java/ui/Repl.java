package ui;

import java.util.Scanner;

public class Repl {
    //UI instances
    private static PreLoginUI preLoginUI;
    private static PostLoginUI postLoginUI;
    private static GameplayUI gameplayUI;
    private static ObservingUI observingUI;

    public static State state;

    public Repl() {
        preLoginUI = new PreLoginUI();
        postLoginUI = new PostLoginUI();
        gameplayUI = new GameplayUI();
        observingUI = new ObservingUI();

        state = State.LOGGED_OUT;
    }

    public void run() {
        System.out.printf("%s%n%s%s%n%s", EscapeSequences.ERASE_SCREEN,
                String.format("%s%s", EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_TEXT_COLOR_MAGENTA),
                "Welcome to the CS 240 Chess Client! Type \"help\" to get started.",
                EscapeSequences.RESET_TEXT_BOLD_FAINT);

        Scanner scanner = new Scanner(System.in);
        var input = "";
        var result = "";
        while (!input.equals("quit")) {
            printPrompt();
            input = scanner.nextLine();

            switch (state) {
                case State.LOGGED_OUT -> result = preLoginUI.eval(input);
                case State.LOGGED_IN -> result = postLoginUI.eval(input);
                case State.IN_GAME -> result = gameplayUI.eval(input);
                case State.OBSERVING_GAME -> result = observingUI.eval(input);
            }
            System.out.printf("%s%n", result);
        }
    }

    private void printPrompt() {
        System.out.printf("%s%s[%s] >>> %s", EscapeSequences.SET_TEXT_BOLD, EscapeSequences.SET_TEXT_COLOR_WHITE, state,
            EscapeSequences.RESET_TEXT_BOLD_FAINT);
    }

}
