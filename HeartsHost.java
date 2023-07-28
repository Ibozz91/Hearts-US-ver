import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.Collections;
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
            for(int i = 0; i < 4; i++){
                if(players[i].points()>=100){
                    gamecontinue = false;
                }
            }
            if(!gamecontinue){
                continue;
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
            int player2c = -1;
            for(int i = 0; i < 4; i++){
                if(players[i].readInt()==1){
                    player2c=i;
                }
            }
            for(int i = 0; i < 4; i++){
                players[i].sendInt(player2c);
            }
            int playertoplay = player2c;
            ArrayList<ArrayList<Card>> penaltycards = new ArrayList<ArrayList<Card>>(4);
            for(int ii = 0; ii < 4; ii++){
                penaltycards.add(new ArrayList<Card>());
            }
            for(int i = 0; i < 13; i++){
                ArrayList<Card> Cardsplayed = new ArrayList<Card>();
                int increment = 0;
                for(int ii = playertoplay; increment < 4; ii = mod4next(ii)){
                    Card cardplayed = players[ii].readCard();
                    for(int iii = 0; iii < 4; iii++){
                        if(iii!=ii){
                            players[iii].sendCard(cardplayed);
                        }
                    }
                    Cardsplayed.add(cardplayed);
                    increment++;
                }
                playertoplay = (playertoplay+winnerof4(Cardsplayed))%4;
                for(int v = 0; v < 4; v++){
                    if(Cardsplayed.get(v).penalty()>0){
                        penaltycards.get(playertoplay).add(Cardsplayed.get(v));
                    }
                }
            }
            ArrayList<Integer> pointstoadd = new ArrayList<Integer>();
            for(int i = 0; i < 4; i++){
                pointstoadd.add(0);
            }
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < penaltycards.get(i).size(); j++){
                    pointstoadd.set(i, pointstoadd.get(i)+penaltycards.get(i).get(j).penalty());
                }
            }
            if(pointstoadd.contains(26)){
                for(int i = 0; i < 4; i++){
                    if(pointstoadd.get(i)==26){
                        pointstoadd.set(i, 0);
                    }
                    else{
                        pointstoadd.set(i, 26);
                    }
                }
            }
            for(int i = 0; i < 4; i++){
                players[i].addpoints(pointstoadd.get(i));
            }
            mod4=mod4next(mod4);
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
        return (initial+1)%4;
    }
    public static int mod4previous(int initial){
        return (initial-1)%4;
    }
    public static int winnerof4(ArrayList<Card> cards){
        ArrayList<Card> cards2 = new ArrayList<Card>();
        for(int i = 0; i < 4; i++){
            if(cards.get(i).gs().equals(cards.get(0).gs())){
                cards2.add(cards.get(i));
            }
        }
        return cards.indexOf(Collections.max(cards2));
    }
}