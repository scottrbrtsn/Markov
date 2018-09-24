
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



//adapted from https://gist.github.com/veryphatic/3190969

public class Markov {

    //TODO
    //Setup interaction vs. auto mode

    //Setup auto-gen music
    //Add rhythm to auto mode, min and max length of sentence
    //  style:  ~8-12 first line, ~3-5 second line
    //Cleanup input to markov chain
    // MVP auto mode
    //
    //Add mode to turn off voice
    //
    //Connect voice to text
    //Specific words trigger different levels of ambiance
    //MVP interactive mode
    //
    //Bonus feature:
    //Adjust voice quality
    //
    //make auto mode a conversation

    //how to keep track of state of conversation.

    private static MarkovCollection markovCollection = new MarkovCollection();
    private static PhraseProcessor phraseProcessor = new PhraseProcessor(markovCollection);
    private static VoiceAndText voiceAndText = new VoiceAndText("male");
    private static String text; // string to speech

	/*
	 * Main constructor
	 */
	public static void main(String[] args) throws IOException {
        markovCollection.setup(FunctionalInstructions.selectAuthor());
        //System.out.println(markovChain.toString());
        startMarkov(FunctionalInstructions.selectMode());
    }

	private static void startMarkov(String mode) throws IOException{
        System.out.println("Begin");
        if(mode.equals("1")){
            try {
                autoMode(0, "Love space frontier start death life light");
            }catch (InterruptedException e){
                System.out.println("Something interrupted the delay: " + e);
            }
        }else{
                interactiveMode();
        }
    }

    private static void autoMode(int counter, String nextSentence) throws IOException, InterruptedException {
        if(counter != 10) {
            //how to start auto mode?
            text = phraseProcessor.splitAndRespond(nextSentence);
            System.out.println(counter);
            System.out.println(text);
            voiceAndText.speak(text);
            nextSentence = text;
            counter++;
            Thread.sleep(3000);
            autoMode(counter, nextSentence);
        }
    }

    private static void interactiveMode() throws IOException{
        while(true) {
            System.out.println("");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String sInput = in.readLine();
            System.out.println("");
            text = phraseProcessor.splitAndRespond(sInput);
            System.out.println(text);
            voiceAndText.speak(text);
            // System.out.println(markovChain.toString());
        }
    }

}