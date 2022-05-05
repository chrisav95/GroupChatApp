import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/* It implements Serializable so that each broker can send messages
       * to the UserNode that is connected to them.
       *
       *
       * For pass an object with a socket, the class and the package needs to be the same,
       *  and then you need to set the same serialVersionUID.
*/


public class SocketMessage implements Serializable{
    private static final long serialVersionUID = 1L;

    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


    private SocketMessageContent content;
    public SocketMessageContent getContent() {
        return content;
    }
    public void setContent(SocketMessageContent content) {
        this.content = content;
    }


    public SocketMessage(String type, SocketMessageContent content) {
        this.type=type;
        this.content = content;
    }


    /*
    enum type {
        USER_TOPIC_LOOKUP,
        USER_TOPIC_LOOKUP_ERROR,
        USER_TOPIC_LOOKUP_REDIRECT,
        USER_TOPIC_LOOKUP_SUCCESS
    }*/





}
