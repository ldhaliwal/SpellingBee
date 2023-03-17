import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Liliana Dhaliwal
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<>();
    }

    // Generates all possible substrings and permutations of the letters.
    // Stores them all in the ArrayList words.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateWords("", letters);
    }

    private void generateWords(String word, String str) {
        if (str.length() == 0) {
            if(!word.equals("")){
                words.add(word);
            }
            return;
        }

        for(int i = 0; i < str.length(); i++){
            // Creates a new word and a new string
            String newWord = word + str.charAt(i);
            String newString = str.substring(0, i) + str.substring(i + 1);
            // Only adds the new word if the String has values
            if(!word.equals("")){
                words.add(word);
            }
            // Recurses to generate all iterations of the words
            generateWords(newWord, newString);
        }
    }

    // Applies mergesort to sort all words.
    public void sort() {
        // Calls the merge sort algorithm
        words = mergeSort(words, 0, words.size() - 1);
    }

    private ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Checks if there is only one element left in the arraylist, adds it to a new arraylist, and returns it.
        if (low == high) {
            ArrayList<String> arr = new ArrayList<>();
            arr.add(words.get(low));
            return arr;
        }

        int mid = (low + high) / 2;

        // Recurses on both halves of the original arraylist
        ArrayList<String> arr1 = mergeSort(words, low, mid);
        ArrayList<String> arr2 = mergeSort(words, mid + 1, high);

        // Merges the sorted halves
        return merge(arr1, arr2);
    }

    private ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        int i = 0;
        int j = 0;
        ArrayList<String> sorted = new ArrayList<>();

        // Iterates through both arraylists and adds elements in order to the new arraylist
        while (i < arr1.size() && j < arr2.size()) {
            if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                sorted.add(arr1.get(i));
                arr1.remove(i);
            }
            else{
                sorted.add(arr2.get(j));
                arr2.remove(j);
            }
        }
        // Adds any leftover elements from whatever half has more elements
        while (i < arr1.size()) {
            sorted.add(arr1.get(i));
            arr1.remove(i);
        }
        while ( j < arr2.size()) {
            sorted.add(arr2.get(j));
            arr2.remove(j);
        }
        // Returns the sorted arraylist
        return sorted;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // For each word, uses binary search to see if it is in the dictionary.
    // If it is not in the dictionary, it's removed from words.
    public void checkWords() {
        // Iterates through each element in words and checks it against the Dictionary
        for(int i = 0; i < words.size(); i++){
            if(!(found(words.get(i), 0, DICTIONARY_SIZE))){
                // removes the word if it is not found in the Dictionary
                words.remove(i);
                i--;
            }
        }
    }

    public boolean found(String s, int low, int high){
        //  Returns false if the word is not found
        if(low > high){
            return false;
        }

        int mid = (low + high) / 2;

        // Returns true if the word is found
        if (DICTIONARY[mid].equals(s)) {
            return true;
        }
        // Changes low or high based on if the value of the word is higher or lower than
        // the middle word in the section of the Dictionary being checked
        else if (DICTIONARY[mid].compareTo(s) < 0) {
            low = ++mid;
        }
        else if (DICTIONARY[mid].compareTo(s) > 0) {
            high = --mid;
        }

        // Recurses on updated section of the Dictionary
        return found(s, low, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
