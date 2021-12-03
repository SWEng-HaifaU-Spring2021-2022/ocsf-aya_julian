package il.ac.haifa.cs.sweng.OCSFSimpleChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ChatClientCLI {

    private SimpleChatClient client;
    private boolean isRunning;
    private static final String SHELL_STRING = "Enter message (or exit to quit)> ";
    private Thread loopThread;
    private ArrayList<String> buffer = new ArrayList<>();

    public ChatClientCLI(SimpleChatClient client) {
        this.client = client;
        this.isRunning = false;
    }

    public void loop() throws IOException {
        loopThread = new Thread(new Runnable() {

            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String message;
                String message_2 = "Aya Mahagna, Julian Farraj";
                while (client.isConnected()) {
                    System.out.print(SHELL_STRING);

                    try {
                        message = reader.readLine();


                        if (message.isBlank())
                            continue;

                        StringTokenizer buf = new StringTokenizer(message);
                        while (buf.hasMoreTokens()) {
                            buffer.add(buf.nextToken());
                        }
                        if (buffer.get(0).equals("#sendSubmitters"))
                            client.sendToServer(message_2);
                        else if (buffer.get(0).equals("#send"))
                            client.sendToServer(message.substring(5));
                        else if (buffer.get(0).equals("#exit"))
                            client.closeConnection();

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    buffer = new ArrayList<>();

                }
            }
        });

        loopThread.start();
        this.isRunning = true;

    }

    public void displayMessage(Object message) {
        if (isRunning) {
            System.out.print("(Interrupted)\n");
        }
        System.out.println("Received message from server: " + message.toString());
        if (isRunning)
            System.out.print(SHELL_STRING);
    }

    public void closeConnection() {
        System.out.println("Connection closed.");
        System.exit(0);
    }
}
