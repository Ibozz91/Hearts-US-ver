import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
public class HeartsClient{
    public static void main(String Args[]) throws IOException, UnknownHostException, UnsupportedOperationException, ClassNotFoundException{
        Scanner Scan = new Scanner(System.in);
        System.out.println("Hearts\nIf running in command line please run chcp 65001 first\nPlease enter the IP address of the game you want to join.");
        Socket s = new Socket(Scan.nextLine(), 6492);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
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
        int mod4 = 0;
        while(gamecontinue){
            System.out.println("Current scores:");
            for(int i = 0; i < 4; i++){
                System.out.println(playernames[i]+": "+(Integer)ois.readObject());
            }
            ArrayList<Card> hand = new ArrayList<Card>();
            for(int i = 0; i < 13; i++){
                hand.add((Card)ois.readObject());
            }
            Collections.sort(hand);
            printHand(hand);
            gamecontinue = false;
            if(mod4==3){
                mod4=0;
            }
            else{
                mod4++;
            }
        }
    }
    public static void printHand(ArrayList<Card> Hand){
        for(int i = 0; i < Hand.size(); i++){
            System.out.print((i<10?"0"+(i+1):i+1)+" ");
        }
        System.out.println();
        for(int i = 0; i < Hand.size(); i++){
            System.out.print(Hand.get(i).toString()+" ");
        }
    }
}