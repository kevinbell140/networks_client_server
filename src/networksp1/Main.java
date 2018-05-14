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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Main Class for Client
 */
public class Main {
   
    /**
     * Launches a single demo client or an increasing number of concurrent client threads used to test server response time
     * @param args The hostname of the server
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        //Test for valid hostname
        if(args.length == 0){
            System.out.println("A hostname must be entered");
            System.exit(1);
        }//end if
        
        int option = -1;
               
        //User chooses which client to launch
        while(option == -1 || option>2){
            System.out.println("Welcome to project 1, what would you like to do?\n0. Demo\n1. Light Load\n2. Heavy Load");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String input = userInput.readLine();
            try{
               option = Integer.parseInt(input); 
            }catch(Exception e){
                System.out.println("Please enter a valid option");
            }//end try
        }//end while
        
        //Launches single demo client
        if(option == 0){
            Client demo = new Client(args[0]);
            demo.go();
            
        //Launches concurrent client threads and records average turn around times for each set    
        }else{
        
            //array to store mean turnaround time for each set of clients threads
            long[] avgTimes = new long[12]; 

            //array for the number of client threads to be spawned 
            int[] numClients = {1, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

            //for each set of clients, instatiate arrays to hold threads and turnaround times
            for (int j =0; j<numClients.length; j++ ){  
                int users = numClients[j];
                ArrayList<clientThread> threadArray = new ArrayList<>();  
                ArrayList<Long> Results = new ArrayList<>();
                
                long totalTime = 0; //aggregates all turn around times for a specific number of clients
                
                clientThread ct; //creates a refernce to client threads
                
                //Spawns a specific number of client threads and adds them to a container array
                for (int i = 0; i<users; i++){
                    ct = new clientThread(args[0], option);
                    threadArray.add(ct);
                }//end for

                //Starts threads
                for (int m = 0; m<threadArray.size(); m++){
                    threadArray.get(m).start();
                }//end for
                
                //Joins a specific number of client threads, and records each's turnaround time in an array
                for (int k = 0; k<users; k++){
                    try{
                        threadArray.get(k).join();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }//end try                    
                    Results.add(threadArray.get(k).getRuntime());
                }//end for

                //Aggregates all of the turnaround times recorded for a specific number of client threads
                for (int l = 0; l < Results.size(); l++){
                    totalTime += Results.get(l);
                }//end for
                
                //Computes the mean turnaround time for a specific number of clients and stores the results in an array
                avgTimes[j] = totalTime/numClients[j];
            }//end for
            
            //After each set of clients are tested, display the results
            for(int i =0; i<12; i++){
                System.out.println("Avg turnaround time for " + numClients[i] + " client(s) is " + avgTimes[i]);
            }//end for
        }//end if
    }//end main()
}//end class
