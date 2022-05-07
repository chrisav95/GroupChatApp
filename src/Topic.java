import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.net.*;



// Class for every Topic
// Keeps subscribers and history of every topic


public class Topic implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<String> subscribers = new ArrayList<String>();
    private List<String> history = new ArrayList<String>();

    private String topicName;

    //Class constructor.

    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<String> getHistory() {
        return history;
    }

    //addSubscriber to xrhsimopoiei o Broker gia na prosthetei kainourious subs

}