package assignment.dictionary;

import java.io.*;
import java.util.*;
import javafx.application.Platform;

/**
 * A Thread that contains the application we are going to animate
 *
 */

public class MisSpellActionThread implements Runnable {

    DictionaryController controller;
    private final String textFileName;
    private final String dictionaryFileName;

    private LinesToDisplay myLines;
    private DictionaryInterface<String, String> myDictionary;
    private boolean dictionaryLoaded;

    /**
     * Constructor for objects of class MisspellActionThread
     *
     * @param controller
     */
    public MisSpellActionThread(DictionaryController controller) {
        super();

        this.controller = controller;
        textFileName = "src/main/resources/assignment/dictionary/check.txt";
        dictionaryFileName = "src/main/resources/assignment/dictionary/sampleDictionary.txt";

        myDictionary = new HashedMapAdaptor<String, String>();
        myLines = new LinesToDisplay();
        dictionaryLoaded = false;

    }

    @Override
    public void run() {

        loadDictionary(dictionaryFileName, myDictionary);


        Platform.runLater(() -> {
            if (dictionaryLoaded) {
               controller.SetMsg("The Dictionary has been loaded"); 
            } else {
               controller.SetMsg("No Dictionary is loaded"); 
            }
        });
        
        checkWords(textFileName, myDictionary);

    }

    /**
     * Load the words into the dictionary.
     *
     * @param theFileName The name of the file holding the words to put in the
     * dictionary.
     * @param theDictionary The dictionary to load.
     */
    public void loadDictionary(String theFileName, DictionaryInterface<String, String> theDictionary) {
        Scanner input;
        try {
// ADD CODE HERE
// >>>>>>>>>>> ADDED CODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            input = new Scanner(new File(theFileName));

            while(input.hasNextLine()){
                String line = input.nextLine();
                if(!line.isEmpty()) {
                    theDictionary.add(line, line);
                }
            }
            input.close();
            dictionaryLoaded = true;
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         


        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: " + theFileName);
            System.out.println(e.getMessage());
        }

    }

    /**
     * Get the words to check, check them, then put Wordlets into myLines. When
     * a single line has been read do an animation step to wait for the user.
     *
     */
    public void checkWords(String theFileName, DictionaryInterface<String, String> theDictionary) {
        Scanner input;
        try {
            // >>>>>>>>>>> ADDED CODE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            input = new Scanner(new File(theFileName));
            while(input.hasNextLine()) {
                String line = input.nextLine(); // Read the whole line

                // Split the line into segments of words and spaces
                String[] segments = line.split("(?<=\\s)|(?=\\s)");

                StringBuilder originalTextBuilder = new StringBuilder();
                for (String segment : segments) {
                    if (segment.matches("\\s+")) {
                        // If segment is purely space(s), append it directly
                        originalTextBuilder.append(segment);
                    } else {
                        // Process the non-space segment as a word
                        String word = segment;
                        String normalizedWord = word.toLowerCase().replaceAll("\\W", "");
                        boolean isCorrect = checkWord(normalizedWord, theDictionary);

                        // Append the word to the originalTextBuilder
                        originalTextBuilder.append(word);

                        // Create and add a Wordlet for the word (or word with preceding spaces) to the current line
                        Wordlet wordlet = new Wordlet(originalTextBuilder.toString(), isCorrect);
                        myLines.addWordlet(wordlet);
                        // Reset the originalTextBuilder for the next word
                        originalTextBuilder.setLength(0);
                    }
                }

                // At the end of the line, move to the next line in LinesToDisplay and update the GUI
                myLines.nextLine();
                showLines(myLines);
            }

            input.close(); // Close the file scanner
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: " + theFileName);
            System.out.println(e.getMessage());
        }

    }

    /**
     * Check the spelling of a single word.
     *
     */
    public boolean checkWord(String word, DictionaryInterface<String, String> theDictionary) {
        return theDictionary.contains(word);
    }

    private void showLines(LinesToDisplay lines) {
        try {
            Thread.sleep(500);
            Platform.runLater(() -> {
                if (myLines != null) {
                    controller.UpdateView(lines);
                }
            });
        } catch (InterruptedException ex) {
        }
    }

} // end class MisspellActionThread

