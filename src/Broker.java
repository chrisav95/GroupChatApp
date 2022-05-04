import java.util.*;
import java.io.*;
import java.net.*;
import static java.util.Objects.hash;

public class Broker implements Node{

    private static List<String> existingGroups = new ArrayList<String>();

    private List<String> managedGroups = new ArrayList<String>();
    //private List<UserNode> registeredUsers = new ArrayList<>(); //List of registered Users.
    private int brokerId;


    public static void main(String[] args) throws IOException{

        Broker b = new Broker();
        int port;

        //Reading the init file to initialize this broker's port correctly.
        BufferedReader reader = new BufferedReader(new FileReader("./brokerInfo/init.txt"));
        String line;
        line = reader.readLine();
        int brokerNumber = Integer.parseInt(line);
        b.setBrokerId(brokerNumber); //Setting broker's id, based on the init.txt file.

        //Reading the init file and initializing a list of the existing Groups.
        line = reader.readLine();
        while(line!=null){
            if (line.length()>0){
                existingGroups.add(line);
            }
        }

        reader.close();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./brokerInfo/init.txt")));

        if (brokerNumber == 0) { //This means that this is the first broker to be initialized.
            port = FIRSTBROKER;
            brokerNumber++; //Increasing the counter of running brokers.
            System.out.println("Broker Number " + brokerNumber + " with port " + port );
            writer.write(String.valueOf(brokerNumber)); //Refreshing init file.
        } else if (brokerNumber == 1) {
            port = SECONDBROKER;
            brokerNumber++;
            System.out.println("Broker Number " + brokerNumber + " with port " + port );
            writer.write(String.valueOf(brokerNumber));
        } else {
            port = THIRDBROKER;
            System.out.println("Broker Number 3 with port " + port );
            brokerNumber = 0; //When we initialize the third broker we reset the counter to 0.
            writer.write(String.valueOf(brokerNumber));
        }

        /*
        //Setting Info ip, port and brokerId.
        b.getBrokerInfo().setIp(ip);
        b.getBrokerInfo().setPort(String.valueOf(port));
        b.getBrokerInfo().setBrokerId(b.getBrokerId());


        writer.close();

        b.calculateKeys(); //Check method.
        */

        b.init(port);

    }

    public void init(int port) throws UnknownHostException, IOException {

    }

    public List<Broker> getBrokers() {
        return null;
    }

    public void connect() {

    }

    public void disconnect() {

    }

    public void updateNodes() {

    }

    public void setBrokerId(int brokerId) {
        this.brokerId = brokerId;
    }

    public int getBrokerId() {
        return brokerId;
    }
}
