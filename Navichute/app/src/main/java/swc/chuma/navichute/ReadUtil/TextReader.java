package swc.chuma.navichute.ReadUtil;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class TextReader{
    public static TextToSpeech tts;
    static Context context;
    public static TextToSpeech getInstance(Context mcontext){
        if(tts==null||context!=mcontext){
            context=mcontext;
            tts= new TextToSpeech(mcontext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != ERROR) {
                        tts.setLanguage(Locale.KOREAN);
                    }
                }
            });
        }
        return tts;
    }
    public static void readText(String str){
        tts.speak(str,TextToSpeech.QUEUE_FLUSH, null);
    }
    public static void setReadPitch(float number){
        tts.setPitch(number);
    }
    public static void setReadRate(float number){
        tts.setSpeechRate(number);
    }

}