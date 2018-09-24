
import java.util.Random;
import java.util.Vector;

/**
 * Created by scottrobertson on 10/24/17.
 */
public class PhraseProcessor {

    private MarkovCollection markovCollection = new MarkovCollection();

    private Random rnd = new Random();

    public PhraseProcessor(MarkovCollection markovCollection){
        this.markovCollection = markovCollection;
    }

    /*
	 * Add words
	 */
    public String splitAndRespond(String phrase) {
        // put each word into an array
        String[] words = phrase.split(" ");
        String response;
        if(words.length == 1){
            markovCollection.addSingleWordToMarkovCollection(words[0]);
            response = generateOneWordSentence();
        }else{//create a random response
            markovCollection.addWordsToMarkovCollection(words);  //only if I want to keep a log of what people enter
            response = generateSentence(pickKeywordToStartResponse(words), phrase);
        }
        return response;
    }

    /*
     * Generate a markov phrase
     */
    private String generateSentence(String startWord, String previousSentence) {

        // Vector to hold the phrase
        Vector<String> newPhrase = new Vector<String>();
        newPhrase.add(startWord);

        // String for the next word
        String nextWord = startWord.toLowerCase();
        Vector<String> wordSelection;
        // Keep looping through the words until we've reached the end
        while (nextWord.length() != 0
                && nextWord.charAt(nextWord.length()-1) != '.'
                && nextWord.charAt(nextWord.length()-1) != '?'
                && nextWord.charAt(nextWord.length()-1) != '!') {
            wordSelection = markovCollection.getWordFromMarkovChain(nextWord);
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
            sentence = sentence + " " + newPhrase.get(i);
        }
        sentence = sentence + ".";
        if (sentence.equals(previousSentence)){
            sentence = generateSentence(generateRandomWord(), sentence);
        }
        return sentence;
    }

    private String generateRandomWord(){
        Object[] keySet = markovCollection.getKeySet().toArray();
        return (String) keySet[rnd.nextInt(keySet.length)];
    }

    private String pickKeywordToStartResponse (String [] input){
        String startWord = input[rnd.nextInt(input.length)];

        while(markovCollection.getCommonWords().contains(startWord.toLowerCase())
                || startWord == null
                || startWord.equals("")
                || startWord.contains(".")){
            startWord = input[rnd.nextInt(input.length)];
        }
        return toStartWord(startWord);
    }

    private String toStartWord(String s){
        char [] sArray = s.toCharArray();
        sArray[0] = Character.toUpperCase(sArray[0]);
        return String.valueOf(sArray);
    }

    private String generateOneWordSentence(){

        // Vector to hold the phrase
        Vector<String> newPhrase = new Vector<String>();

        // String for the next word
        String word;

        // Select the first word
        Vector<String> oneWords = markovCollection.getWordFromMarkovChain("_one");
        int startWordsLen = oneWords.size();
        word = oneWords.get(rnd.nextInt(startWordsLen));
        newPhrase.add(word);

        return newPhrase.get(0);
    }

}
