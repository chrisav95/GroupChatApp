import java.io.Serializable;

/* MultimediaFile objects are used to store the File's information.
 * The strings multimediaFileName, profileName, dateCreated, length,
 * framerate, frameHeight, frameWidth are extracted from
 * ///////the Id3v1Tag///////ALLAZOUME ANALOGA of the multimedia file that the consumer wants to receive.
 * The byte[] multimediaFileChunk is extracted from the file itself.
 */

public class MultimediaFile implements Serializable{
    private static final long serialVersionUID = 1L;
    private String multimediaFileName;
    private String profileName;
    //private String dateCreated;
    private Long length;
    //private String framerate;
    //private String frameHeight;
    //private String frameWidth;

    private byte[] multimediaFileChunk; //The chunk of data that is going to be sent from the Publisher -> Broker -> Consumer.

    //Class constructor.

    //public MultimediaFile(String multimediaFileName, String profileName, String dateCreated,
                          //String length, String framerate, String frameHeight, String frameWidth, byte[] multimediaFileChunk) {
    public MultimediaFile(String multimediaFileName, String profileName, long length, byte[] multimediaFileChunk){
        if(multimediaFileName != null) {
            this.multimediaFileName = multimediaFileName;
        }else {
            this.multimediaFileName = "";
        }

        if(profileName != null) {
            this.profileName = profileName;
        }else {
            this.profileName = "";
        }

        if(length != 0 ) {
            this.length = length;
        }else {
            this.length = Long.valueOf(0);
        }



/*
        if(dateCreated != null) {
            this.dateCreated = dateCreated;
        }else {
            this.dateCreated = "";
        }



        if(framerate != null) {
            this.framerate = framerate;
        }else {
            this.framerate = "";
        }

        if(frameHeight != null) {
            this.frameHeight = frameHeight;
        }else {
            this.frameHeight = "";
        }

        if(frameWidth != null) {
            this.frameWidth = frameWidth;
        }else {
            this.frameWidth = "";
        }

        this.multimediaFileChunk = multimediaFileChunk;
*/
    }

    //Setters and getters of this class.

    public void setMultimediaFileName(String multimediaFileName){
        this.multimediaFileName = multimediaFileName;
    }

    public void setProfileName(String profileName){
        this.profileName = profileName;
    }

    public void setLength(Long length){
        this.length = length;
    }

    public void setMultimediaFileChunk(byte[] multimediaFileChunk){
        this.multimediaFileChunk = multimediaFileChunk;
    }

    public byte[] getMultimediaFileChunk(){
        return this.multimediaFileChunk;
    }

    public String getProfileName(){
        return this.profileName;
    }
    public Long getLength(){
        return this.length;
    }

    public String getMultimediaFileName(){
        return this.multimediaFileName;
    }




/*
    public void setDateCreated(String dateCreated){
        this.dateCreated = dateCreated;
    }

    public void setFramerate(String framerate){
        this.framerate = framerate;
    }

    public void setFrameHeight(String frameHeight){
        this.frameHeight = frameHeight;
    }

    public void setFrameWidth(String frameWidth){
        this.frameWidth = frameWidth;
    }


    public String getDateCreated(){
        return this.dateCreated;
    }

    public String getFramerate(){
        return this.framerate;
    }

    public String getFrameHeight(){
        return this.frameHeight;
    }

    public String getFrameWidth(){
        return this.frameWidth;
    }
*/


}
