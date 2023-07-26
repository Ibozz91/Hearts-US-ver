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
        int playernumber=-1;
        for(int i = 0; i < 4; i++){
            String name = (String)ois.readObject();
            playernames[i] = name;
            if(name.equals(yourusername)){
                playernumber=i;
            }
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
            if(mod4==3){
                System.out.println("No cards are being passed this round.");
            }
            else{
                int amountofcardspassed = 0;
                int[] cardstopass = new int[3];
                while(amountofcardspassed<3){
                    System.out.println("Select a card to pass");
                    int cardtopass = Scan.nextInt();
                    if(!cardstopass.contains(cardtopass) && cardtopass>0 && cardtopass<14){

                    }
                }
            }
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
}