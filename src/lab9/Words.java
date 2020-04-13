package lab9;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Words {
    public static void freqWords(String filename){

        //Use a Map data structure to store the results
        Map<String, Integer> wordFreq = new TreeMap<>();

        Scanner input = null;
        try {
            input = new Scanner(new File(filename));

            while (input.hasNextLine()){
                for (String word: splitLineWords(input.nextLine())){
                    if(isValidWord(word)){
                        addToWordFreq(word, wordFreq);
                    }
                }
            }

            printResult(wordFreq, filename);
        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            input.close();
        }
    }

    private static String[] splitLineWords(String nextLine) {
        //Any non-alphabetic symbol should be considered a word boundary (or delimiter)
        var  wordsChars = new ArrayList<ArrayList<Character>>();
        int wordCount = 0;
        short charWordCount = 0;

        for (var c: nextLine.toCharArray()){
//System.Time
            if (isAlfaNumeric(c)){
                if(charWordCount == 0) wordsChars.add(new ArrayList<>());

                wordsChars.get(wordCount).add(c);
                charWordCount++;
            } else if(charWordCount != 0) {
                wordCount++;
                charWordCount = 0;
            }
        }

        var result = new ArrayList<String>();

        for(var chars: wordsChars){
            result.add(chars.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining())
            );
        }
        return result.toArray(String[]::new);
    }

    private static void addToWordFreq(String word, Map<String, Integer> wordFreq) {
        if(wordFreq.containsKey(word)){
            wordFreq.put(word, wordFreq.get(word) + 1);
        } else {
            wordFreq.put(word, 1);
        }
    }

    private static boolean isValidWord(String word) {
        return word.length() > 1;
    }

    private static boolean isAlfaNumeric(char c){
        return c >= 65 && c <= 90 //A-Z
            || c >= 97 && c <= 122 //a-z
            || c >= 48 && c <= 57; //0-9
    }

    private static void printResult(Map<String, Integer> result, String filename){
        System.out.println(String.format("Reading input from %s", filename));
        System.out.println(String.format("%-20s", "Word") + "Frequency");

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            System.out.println(String.format("%-20s", entry.getKey()) + entry.getValue());
        }
    }
}

//        __
//   hi  c(..)o    (
//     \__(-)     __)
//        /\     (
//       /(_)___)
//      w /|
//        | \
//        m  m
