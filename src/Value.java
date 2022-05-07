import java.io.Serializable;

/* Value objects are used to send the chunks of a certain song from Publisher -> Broker -> Consumer.
 * Serializable makes the objects of this class able to be converted into streams that are going to be
 * sent/received from the ObjectInput/OutputStreams.
 */

public class Value implements Serializable{
    private static final long serialVersionUID = 1L;
    private MultimediaFile multimediaFile;

    //Class constructor.

    public Value(MultimediaFile musicFile) {
        this.multimediaFile = musicFile;
    }

    //Setters and getters of this class.

    public void setMusicFile(MultimediaFile musicFile){
        this.multimediaFile = musicFile;
    }

    public MultimediaFile getMusicFile(){
        return this.multimediaFile;
    }

    //Print returns the MusicFile's information (mainly used for debugging).

    public String print() {
        return multimediaFile.getMultimediaFileName() + ": " + multimediaFile.getProfileName() ;
    }
}