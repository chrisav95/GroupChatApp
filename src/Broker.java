import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.net.*;
import static java.lang.Integer.parseInt;
import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broker implements Node{

    HashMap<String, Topic> myTopics = new HashMap<String, Topic>(); //Topics managed by this Broker

    private ExecutorService pool = Executors.newFixedThreadPool(100); //Broker thread pool.
    private ServerSocket providerSocket; //Broker's server socket, this accepts Consumer queries.
    private int brokerId;
    private int port;

    private Socket connection;
    private Socket requestSocket = null;
    private PrintWriter printOut;
    private BufferedReader out;
    private InputStreamReader in;
    private BufferedReader publisherReader;
    private ObjectInputStream inP;
    private ObjectOutputStream outC;
    private PrintWriter publisherWriter;
    private String connectionType;

    public Broker(){}

    public Broker(int id){
        this.brokerId = id;
        if (id == 0) this.port = FIRSTBROKER;
        if (id == 1) this.port = SECONDBROKER;
        if (id == 2) this.port = THIRDBROKER;

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

        Broker b;
        initBrokers();

        // Reading initBroker.txt
        BufferedReader reader = new BufferedReader(new FileReader("./src/initBroker.txt")); //Reading init file to initialize this broker's port correctly.
        String line;
        line = reader.readLine();
        int brokerNumber = parseInt(line);

        reader.close();

        //Updating the txt file with the current broker
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./src/initBroker.txt")));
        if (brokerNumber == 0) { //This means that this is the first broker to be initialized.
            b = brokers.get(0);
            brokerNumber++;
            System.out.println("Broker Number " + brokerNumber + " with port " + b.port );
            writer.write(String.valueOf(brokerNumber)); //Refreshing init file.
        } else if (brokerNumber == 1) {
            b = brokers.get(1);
            brokerNumber++;
            System.out.println("Broker Number " + brokerNumber + " with port " + b.port );
            writer.write(String.valueOf(brokerNumber));
        } else {
            b = brokers.get(2);
            System.out.println("Broker Number 3 with port " + b.port );
            brokerNumber = 0; //When we initialize the third broker we reset the counter to 0.
            writer.write(String.valueOf(brokerNumber));
        }
        writer.close();

        b.init(b.port);

    }

    // Initialize broker.
    public int init(int port) throws UnknownHostException, IOException, NoSuchAlgorithmException, ClassNotFoundException {

        for (String topic : topics)
        {
            // Calculate topic's hash and `mod` it with the number of spawned brokers.
            BigInteger key = new BigInteger(calculateKeys(topic) , 16);
            int hash = (key.mod(BigInteger.valueOf(3))).intValue();
            //System.out.println(hash); // As decimal...
            //System.out.println(brokerId);

            // If topic belongs to the broker, register it to the topics list.
            brokers.get(hash).myTopics.put(topic, new Topic(topic));

            //if (brokerId == hash){
            //    myTopics.put(topic, new Topic(topic));   //  !It's OK . Ta topic tou kathe broker mpainoun sto hashmap
            //}
        }

        //System.out.println("Size: " + brokers.size());
        for(Broker b: this.brokers) {
            System.out.println("Broker ID: " + b.getBrokerId() + " \nManaging Topics: ");
            for (String i : b.myTopics.keySet()) {
                System.out.println(i);
            }
            System.out.println("\n");
        }

        // Start listening for connections.
        providerSocket = new ServerSocket(port);
        System.out.println("[BROKER] "+ getBrokerId() + " Initializing data.");

        while(true) {       //Accepting UserNode queries.
            System.out.println("[BROKER] Waiting for userNode connection.");
            Socket user = providerSocket.accept();
            in = new InputStreamReader(user.getInputStream());
            out = new BufferedReader(in);
            printOut = new PrintWriter(user.getOutputStream(), true);
            outC = new ObjectOutputStream(user.getOutputStream());
            System.out.println("[BROKER] Connected to a UserNode!");

            String topic;
            boolean exists = false; //Checks if a topic exists in current broker's list
            //usernode establishes whether he is a publisher or a consumer
            SocketMessage response = (SocketMessage) inP.readObject();
            topic = response.getContent().getTopic(); //reading consumer/publisher first message
            connectionType = response.getType();
            if (connectionType == "PUBLISHER_CONNECTION") {

                topic = "";
                for (String t : topics) {
                    topic = topic + t + ":"; //Sends to publisher the available topic list.
                }
                outC.writeObject(new SocketMessage("TOPIC_LIST", new SocketMessageContent(topic)));
                outC.flush();

                response = (SocketMessage) inP.readObject(); //Publisher's chosen topic
                if (response.getType() == "USER_TOPIC_LOOKUP") {
                    for (Broker b : brokers) {
                        for (String t : myTopics.keySet()) {
                            if (b.brokerId == this.getBrokerId() && response.getContent().getTopic() == t) {
                                exists = true;
                            }
                        }
                    }

                    if (exists) {
                        topic = "topic info"; // wip
                        outC.writeObject(new SocketMessage("USER_TOPIC_LOOKUP_SUCCESS", new SocketMessageContent(topic)));
                        outC.flush();
                    } else {
                        //Sinexizoume edw pou prepei na kanei redirect sto swsto broker!!!!
                        outC.writeObject(new SocketMessage("USER_TOPIC_LOOKUP_REDIRECT", new SocketMessageContent(topic)));
                        outC.flush();
                    }
                    outC.writeObject(new SocketMessage("TOPIC_LIST", new SocketMessageContent(topic)));
                    outC.flush();
                }

            } else if (connectionType == "CONSUMER_CONNECTION") {

            } else {
                System.out.println("Unknown connection.");
            }
            //Συμφωνα με το LAB2 ό,τι κανει μετα ο server είναι σε ενα thread που παίρνει όρισμα το socket
            //Thread t = new ActionsForUserNode(client);
            //t.start();

            ActionsForUserNodes consumerThread = new ActionsForUserNodes(user, connectionType);
            pool.execute(consumerThread);

        }

    }


    private class ActionsForUserNodes extends Thread {

        //Sockets for consumer and publisher, I/O streams, reader/writers.

        public ActionsForUserNodes(Socket socket, String type) {
            connection = socket;
            connectionType = type;
        }


        public void run() {
            try {
                //I/O streams for the consumer
                in = new InputStreamReader(connection.getInputStream());
                out = new BufferedReader(in);
                printOut = new PrintWriter(connection.getOutputStream(), true);
                outC = new ObjectOutputStream(connection.getOutputStream());
                String topic = "";
                outC.writeObject(new SocketMessage("USERNODE_TYPE", new SocketMessageContent(topic)));
                outC.flush();


                while (true) { //This is where the Broker pulls from the Publisher and pushes to the Consumer the mp3 that's been asked from the query.

                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    //Closing sockets, I/O streams, writers/readers.
                    printOut.close();
                    in.close();
                    out.close();
                    connection.close();
                    if (requestSocket != null) {
                        requestSocket.close();
                        publisherReader.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }


        }
    }


    public void addBroker(Broker b){
        brokers.add(b);
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
    public static void initBrokers(){
        for(int i=0;i<3;i++){
            brokers.add(new Broker(i));
        }
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
