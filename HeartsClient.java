import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class HeartsClient{
    public static void main(String Args[]) throws IOException, UnknownHostException, UnsupportedOperationException{
        Scanner Scan = new Scanner(System.in);
        System.out.println("Hearts\nPlease enter the IP address of the game you want to join.");
        Socket s = new Socket(Scan.nextLine(), 6492);
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream ps = new PrintStream(s.getOutputStream());
        int playercount=Integer.parseInt(br.readLine());
        System.out.println("There are "+playercount+" players. Select a username for yourself.");
        String yourusername=Scan.nextLine();
        ps.println(yourusername);
        ps.flush();
        String[] playernames = new String[playercount];
        for(int i = 0; i < playercount; i++){
            playernames[i]=br.readLine();
        }
        System.out.println("All player usernames:");
        for(String i: playernames){
            System.out.println(i);
        }
    }
}