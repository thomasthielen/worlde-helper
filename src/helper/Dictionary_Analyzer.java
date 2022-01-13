package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Dictionary_Analyzer {
  
  public static class Letter implements Comparable<Letter>{
    private char letter;
    private int occurence;
    
    public Letter(char letter) {
      this.letter = letter;
      occurence = 0;
    }
    
    public void incOccurence() {
      this.occurence++;
    }
    
    public char getLetter() {
      return this.letter;
    }
    
    public int getOccurence() {
      return this.occurence;
    }
    
    @Override
    public int compareTo(Letter otherLetter) {
        return Integer.compare(otherLetter.getOccurence(), getOccurence());
    }
  }

  private static HashSet<String> fiveLetters = new HashSet<String>();
  private static ArrayList<Letter> letters = new ArrayList<Letter>();


  public static void main(String[] args) {
    readFiveLetterDictionary();
    countLetterOccurences();
    Collections.sort(letters);
    for (Letter l : letters) {
      System.out.println(l.getLetter() + " " + l.getOccurence());
    }
  }

  private static void countLetterOccurences() {
    initializeLetters();
    for (Letter l : letters) {
      for (String word : fiveLetters) {
        if (word.contains(String.valueOf(l.getLetter()))) {
          l.incOccurence();
        }
      }
    }
 
  }
  
  private static void initializeLetters() {
    char[] letterArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    for (char c : letterArray) {
      letters.add(new Letter(c));
    }
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

  private static void createTextFileFiveLetters() {
    HashSet<String> fiveLetters = new HashSet<String>();
    // read the whole dictionary
    try (BufferedReader br = new BufferedReader(new FileReader("src/helper/simple_dictionary.txt"))) {
      String line = br.readLine();
      while (line != null) {
        // only add words with a length of exactly 5
        if (line.length() == 5) {
          fiveLetters.add(line);
        }
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    PrintWriter writer;
    try {
      writer = new PrintWriter(new File("src/helper/simple_five_letters.txt"));
      for (String word : fiveLetters) {
        writer.println(word);
      }
      writer.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }
}
