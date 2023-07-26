//h
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.net.*;
public class HeartsHost{
    public static void main(String[] Args) throws IOException, ClassNotFoundException{
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
            String usernamee = i.readString();
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
                i.sendString(ii.usrn());
            }
        }
        boolean gamecontinue = true;
        int mod4 = 0;
        while(gamecontinue){
            for(int i = 0; i < 4; i++){
                for(int ii = 0; ii < 4; ii++){
                    players[i].sendInt(players[ii].points());
                }
            }
            ArrayList<Card> shuffledeck = new ArrayList<Card>();
            shuffledeck.addAll(deck);
            for(int i = 0; i < 4; i++){
                for(int ii = 0; ii < 13; ii++){
                    int cardtoremove = r.nextInt(shuffledeck.size());
                    players[i].sendCard(shuffledeck.get(cardtoremove));
                    shuffledeck.remove(cardtoremove);
                }
            }
            if(mod4!=3){
                for(int i = 0; i < 4; i++){
                    players[passto(i, mod4)].sendCardArrayList(players[i].readCardArrayList());
                }
            }
            gamecontinue = false;
            mod4=mod4next(mod4);
        }
        while(true){

        }
    }
    public static int passto(int player, int cycle){
        if(cycle==0){
            if(player<3){
                return player+1;
            }
            else{
                return 0;
            }
        }
        else if(cycle==1){
            if(player>0){
                return player-1;
            }
            else{
                return 3;
            }
        }
        else if(cycle==2){
            int j = 4-player;
            if(j%2==0){
                j-=2;
            }
            return j;
        }
        else{
            return -1;
        }
    }
    public static int mod4next(int initial){
        if(initial==3){
            return 0;
        }
        else{
            return initial+1;
        }
    }
    public static int mod4previous(int initial){
        if(initial==0){
            return 3;
        }
        else{
            return initial-1;
        }
    }
}