import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Created by scottrobertson on 10/23/17.
 */
public class VoiceAndText {

    private final String VOICENAME_kevin = "kevin";
    private final String VOICENAME_kevin16 = "kevin16";
    private final String VOICENAME_alan = "alan";
    private final Voice voice;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String key;
    private String value;

    public VoiceAndText(String option){
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin16);
        setVoice(option);

    }

    public VoiceAndText(String key, String value){
        this.key = key;
        this.value = value;
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin16);

    }
    public void speak(String text) {
        voice.allocate();
        String[] phrases = text.split(",");
        for(String phrase : phrases) {
            inflect(voice, phrase);
        }
    }

    private void setVoice(String option){
        if(option.equals("male")) {
            voice.setPitch(70);//need to test
            voice.setPitchRange(10);//need to test
            voice.setDurationStretch(1.25f);
        }
    }

    private void inflect(Voice current, String phrase){
        voice.speak(phrase);
            if (current.getPitch() == 70) {
                current.setPitch(90);
            } else if (current.getPitch() > 70) {
                current.setPitch(50);
            } else {
                current.setPitch(70);
            }
    }

}
