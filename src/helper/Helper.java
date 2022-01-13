package helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Helper {

  private static HashSet<String> fiveLetters = new HashSet<String>();
  private static HashSet<String> goodStarters = new HashSet<String>();

  public static void main(String[] args) {
    readFiveLetterDictionary();
    extractGoodStarters();

    System.out.println("Good starter words:");
    for (String word : goodStarters) {
      System.out.print(word + " ");
    }

    Scanner reader = new Scanner(System.in); // Reading from System.in

    String line;
    do {
      System.out.println("\n\nEnter the letters which already are at their correct spot!\nFor blank spaces, use a '?'.");
      System.out.println("(Example: '?E??R')");
      line = reader.nextLine();
    } while (line.length() != 5 || !line.matches("[a-zA-Z?]+"));

    Character[] correctSpot = new Character[5];

    for (int i = 0; i < line.length(); i++) {
      if (line.charAt(i) == '?') {
        correctSpot[i] = null;
      } else {
        correctSpot[i] = line.charAt(i);
      }
    }

    line = "";
    do {
      System.out.println("\nEnter the letters which are definitely in the word.");
      System.out.println("Example: 'LRED'");
      line = reader.nextLine();
    } while (line.length() > 5 || !line.matches("[a-zA-Z]+"));

    HashSet<Character> contained = new HashSet<Character>();
    for (int i = 0; i < line.length(); i++) {
      contained.add(line.charAt(i));
    }
    
    line = "";
    do {
      System.out.println("\nEnter the letters which are definitely NOT in the word.");
      System.out.println("Example: 'UTFA'");
      line = reader.nextLine();
    } while (line.length() > 5 || !line.matches("[a-zA-Z]+"));

    HashSet<Character> uncontained = new HashSet<Character>();
    for (int i = 0; i < line.length(); i++) {
      uncontained.add(line.charAt(i));
    }

    HashSet<String> possibleSolutions = solver(correctSpot, contained, uncontained);
    System.out.println("\n\nPossible solutions: " + possibleSolutions.size() + "\n");
    for (String solution : possibleSolutions) {
      System.out.println(solution);
    }
    
    reader.close();
  }

  private static void readFiveLetterDictionary() {
    // read the whole dictionary
    try (BufferedReader br = new BufferedReader(new FileReader("src/helper/simple_five_letters.txt"))) {
      String line = br.readLine();
      while (line != null) {
        // only add words with a length of exactly 5
          fiveLetters.add(line);
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void extractGoodStarters() {
    // naive heuristic: choose words the five most common letters
    for (String word : fiveLetters) {
      word = word.toLowerCase();
      if (word.contains("e") && word.contains("s") && word.contains("a") && word.contains("r") ) {
        goodStarters.add(word);
      }
    }
  }

  private static HashSet<String> solver(Character[] correctSpot, HashSet<Character> contained,
      HashSet<Character> uncontained) {
    HashSet<String> possibleSolutions = new HashSet<String>();
    for (String s : fiveLetters) {
      if (checkWord(s.toLowerCase(), correctSpot, contained, uncontained)) {
        possibleSolutions.add(s);
      }
    }
    return possibleSolutions;
  }

  private static boolean checkWord(String word, Character[] correctSpot, HashSet<Character> contained,
      HashSet<Character> uncontained) {
    for (int i = 0; i < correctSpot.length; i++) {
      if (correctSpot[i] != null) {
        // should the word have another letter at the correct spot
        if (word.charAt(i) != Character.toLowerCase(correctSpot[i])) {
          // throw the word out
          return false;
        }
      }
    }

    for (Character letter : contained) {
      // should the word not contain a letter which has to be contained
      if (!word.contains(Character.toString(letter).toLowerCase())) {
        return false;
      }
    }

    for (Character letter : uncontained) {
      // should the word contain a letter which isn't allowed to be contained
      if (word.contains(Character.toString(letter).toLowerCase())) {
        return false;
      }
    }

    return true;
  }
}
