import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;

public class HeartsClient{
    public static void main(String Args[]) throws IOException, UnknownHostException, UnsupportedOperationException, ClassNotFoundException{
        Scanner Scan = new Scanner(System.in);
        System.out.println("Hearts\nPlease enter the IP address of the game you want to join.");
        Socket s = new Socket(Scan.nextLine(), 6492);
        System.out.println("A");
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        System.out.println("sf");
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        System.out.println("Select a username for yourself.");
        String yourusername=Scan.nextLine();
        oos.writeObject(yourusername);
        oos.flush();
        String[] playernames = new String[4];
        for(int i = 0; i < 4; i++){
            playernames[i] = (String)ois.readObject();
        }
        System.out.println("All player usernames:");
        for(String i: playernames){
            System.out.println(i);
        }
        boolean gamecontinue = true;
        while(gamecontinue){
            ArrayList<Card> hand = new ArrayList<Card>();
            for(int i = 0; i < 13; i++){
                hand.add((Card)ois.readObject());
            }
            gamecontinue = false;
        }
    }
}