import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Player{
    private Socket sock;
    private String username;
    private BufferedReader br;
    private PrintStream ps;
    private int points;
    public Player(Socket socketaccepted) throws IOException{
        sock = socketaccepted;
        br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        ps = new PrintStream(sock.getOutputStream());
        hand = new ArrayList<Card>();
        points = 0;
    }
    public void assignusername(String usn){
        username = usn;
    }
    public String usrn(){
        return username;
    }
    public void send(String whattosend){
        ps.println(whattosend);
        ps.flush();
    }
    public String read() throws IOException{
        return br.readLine();
    }
    public void addpoints(int amount){
        points+=amount;
    }
    public int points(){
        return points;
    }
}