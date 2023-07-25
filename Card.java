//No es confuso hoy en dia
//Compare function for hearts only
import java.io.Serializable;
import java.util.ArrayList;
public class Card implements Serializable, Comparable<Card>{
	private String suit;
	private int value;
	public Card(int val, String sut) {
		suit = sut;
		value = val;
	}
	public String gs() {
		return suit;
	}
	public int gv() {
		return value;
	}
	public String toString() {
        String cardname = Integer.toString(value);
        if(value == 11){
            cardname = "J";
        }
        else if(value == 12){
            cardname = "Q";
        }
        else if(value == 13){
            cardname = "K";
        }
        else if(value == 14){
            cardname = "A";
        }
		return cardname+suit;
	}
    public int penalty(){
        if(suit.equals("♥")){
            return 1;
        }
        if(value==12 && suit.equals("♠")){
            return 13;
        }
        return 0;
    }
    public int compareTo(Card c){
        ArrayList<String> suits = new ArrayList<String>();
        suits.add("♣");
        suits.add("♦");
        suits.add("♥");
        suits.add("♠");
        if(suits.indexOf(suit)<suits.indexOf(c.gs())){
            return -1;
        }
        else if(suits.indexOf(suit)>suits.indexOf(c.gs())){
            return 1;
        }
        else{
            if(value<c.gv()){
                return -1;
            }
            else if(value>c.gv()){
                return 1;
            }
        }
        return 0;
    }
}