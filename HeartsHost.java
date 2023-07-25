//h
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.net.*;
public class HeartsHost{
    public static void main(String[] Args) throws IOException{
        Random r = new Random();
        ServerSocket ss = new ServerSocket(6492);
        String[] suits = {"♣", "♦", "♥", "♠"};
        ArrayList<Card> deck = new ArrayList<Card>();
        for(int i = 2; i < 15; i++){
            for(String b: suits){
                deck.add(new Card(i,b));
            }
        }
        Player[] players = new Player[4];
        System.out.println("Hearts\nHave players join at "+InetAddress.getLocalHost().getHostAddress()+" on the Client Side version.\nIf you want to join too, open a Client Side version in a new window and enter \"localhost\".\nMake sure everybody is on the same wifi.");
        for(int i = 0; i < 4; i++){
            System.out.println(i+" player(s) have joined.");
            players[i] = new Player(ss.accept());
        }
        System.out.println("Everyone has joined.");
        ArrayList<String> usrns = new ArrayList<String>();
        for(Player i: players){
            String usernamee = i.read();
            if(usrns.contains(usernamee)){
                System.out.println("Duplicate username detected");
                System.exit(0);
            }
            else{
                i.assignusername(usernamee);
                usrns.add(usernamee);
            }
        }
        System.out.println("Current players:");
        for(Player i: players){
            System.out.println(i.usrn());
        }
        for(Player i: players){
            for(Player ii: players){
                i.send(ii.usrn());
            }
        }
        boolean gamecontinue = true;
        while(gamecontinue){
            ArrayList<Card> shuffledeck = new ArrayList<Card>();
            shuffledeck.addAll(deck);
            for(int i = 0; i < 4; i++){
                for(int ii = 0; ii < 13; ii++){
                    int cardtoremove = r.nextInt(shuffledeck.size());
                    players[i].sendcard(shuffledeck.get(cardtoremove));
                    shuffledeck.remove(cardtoremove);
                }
            }
            gamecontinue = false;
        }
    }
}