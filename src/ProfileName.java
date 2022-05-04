import java.util.*;

public class ProfileName {
    private String profileName;
    private HashMap<String, ArrayList<Value>> userVideoFilesMap;
    private HashMap<String, Integer> subscribedGroups;

    //Class constructor.

    public ProfileName(String profileName) {
        this.profileName = profileName;
    }

    //Setters and getters of this class.

    public void setProfileName(String profileName){
        this.profileName = profileName;
    }

    public String getProfileName(){
        return this.profileName;
    }

}
