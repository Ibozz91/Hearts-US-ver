import java.io.*;
import java.net.*;
public class Player{
    private Socket sock;
    private String username;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int points;
    public Player(Socket socketaccepted) throws IOException{
        sock = socketaccepted;
        oos = new ObjectOutputStream(sock.getOutputStream());
        ois = new ObjectInputStream(sock.getInputStream());
        points = 0;
    }
    public void assignusername(String usn){
        username = usn;
    }
    public String usrn(){
        return username;
    }
    public void sendString(String whattosend) throws IOException{
        oos.writeObject(whattosend);
        oos.flush();
    }
    public void sendCard(Card whatosend) throws IOException{
        oos.writeObject(whatosend);
        oos.flush();
    }
    public String readString() throws IOException, ClassNotFoundException{
        return (String)ois.readObject();
    }
    public Card readCard() throws IOException, ClassNotFoundException{
        return (Card)ois.readObject();
    }
    public void addpoints(int amount){
        points+=amount;
    }
    public int points(){
        return points;
    }
}