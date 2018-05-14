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
 * Defines a single demo client
 */
public class Client {
    Socket client;
    BufferedReader fromServer;
    PrintStream toServer;
    BufferedReader userInput;
    String serverAddress;

    /**
     * Constructor for demo Client
     * @param host The hostname of the Server to connect to
     */
    public Client(String host){
        serverAddress = host;
    }//end constructor
    
    /**
     * Runs the demo client
     */
    public void go(){
        
        //establish connection and data streams
        try{
            client = new Socket(serverAddress, 9090);
            fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            toServer = new PrintStream(client.getOutputStream());
            userInput = new BufferedReader(new InputStreamReader(System.in));            
        }catch(IOException e){
            System.out.println("Problem connecting");           
        }//end try
        
        //while all connections are established
        while (client != null && toServer != null && fromServer != null){
            try{
                String input;
                String response;
                String outputData = "";
                
                //display menu
                System.out.println("Please select a number from the following options.\n1. Current date.\n2. System uptime.\n3. Memory usage.\n4. Netstat\n5. Current Users\n6. Running Processes\n7. Quit\n");
                
                input = userInput.readLine(); //Gets first user input
                
                //while input is valid
                while (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5") || input.equals("6")){
                    toServer.println(input); //send request to server
                    
                    //while server is responding
                    while((response = fromServer.readLine()) != null){
                        outputData += response + "\n"; 
                         
                        //break loop if server is done responding
                        if(response.length() == 0){ 
                             break;
                         }//end if
                    }//end while   
                    System.out.print(outputData); //display results to user
                    outputData = ""; //clears data for next request
                    input = userInput.readLine(); //Gets next user input
                }//end while
                
                //if user wants to exit client
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
                System.out.println("Problem listening to server");
            }//end try
        }//end if
    }//end go()
}//end class
