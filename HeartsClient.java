import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
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
                    System.out.println("Select a card to pass to "+playernames[passto(playernumber,mod4)]);
                    int cardtopass = Scan.nextInt();
                    Scan.nextLine();
                    if(!Arrays.asList(cardstopass).contains(cardtopass) && cardtopass>0 && cardtopass<14){
                        cardstopass[amountofcardspassed]=cardtopass;
                        amountofcardspassed++;
                    }
                }
                ArrayList<Card> theactualcardstopass = new ArrayList<Card>();
                for(int k = 0; k < 3; k++){
                    theactualcardstopass.add(hand.get(cardstopass[k]-1));
                }
                oos.writeObject(theactualcardstopass);
                oos.flush();
                hand.removeAll(theactualcardstopass);
                ArrayList<Card> theactualcardstorecieve = (ArrayList<Card>)ois.readObject();
                hand.addAll(theactualcardstorecieve);
                Collections.sort(hand);
                System.out.println("Your new hand:");
                printHand(hand);
                System.out.println("Press enter to continue.");
                Scan.nextLine();
            }
            int has2clubs=0;
            if(hand.get(0).gs().equals("♣") && hand.get(0).gv()==2){
                has2clubs=1;
            }
            oos.writeObject(has2clubs);
            oos.flush();
            int playertoplay = (Integer)ois.readObject();
            boolean firstround = true;
            boolean pointsplayed = false;
            ArrayList<ArrayList<Card>> penaltycards = new ArrayList<ArrayList<Card>>(4);
            for(int i = 0; i < 13; i++){
                System.out.println("Penalty cards:");
                for(int iii = 0; iii < 4; iii++){
                    System.out.print(playernames[iii]+": ");
                    for(int iv = 0; iv < penaltycards.get(iii).size(); iv++){
                        System.out.print(penaltycards.get(iii).get(iv));
                    }
                    System.out.println();
                }
                for(int ii = playertoplay; ii <= mod4previous(playertoplay); ii = mod4next(ii)){
                    ArrayList<Card> Cardsplayed = new ArrayList<Card>();
                    if(ii==playernumber){
                        Card cardtoplay = null;
                        printHand(hand);
                        if(playernumber==playertoplay){
                            if(firstround){
                                boolean b = true;
                                while(b){
                                    System.out.println("You must play the 2 of clubs. Select the 2 of clubs");
                                    int selection = Scan.nextInt();
                                    Scan.nextLine();
                                    if(selection > 0 && selection < 13 && hand.get(selection).gs().equals("♣") && hand.get(selection).gv()==2){
                                        cardtoplay=hand.get(selection);
                                        b=false;
                                    }
                                    else{
                                        System.out.println("That was not the 2 of clubs.");
                                    }
                                }
                            }
                            else{
                                if(pointsplayed){
                                    
                                }
                                else{
                                    System.out.println("You may play any card you want.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 13){
                                            cardtoplay=hand.get(selection);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Please select something inbounds.");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else{
                        Card cardputdown = (Card)ois.readObject();
                        Cardsplayed.add(cardputdown);
                        System.out.println(playernames[ii]+" plays "+cardputdown.toString());
                    }
                }
            }
            mod4=mod4next(mod4);
        }
    }
    public static int mod4next(int initial){
        return (initial+1)%4;
    }
    public static int mod4previous(int initial){
        return (initial-1)%4;
    }
    public static void printHand(ArrayList<Card> Hand){
        for(int i = 0; i < Hand.size(); i++){
            System.out.print((i<9?"0"+(i+1):i+1)+" ");
        }
        System.out.println();
        for(int i = 0; i < Hand.size(); i++){
            System.out.print(Hand.get(i).toString()+" ");
        }
        System.out.println();
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
    public static boolean hasallPointed(ArrayList<Card> cards){
        boolean b = true;
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).gv()==0){
                b = false;
            }
        }
        return b;
    }
}