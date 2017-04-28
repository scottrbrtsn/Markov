import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

//adapted from https://gist.github.com/veryphatic/3190969

public class Markov {

	// Hashmap
	private static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
	static Random rnd = new Random();
	private static int numSentencesInput;
   // private static String regEx = "(?s).*\\b1\\b.*\\b2\\b.*\\b3\\b.*\\b4\\b.*\\b5\\b.*\\b6\\b.*\\b7\\b.*\\b8\\b.*\\b9\\b.*\\b0\\b.*";
    private static String regEx = "([0-9])";
	/*
	 * Main constructor
	 */
	public static void main(String[] args) throws IOException {
		
		// Create the first three entries (k:_start, k:_end, k:_one)
		markovChain.put("_start", new Vector<String>());//words that start sentences
		markovChain.put("_end", new Vector<String>());//words that end sentences
        markovChain.put("_one", new Vector<String>());//one word sentences
        numSentencesInput = 0;


        while(true) {
		// Get some words
            System.out.println("Markov Chain > " + markovChain.toString());
            System.out.println("");
            System.out.println("Enter your phrase > ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String sInput = in.readLine();
            System.out.println("");
            System.out.println("");

		// Add the words to the hash table and print answer
            System.out.println(splitAndCountWords(sInput));
		}
		
	}
	
	/*
	 * Add words
	 */
	private static String splitAndCountWords(String phrase) {
		// put each word into an array
		String[] words = phrase.split(" ");
        String response;
		if(words.length == 1){
            addWordToMarkovCollection(words[0]);
			response = generateOneWordSentence();
		}else{
            // Loop through each word, check if it's already added
            // if its added, then get the suffix vector and add the word
            // if it hasn't been added then add the word to the list
            // if its the first or last word then select the _start / _end key
            // then generate the next sentence
            addWordsToMarkovCollection(words);
            response = generateSentence();
            for (int i = 0; i < numSentencesInput/2; i ++){
               if (i == 0){

               }else{
                   response = response + " " + generateSentence();
               }
           }

		}
		return response;
	}

	private static void addWordToMarkovCollection (String word){
        Vector<String> oneWords = markovChain.get("_one");
        oneWords.add(word);
    }

	private static void addWordsToMarkovCollection(String[] words){

		// Add the start and end words to their own
        for (int i=0; i<words.length; i++) {
            //first word of input
            if (i == 0) {
                Vector<String> startWords = markovChain.get("_start");
                startWords.add(words[i]);

                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<>();
                    suffix.add(" " + words[i + 1]);
                    if(!words[i].matches(regEx) && !words[i+1].matches(regEx)) {
                        markovChain.put(" " + words[i], suffix);
                    }
                }
            //last word of input
            } else if (i == words.length - 1) {
                Vector<String> endWords = markovChain.get("_end");
                if (!words[i].contains(".") ||
                        !words[i].contains("?") ||
                        !words[i].contains("!")){
                    words[i] = words[i] + ".";
                }
                endWords.add(" " + words[i]);
            //middle words/sentences of input
            } else {
                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<>();
                    suffix.add(" " + words[i + 1]);
                    if(!words[i].matches(regEx) && !words[i+1].matches(regEx)) {
                        markovChain.put(" " + words[i], suffix);
                    }
                } else {
                    suffix.add(" " + words[i + 1]);
                    if(!words[i].matches(regEx) && !words[i+1].matches(regEx)) {
                        markovChain.put(" " + words[i], suffix);
                    }
                }
                if (words[i].contains(".") ||//input had more than one sentence
                        words[i].contains("?") ||
                        words[i].contains("!")){
                    numSentencesInput ++;
                    Vector<String> endWords = markovChain.get("_end");
                    endWords.add(" " + words[i]);
                    Vector<String> startWords = markovChain.get("_start");
                    startWords.add(words[i+1]);
                }
            }
        }

	}


	private static String generateOneWordSentence(){

        // Vector to hold the phrase
        Vector<String> newPhrase = new Vector<String>();

        // String for the next word
        String word;

        // Select the first word
        Vector<String> oneWords = markovChain.get("_one");
        int startWordsLen = oneWords.size();
        word = oneWords.get(rnd.nextInt(startWordsLen));
        newPhrase.add(word);

        return newPhrase.get(0);
    }

	/*
	 * Generate a markov phrase
	 */
	private static String generateSentence() {
		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<String>();
		
		// String for the next word
		String nextWord = "";
				
		// Select the first word
        Vector<String> startWords = markovChain.get("_start");
		int startWordsLen = startWords.size();
        int weight = (int)Instant.now().toEpochMilli();
        nextWord = startWords.get(rnd.nextInt(startWordsLen) % ((int)Instant.now().toEpochMilli()));//could be improved

		newPhrase.add(nextWord);
        Vector<String> wordSelection;
        // Keep looping through the words until we've reached the end
        System.out.println(nextWord);
		while (nextWord.length() != 0 && nextWord.charAt(nextWord.length()-1) != '.' && nextWord.charAt(nextWord.length()-1) != '?') {
            if(startWords.contains(nextWord)) {
                wordSelection = markovChain.get(" " + nextWord);
            }else{
                wordSelection = markovChain.get(nextWord);
            }
            if(wordSelection!=null) {
                int wordSelectionLen = wordSelection.size();
                nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
                newPhrase.add(nextWord);
            }else{
                break;
            }
            System.out.println(nextWord);
        }
		String sentence = "";

        for (int i = 0; i < newPhrase.size(); i ++){
            sentence = sentence + newPhrase.get(i);
        }
		return sentence;
	}

}