import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class UserNode{

    int userId;

    public static void main(String[] args) throws IOException{
        UserNode u = new UserNode();
        Consumer consumer = new Consumer();

        //Reading init file to initialize this User.
        BufferedReader reader = new BufferedReader(new FileReader("./brokerInfo/init.txt"));
        String line;
        line = reader.readLine();
        int userNumber = Integer.parseInt(line);
        u.setUserId(userNumber); //Setting broker's id, based on the init.txt file.

        line = reader.readLine();
        while(line!=null){
            if (line.length()>0){
                existingUsers.add(line);
            }
        }
        /*
        System.out.println("Enter profile name: ");

        ProfileName profileName = new ProfileName(consumer.getKeyboard().readLine());

        consumer.setProfileName(profileName);

        consumer.connect(); //Connecting to a random Broker.

        consumer.init(consumer.getSocket().getPort());

        Publisher publisher = new Publisher(profileName);

        Group group = new Group("Gossip");

        // Infinite loop to read and send messages.
        consumer.listenForMessage();
        publisher.sendMessage();

        System.out.println("Bye!");
        */
    }


}
