/*
 * Computer Networks and Distributed Processing
 * Tyrone Arthurs, Kevin Bell, Andrew Greer, Campbell Metz, Thorn Prescott, Tim Tuite
 * Program will create a server with a menu system
 * Light and Heavy loads and calculates the response time
 * Program also uses Linux commands to find the Date, Uptime, Memory, Netstat, Current Users, and Uptime
 * We will be using servers 111 and 112
 * Client will be on one of the IP addresses and the server will be on another.
 * Server must be launched first, then the client using java -jar P1Server.jar for the server 
 * Client must be launched next on the other IP address using java -jar NetworksP1.jar 198.168.100.111 to connect to the ip address
 * We will use SSH and Linux on a Cisco VPN. Virtual Private Network
 * A virtual private network (VPN) is a network that is constructed using public wires — usually the Internet — to connect to a private network
 */

package networksp1;
import java.io.*;
import java.net.Socket;

/**
 * Defines a thread of the client
 */
public class clientThread extends Thread {
    
    Socket client;
    BufferedReader fromServer;
    PrintStream toServer;
    BufferedReader userInput;
    
    String hostName;
    int load;

    long startTime;
    long runTime;
    String[] inputCommands;
    
    /**
     * Constructor for client thread
     * @param host The host name of the server
     * @param load The commands to be executed
     */
    clientThread(String host, int load){
        this.hostName = host; 
        this.load = load;
    }//end constructor
    
    /**
     * Runs the client thread
     */
    @Override
    public void run(){
           
        //Determine which commands to execute
        if (load == 1){
         inputCommands = new String[] {"1", "7"};  
        }else{
         inputCommands = new String[] {"4", "7"};
        }//end if
        
        //establish connection and data streams
        try{
            client = new Socket(hostName, 9090);
            fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            toServer = new PrintStream(client.getOutputStream());
            userInput = new BufferedReader(new InputStreamReader(System.in));          
        }catch(IOException e){
            System.out.println("Problem connecting");           
        }//end try
        
        //while all connections are established
        while (client != null && toServer != null && fromServer != null){
            try{
                int i = 0;
                String input = inputCommands[i];
                String response;
                String data = "";
                
                //display menu
                System.out.println("Please select a number from the following options.\n1. Current date.\n2. System uptime.\n3. Memory usage.\n4. Netstat\n5. Current Users\n6. Running Processes\n7. Quit\n");
                                                
                //while input is valid
                while (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5") || input.equals("6")){
                    startTime = System.currentTimeMillis(); //record the start time
                    toServer.println(input); //send request to server

                    //while server is responding
                    while((response = fromServer.readLine()) != null){
                        data +=response + "\n"; 
                           
                        //break loop if server id done responding
                        if(response.length() == 0){
                             break;
                         }//end if
                    }//end while   
                    
                    runTime = System.currentTimeMillis() - startTime; // record the runtime
                    System.out.print(data); //display the results to the user
                    data = ""; //clears data for next request
                    input = inputCommands[++i]; //Gets next user input
                }//end while
                
                //If user wants to exit the client
                if(input.equals("7")){
                    toServer.println(input);
                    System.out.println("Client Closed");
                    toServer.close();
                    fromServer.close();
                    client.close();
                    break;
                  
                //handles invalid input    
                }else{
                    System.out.println("Invalid Input. Try reading the menu first...");
                }//end if
            }catch(Exception e){
                System.out.println("Problem listening");
            }//end try
        }//end if
    }//end run()
    
    /**
     * Gets the runtime of the client-server interaction
     * @return The runtime
     */
    public long getRuntime(){
        return runTime;
    }//end getRuntime()
}//end class
    
    

