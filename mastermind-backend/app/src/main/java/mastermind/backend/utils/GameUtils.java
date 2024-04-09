package mastermind.backend.utils;

import java.lang.StringBuilder;

public class GameUtils {
    
    // counts distinct characters in guess that exist in answer
    public static int getNumberFeedback(String guess, String answer) {
        // get distinct characters in
        String distinctCharacters = getDistinctCharacters(guess);

        // count distinct characters
        int distinctCount = 0;
        for (int i = 0; i < distinctCharacters.length(); i++) {
            char currentDistinct = distinctCharacters.charAt(i);
            // if a distinct character in guess exists in answer,
            // increment distinct count by 1
            if(answer.indexOf(currentDistinct) != -1) {
                distinctCount++;
            }
        }
        return distinctCount;
    }

    // counts characters in guess and answer at exact location
    public static int getLocationFeedback(String guess, String answer) {
        int locationCount = 0;
        // iterate each character in both strings
        // count the ones that match
        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            char answerChar = answer.charAt(i);
            if (guessChar == answerChar) {
                locationCount ++;
            }
        }
        return locationCount;
    }

    // creates string of distinct letters given string
    private static String getDistinctCharacters(String givenString) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < givenString.length(); i++) {
            char currentChar = givenString.charAt(i);
            if (result.indexOf(String.valueOf(currentChar)) == -1) {
                result.append(currentChar);
            }
        }
        return result.toString();
    }
}
