import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.net.*;
public class HeartsHost {
    public static void main(String[] Args){
        ServerSocket ss = new ServerSocket(6492);
        String[] suits = {"♣", "♦", "♥", "♠"};
        ArrayList<String> deck = new ArrayList<String>();
        for(int i = 2; i < 15; i++){
            for(String b: suits){
                deck.add(new Card(i,b));
            }
        }
        System.out.println("Have players join at "+InetAddress.getLocalHost().getHostAddress()+" on the Client Side version.\nIf you want to join too, open a Client Side version in a new window and enter \"localhost\".\nMake sure everybody is on the same wifi.");
    }
}