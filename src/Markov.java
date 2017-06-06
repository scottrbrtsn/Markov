import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

//adapted from https://gist.github.com/veryphatic/3190969

public class Markov {

	// Hashmap
	private static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
    private static List <String> commonWords = new ArrayList<>();


    //how to keep track of state of conversation.

    private static int startWordsLen;
    static Random rnd = new Random();
   // private static String numRegEx = "(?s).*\\b1\\b.*\\b2\\b.*\\b3\\b.*\\b4\\b.*\\b5\\b.*\\b6\\b.*\\b7\\b.*\\b8\\b.*\\b9\\b.*\\b0\\b.*";
    private static String numRegEx = "([0-9])";
	/*
	 * Main constructor
	 */
	public static void main(String[] args) throws IOException {
        setup();

        //if sentence is in question

        addWordsToMarkovCollection(readFile("../Oscar_Wilde.txt").split(" "));
        addWordsToMarkovCollection(readFile("../Shakespeare.txt").split(" "));
        addWordsToMarkovCollection(readFile("../Asimov.txt").split(" "));

        System.out.println(markovChain.toString());
        while(true) {
            System.out.println("");
            System.out.println("Enter your phrase > ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String sInput = in.readLine();
            System.out.println("");
            System.out.println("");

		// Add the words to the hash table and print answer
            System.out.println(splitAndRespond(sInput));
		}
		
	}

	private static void setup () {
        // Create the first three entries (k:_start, k:_end, k:_one)
        //generic chain
        markovChain.put("_start", new Vector<>());//words that start sentences
        markovChain.put("_end", new Vector<>());//words that end sentences
        markovChain.put("_one", new Vector<>());//one word sentences
        markovChain.put("_questions", new Vector<>());//one word sentences

        commonWords.add("the");
        commonWords.add("a");
        commonWords.add("I");
        commonWords.add("he");
        commonWords.add("she");
        commonWords.add("it");
        commonWords.add("at");
        commonWords.add("on");
        commonWords.add("in");
        commonWords.add("of");
        commonWords.add("what");
        commonWords.add("do");
        commonWords.add("you");
        commonWords.add("how");
        commonWords.add("are");
        commonWords.add("is");

    }

	/*
	 * Add words
	 */
	private static String splitAndRespond(String phrase) {
		// put each word into an array
		String[] words = phrase.split(" ");
        startWordsLen = words.length;
        String response;
		if(words.length == 1){
            addSingleWordToMarkovCollection(words[0]);
			response = generateOneWordSentence();
		}else{//create a random response
            //addWordsToMarkovCollection(words);  only if I want to keep a log of what people enter
            response = generateSentence(pickKeywordToStartResponse(words));
		}
		return response;
	}

	private static void addSingleWordToMarkovCollection(String word){
        Vector<String> oneWords = markovChain.get("_one");
        oneWords.add(word);
    }

	private static void addWordsToMarkovCollection(String[] words){
        // Loop through each word, check if it's already added
        // if its added, then get the suffix vector and add the word
        // if it hasn't been added then add the word to the list
        // if its the first or last word then select the _start / _end key
        // then generate the next sentence
		// Add the start and end words to their own
        for (int i=0; i<words.length; i++) {
            //first word of input
            if (i == 0) {
                Vector<String> startWords = markovChain.get("_start");//for some reason start words have a lot more than I'd expect
                if(!startWords.isEmpty()
                && !startWords.contains(words[i])) {
                    startWords.add(words[i].replace(" ", ""));
                }

                Vector<String> suffix = markovChain.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<>();
                    suffix.add(" " + words[i + 1].replace("/n", " ").replace(",", " "));
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
                        markovChain.put(" " + words[i].replace("/n", " ").replace(",", " ").toLowerCase(), suffix);
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
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
                        markovChain.put(" " + words[i].replace("/n", " ").replace(",", " "), suffix);
                    }
                } else {
                    suffix.add(" " + words[i + 1]);
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
                        markovChain.put(" " + words[i].replace("/n", " ").replace(",", " "), suffix);
                    }
                }
                if (words[i].contains(".") ||//input had more than one sentence
                        words[i].contains("?") ||
                        words[i].contains("!")){
                    Vector<String> endWords = markovChain.get("_end");
                    endWords.add(" " + words[i]);
                    Vector<String> startWords = markovChain.get("_start");
                    startWords.add(words[i+1]);
                }
            }
        }

	}

	/*
	 * Generate a markov phrase
	 */
	private static String generateSentence(String startWord) {

		// Vector to hold the phrase
		Vector<String> newPhrase = new Vector<String>();
        newPhrase.add(startWord);

        // String for the next word
		String nextWord = " " + startWord.toLowerCase();
        Vector<String> wordSelection;
        // Keep looping through the words until we've reached the end
		while (nextWord.length() != 0
                && nextWord.charAt(nextWord.length()-1) != '.'
                && nextWord.charAt(nextWord.length()-1) != '?'
                && nextWord.charAt(nextWord.length()-1) != '!') {
                wordSelection = markovChain.get(nextWord);
            if(wordSelection!=null
                    && !nextWord.contains(".")
                    && !nextWord.contains("!")
                    && !nextWord.contains("?")) {
                nextWord = wordSelection.get(rnd.nextInt(wordSelection.size()));
//                        % (int) Instant.now().toEpochMilli());
                    newPhrase.add(nextWord);
            }else{
                break;
            }
        }
		String sentence = "";

        for (int i = 0; i < newPhrase.size(); i ++){
            sentence = sentence + newPhrase.get(i);
        }
		return sentence;
	}

    private static String pickKeywordToStartResponse (String [] input){
        String startWord = "";
        Vector <String> toCheck;
        for (String s : input){
            toCheck = markovChain.get(" " + s.toLowerCase().replace("?", ""));
            if(commonWords.contains(s.toLowerCase())){
                continue;
            }else if (toCheck != null){
                startWord = s;
                break;
            }else{
                startWord = input[0];
            }

        }

        return toStartWord(startWord);
    }

    private static String toStartWord(String s){
        char [] sArray = s.toCharArray();
        sArray[0] = Character.toUpperCase(sArray[0]);
        return String.valueOf(sArray);
    }


	private static String readFile (String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line + " ");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
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

}