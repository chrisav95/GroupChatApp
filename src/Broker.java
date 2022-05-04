import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.net.*;
import static java.lang.Integer.parseInt;

public class Broker implements Node{

    private static List<String> existingGroups = new ArrayList<String>(); //All existing Groups in the App

    HashMap<String, Topic> myTopics = new HashMap<String, Topic>(); //Topics managed by this Broker
    private List<Consumer> registeredUsers = new ArrayList<>(); //List of the Consumers that have established a connection with this Broker.
    private List<Publisher> registeredPublisher = new ArrayList<>(); //List of the Publishers that have established a connection with this Broker.

    private ServerSocket providerSocket; //Broker's server socket, this accepts Consumer queries.
    private int brokerId;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        Broker b = new Broker();
        int port;

        // Reading initBroker.txt
        BufferedReader reader = new BufferedReader(new FileReader("./src/initBroker.txt")); //Reading init file to initialize this broker's port correctly.
        String line;
        line = reader.readLine();
        int brokerNumber = parseInt(line);
        b.setBrokerId(brokerNumber); //Setting broker's id, based on the initBroker.txt file.
        reader.close();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./src/initBroker.txt")));
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
        writer.close();

        b.init(port);

        for (String i : b.myTopics.keySet()) {
            System.out.println(i);
        }

    }

    // Initialize broker.
    public void init(int port) throws UnknownHostException, IOException, NoSuchAlgorithmException {

        brokers.add(this);

        for (String topic : topics){
            // Calculate topic's hash and `mod` it with the number of spawned brokers.
            int hash = parseInt(calculateKeys(topic), 16) % 3;

            // If topic belongs to the broker, register it to the topics list.
            if (brokerId == hash){
                myTopics.put("topic", new Topic(topic));

            }

        }


        // Start listening for connections.
        providerSocket = new ServerSocket(port);
        System.out.println("[BROKER] "+ getBrokerId() + " Initializing data.");

        while(true) {       //Accepting Consumer queries.
            System.out.println("[BROKER] Waiting for userNode connection.");
            Socket client = providerSocket.accept();

            System.out.println("[BROKER] Connected to a consumer!");

           //Συμφωνα με το LAB2 ό,τι κανει μετα ο server είναι σε ενα thread που παίρνει όρισμα το socket
            Thread t = new ActionsForUserNode(client);
            t.start();

        }

    }


    private class ActionsForUserNode extends Thread {

        //Sockets for consumer and publisher, I/O streams, reader/writers.
        private Socket connection;
        private Socket requestSocket = null;
        private PrintWriter printOut;
        private BufferedReader out;
        private InputStreamReader in;
        private BufferedReader publisherReader;
        private ObjectInputStream inP;
        private ObjectOutputStream outC;
        private PrintWriter publisherWriter;

        public ActionsForUserNode(Socket socket) {
            this.connection = socket;
        }
/*
        public void run() {
            try {


                }


                while(true) { //This is where the Broker pulls from the Publisher and pushes to the Consumer the mp3 that's been asked from the query.

                }



            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    //Closing sockets, I/O streams, writers/readers.
                    printOut.close();
                    in.close();
                    out.close();
                    connection.close();
                    if(requestSocket != null) {
                        requestSocket.close();
                        publisherReader.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

 */
        }





    public List<Broker> getBrokers() {
        return brokers;
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


    // Calculate topic's hash and `mod` it with the number of spawned brokers.
    public String calculateKeys(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }



}
