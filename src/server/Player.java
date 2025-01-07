//I moved your Player.java to Client <<<<<<<<<<<<<<<<<<<<<<<<<<<<
import java.net.Socket;

public class Player extends Character {
    private String IP;
    private String name;
    private Socket clientSocket;
    private int Id;
    private int x;
    private int y;
    private int hp;
    private String bulletID;
    private int pointingDirection;
    
    Player(String name, String IP, Socket clientSocket, int Id, int x, int y, String bulletID) {
        super(bulletID); //Load Known-character info from Character class which from characterConst
        this.name = name;
        this.IP = IP;
        this.clientSocket = clientSocket;
        this.Id = Id;
        this.x = x;
        this.y = y;
        this.bulletID = bulletID;
        this.hp = super.getMaxHp();
    }

    public String getIP() {
        return this.IP;
    }

    public int getId() {
        return this.Id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHp() {
        return this.hp;
    }

    public String getBulletID() {
        return this.bulletID;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return this.name;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDirection(int pointingDirection) {
        this.pointingDirection = pointingDirection;
    }

    public int getDirection() {
        return this.pointingDirection;
    }

    public Socket getSocket() {
        return this.clientSocket;
    }
}
