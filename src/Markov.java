
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

//adapted from https://gist.github.com/veryphatic/3190969

public class Markov {

	// Hashmap
	private static Hashtable<String, Vector<String>> markovChain = new Hashtable<String, Vector<String>>();
    private static Hashtable<String, Vector<String>> markovChainQuestions = new Hashtable<String, Vector<String>>();
    private static Hashtable<String, Vector<String>> markovChainAnswers = new Hashtable<String, Vector<String>>();
    private static List <String> latinQuestion = new ArrayList<>();
    private static List <String> latinAnswer = new ArrayList<>();
    private static Hashtable<String, Vector<String>> questionAndAnswers = new Hashtable<>();
    private static Hashtable<String, Vector<String>> thesaurus = new Hashtable<String, Vector<String>>();


    //how to keep track of state of conversation.


    static Random rnd = new Random();
	private static int numSentencesInput;
   // private static String numRegEx = "(?s).*\\b1\\b.*\\b2\\b.*\\b3\\b.*\\b4\\b.*\\b5\\b.*\\b6\\b.*\\b7\\b.*\\b8\\b.*\\b9\\b.*\\b0\\b.*";
    private static String numRegEx = "([0-9])";
	/*
	 * Main constructor
	 */
	public static void main(String[] args) throws IOException {
        setup();

        //if sentence is in question

        //upload and parse dictionary
        //uploadDictionary("../dictionary.txt");

        //uploadThesaurus("../thesaurus.txt");

        splitAndRespond(readFile("../Rumi-Poems-2.txt"));

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

        //chain that forms questions
        markovChainQuestions.put("_start", new Vector<>());//words that start questions
        markovChainQuestions.put("_end", new Vector<>());//words that end questions
        markovChainQuestions.put("_one", new Vector<>());//one word questions

        //chain that forms answers
        markovChainAnswers.put("_start", new Vector<>());//words that start answers
        markovChainAnswers.put("_end", new Vector<>());//words that end answers
        markovChainAnswers.put("_one", new Vector<>());//one word answers
        numSentencesInput = 0;
    }

	/*
	 * Add words
	 */
	private static String splitAndRespond(String phrase) {
		// put each word into an array
		String[] words = phrase.split(" ");
        String response;
		if(words.length == 1){
            addWordToMarkovCollection(words[0]);
			response = generateOneWordSentence();
		}else if(latinQuestion.contains(phrase)){//I've heard this question before
            addWordsToMarkovCollection(words);
            Vector<String> pair = questionAndAnswers.get(phrase);
            response = pair.get(rnd.nextInt(pair.size()));
        }else{//create a random response
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
        // Loop through each word, check if it's already added
        // if its added, then get the suffix vector and add the word
        // if it hasn't been added then add the word to the list
        // if its the first or last word then select the _start / _end key
        // then generate the next sentence
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
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
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
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
                        markovChain.put(" " + words[i], suffix);
                    }
                } else {
                    suffix.add(" " + words[i + 1]);
                    if(!words[i].matches(numRegEx) && !words[i+1].matches(numRegEx)) {
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
        nextWord = startWords.get(rnd.nextInt(startWordsLen) % weight);//could be improved

		newPhrase.add(nextWord);
        Vector<String> wordSelection;
        // Keep looping through the words until we've reached the end
        //System.out.println(nextWord);
		while (nextWord.length() != 0 && nextWord.charAt(nextWord.length()-1) != '.' && nextWord.charAt(nextWord.length()-1) != '?') {
            if(startWords.contains(nextWord)) {
                wordSelection = markovChain.get(" " + nextWord);
            }else{
                wordSelection = markovChain.get(nextWord);
            }
            if(wordSelection!=null) {
                int wordSelectionLen = wordSelection.size();
                nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen) % weight);
                newPhrase.add(nextWord);
            }else{
                break;
            }
      //      System.out.println(nextWord);
        }
		String sentence = "";

        for (int i = 0; i < newPhrase.size(); i ++){
            sentence = sentence + newPhrase.get(i);
        }
		return sentence;
	}


	private static String readFile (String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }



    //check Thesaurus logic
    private static void uploadThesaurus (String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String line = br.readLine();

            //skip intro

            //find first word = "A" or "A

            do{
               line = readNextSynonymSet(br);
            }
            while (!line.contains("zymotic"));//last word

        } finally {
            br.close();
        }
    }

    private static String readNextSynonymSet(BufferedReader br) throws IOException {
        String line = br.readLine();
        if(!line.contains(numRegEx)){//key
            //key =
        }else{//values
            while(!line.contains(numRegEx)){
                //log values
                //remove numRegEx
                //split by " " and take the words not numbers
                //remove any ,
                line = br.readLine();//read next line
            }//values
        }
        return line;
    }









    private static void uploadDictionary (String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String line = br.readLine();

            while (line != null) {
                if(!line.contains("***") && isUpperCase(line)) {//this is a word, first line

                }else if(false){//secondLine
                    //parse part of speech
                }else if(line.contains("Defn")){//word definition
                    //parse definition
                }else if(line.contains(numRegEx)){

                }

                line = br.readLine();//read next line

            }
        } finally {
            br.close();
        }
    }

    public static boolean isUpperCase(String s)
    {
        for (int i=0; i<s.length(); i++) {
            if (!Character.isUpperCase(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

}