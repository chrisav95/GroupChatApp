import java.io.Serializable;


/* It implements Serializable so that each broker can send messages
 * to the UserNode that is connected to them.
 *
 *
 * For pass an object with a socket, the class and the package needs to be the same,
 *  and then you need to set the same serialVersionUID.
 */


public class SocketMessageContent implements Serializable{
    private static final long serialVersionUID = 1L;

    private String topic;

    public SocketMessageContent(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private int port;
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }



}
