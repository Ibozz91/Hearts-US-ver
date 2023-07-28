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
            ArrayList<Integer> scores = new ArrayList<Integer>();
            for(int i = 0; i < 4; i++){
                scores.add((Integer)ois.readObject());
                System.out.println(playernames[i]+": "+scores.get(i));
                if(scores.get(i)>=100){
                    gamecontinue = false;
                }
            }
            if(!gamecontinue){
                int thewon=scores.indexOf(Collections.min(scores));
                System.out.println(playernames[thewon]+"won!");
                continue;
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
                ArrayList<Integer> cardstopass = new ArrayList<Integer>();
                while(amountofcardspassed<3){
                    System.out.println("Select a card to pass to "+playernames[passto(playernumber,mod4)]);
                    int cardtopass = Scan.nextInt();
                    Scan.nextLine();
                    if(!cardstopass.contains(cardtopass) && cardtopass>0 && cardtopass<14){
                        cardstopass.add(cardtopass);
                        amountofcardspassed++;
                    }
                }
                ArrayList<Card> theactualcardstopass = new ArrayList<Card>();
                for(int k = 0; k < 3; k++){
                    theactualcardstopass.add(hand.get(cardstopass.get(k)-1));
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
            for(int i = 0; i < 4; i++){
                penaltycards.add(new ArrayList<Card>());
            }
            for(int i = 0; i < 13; i++){
                System.out.println("Penalty cards:");
                for(int iii = 0; iii < 4; iii++){
                    System.out.print(playernames[iii]+": ");
                    for(int iv = 0; iv < penaltycards.get(iii).size(); iv++){
                        System.out.print(penaltycards.get(iii).get(iv)+" ");
                    }
                    System.out.println();
                }
                ArrayList<Card> Cardsplayed = new ArrayList<Card>();
                int increment = 0;
                for(int ii = playertoplay; increment < 4; ii = mod4next(ii)){
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
                                    if(selection > 0 && selection < 14 && hand.get(selection-1).gs().equals("♣") && hand.get(selection-1).gv()==2){
                                        cardtoplay=hand.get(selection-1);
                                        b=false;
                                    }
                                    else{
                                        System.out.println("That was not the 2 of clubs.");
                                    }
                                }
                            }
                            else{
                                if(pointsplayed || hasAllPointed(hand)){
                                    System.out.println("You may play any card you want.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Please select something inbounds.");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("Select a card that does not give any penalty.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14 && hand.get(selection-1).penalty()==0){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Try again.");
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            if(!firstround || hasAllPointed(hand)){
                                if(hasSuit(hand, Cardsplayed.get(0).gs())){
                                    System.out.println("Select a card with the "+Cardsplayed.get(0).gs()+" suit.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14 && hand.get(selection-1).gs().equals(Cardsplayed.get(0).gs())){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Wrong suit or out-of-bounds.");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("You may play any card you want.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Please select something inbounds.");
                                        }
                                    }
                                }
                            }
                            else{
                                if(hasSuit(hand, Cardsplayed.get(0).gs())){
                                    System.out.println("Select a card with the "+Cardsplayed.get(0).gs()+" suit and has no penalty.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14 && hand.get(selection-1).gs().equals(Cardsplayed.get(0).gs()) && hand.get(selection-1).penalty()==0){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Wrong suit, has penalty or out-of-bounds.");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("You may play any card you want so long as there is no penalty.");
                                    boolean b = true;
                                    while(b){
                                        int selection = Scan.nextInt();
                                        Scan.nextLine();
                                        if(selection > 0 && selection < 14 && hand.get(selection-1).penalty()==0){
                                            cardtoplay=hand.get(selection-1);
                                            b = false;
                                        }
                                        else{
                                            System.out.println("Nope. Retry.");
                                        }
                                    }
                                }
                            }
                        }
                        Cardsplayed.add(cardtoplay);
                            hand.remove(cardtoplay);
                        oos.writeObject(cardtoplay);
                        oos.flush();
                        if(cardtoplay.penalty()>0){
                            pointsplayed = true;
                        }
                    }
                    else{
                        Card cardputdown = (Card)ois.readObject();
                        Cardsplayed.add(cardputdown);
                        System.out.println(playernames[ii]+" plays "+cardputdown.toString());
                        if(cardputdown.penalty()>0){
                            pointsplayed = true;
                        }
                    }
                    increment++;
                }
                firstround = false;
                playertoplay = (playertoplay+winnerof4(Cardsplayed))%4;
                System.out.println(playernames[playertoplay]+" gets the cards for this.");
                for(int v = 0; v < 4; v++){
                    if(Cardsplayed.get(v).penalty()>0){
                        penaltycards.get(playertoplay).add(Cardsplayed.get(v));
                    }
                }
            }
            mod4=mod4next(mod4);
        }
        s.close();
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
    public static boolean hasAllPointed(ArrayList<Card> cards){
        boolean b = true;
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).penalty()==0){
                b = false;
            }
        }
        return b;
    }
    public static boolean hasSuit(ArrayList<Card> cards, String suit){
        boolean b = false;
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).gs().equals(suit)){
                b = true;
            }
        }
        return b;
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