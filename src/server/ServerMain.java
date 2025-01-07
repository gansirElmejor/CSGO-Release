import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    //Load consts
    private NetworkConst Const = new NetworkConst();

    ServerSocket sS; //Server socket
    Socket cS; //Client Socket
    ArrayList<Player> onlinePlayers;
    ArrayBlockingQueue<String> bulletQueue;
    ArrayBlockingQueue<String> extraBroadcasts;
    GameHandler game;
    int sessionCount = 0;

    public static void main(String[] args) throws Exception {
        ServerMain server = new ServerMain();
        server.go();
    }

    public void go() throws Exception{
        //initialize game
        int[][] mapId= new GameMap(0, 4, 4, 4).map;
        onlinePlayers = new ArrayList<Player>();
        bulletQueue = new ArrayBlockingQueue<String>(Const.getMaxBulletPerBroadcast());
        extraBroadcasts = new ArrayBlockingQueue<String>(Const.getMaxBulletPerBroadcast());
        game = new GameHandler(mapId, onlinePlayers,bulletQueue,extraBroadcasts);
        Thread broadcastThread = new Thread(new BroadcastHandler(onlinePlayers,bulletQueue,extraBroadcasts),"GameBroadcaster");
        broadcastThread.start();
        System.out.println("Game Initialized. \nStarting server...");
        
        //setup server
        sS = new ServerSocket(Const.getPort());
        System.out.println("Server up listening at port "+ Const.getPort());
        while (true) {
            try{
                cS = sS.accept();
                Thread connectionThread = new Thread(new ConnectionHandler(cS,game,extraBroadcasts),"Client"+sessionCount);
                sessionCount = sessionCount + 1;
                connectionThread.start();
            } catch(Exception err) {
                System.err.println(err);
            }
        }
    }
}

class ConnectionHandler extends Thread {
    //Load consts
    private NetworkConst Const = new NetworkConst();

    Socket cS; //Client Socket
    PrintWriter output;
    BufferedReader input;
    GameHandler game;
    String clientIp;
    ArrayBlockingQueue<String> extraBroadcasts;
    
    ConnectionHandler(Socket cS,GameHandler game,ArrayBlockingQueue<String> extraBroadcasts){
        this.cS = cS;
        this.game = game;
        this.extraBroadcasts = extraBroadcasts;
    }

    @Override
    public void run() {
        try{
            input = new BufferedReader(new InputStreamReader(cS.getInputStream()));
            output = new PrintWriter(cS.getOutputStream());
            InetSocketAddress socketAddress = (InetSocketAddress)cS.getRemoteSocketAddress();
            clientIp = socketAddress.getAddress().getHostAddress();

            while (true) {
                try{
                    String msg = input.readLine(); // message from the client
                    if (msg == "") {continue;} //Skip if no message from client
                    respondAsync(msg); // Respond stuffs
                    output.flush();
                } catch(SocketException err) {
                    System.out.println("Socket is disconnected");
                    break;
                }
            }
            input.close();
            output.close();
            System.out.println("Removing player by ip:"+clientIp);
            game.removePlayerByIP(clientIp);
        } catch(IOException err) {err.printStackTrace();}
    }

    public void respondAsync(String request) {
        String[] firstSplit = request.split(Const.getFstRegex());
        String header = firstSplit[0];
        if (header.equals(Const.getReqNorm())) {
            //200&X Y PointingDirection
            String[] secondSplit = firstSplit[1].split(Const.getSecRegex());
            int newX = Integer.parseInt(secondSplit[0]);
            int newY = Integer.parseInt(secondSplit[1]);
            int direction = Integer.parseInt(secondSplit[2]);
            game.updatePlrPosition(clientIp,newX,newY,direction); 
            //Respond of 200 is going to be based on Broadcast not here
            return;
        } else 
        if (header.equals(Const.getReqLogin())) {
            //201&PlayerName BulletID
            String[] secondSplit = firstSplit[1].split(Const.getSecRegex());
            String playerName = secondSplit[0];
            String bulletID = secondSplit[1];
            this.output.println(game.loginPlr(playerName, bulletID, this.clientIp, this.cS));
            return;
        } else 
        if (header.equals(Const.getReqAttack())) {
            //203&VictimID lostHP
            String[] secondSplit = firstSplit[1].split(Const.getSecRegex());
            int victimID = Integer.parseInt(secondSplit[0]);
            int lostHP = Integer.parseInt(secondSplit[1]);
            Socket victimSocket = game.dmgPlr(victimID,lostHP);
            try{
                PrintWriter victimOutput = new PrintWriter(victimSocket.getOutputStream());
                victimOutput.println(Const.getReqAttack()+Const.getFstRegex()+lostHP);
                //victimOutput.close(); //DO NOT CLOSE BEACUSE: Closing the returned OutputStream will close the associated socket.
            } catch(IOException err) {
                err.printStackTrace();
            }
            return;
        } else
        if (header.equals(Const.getReqRay())) { //Queue the bullet rays and leave it to broadcast
            //202&rayX rayY
            String[] secondSplit = firstSplit[1].split(Const.getSecRegex());
            int rayX = Integer.parseInt(secondSplit[0]);
            int rayY = Integer.parseInt(secondSplit[1]);
            game.queueBullet(clientIp,rayX,rayY);
            return;
        } else
        if (header.equals(Const.getReqBurn())) {
            //204&VictimID
            String victimID = firstSplit[1];
            String res = Const.getReqBurn()+Const.getFstRegex()+victimID;
            this.extraBroadcasts.add(res);
            return;
        } else
        if (header.equals(Const.getReqInvulnerable())) {
            //205&VictimID
            String victimID = firstSplit[1];
            String res = Const.getReqInvulnerable()+Const.getFstRegex()+victimID;
            this.extraBroadcasts.add(res);
            return;
        }
        this.output.println(Const.getReqErr()); //When header is not identified or blank response
    }
}

//GameHandler is where to do run in-game methods, etc.
class GameHandler{
    //Load consts
    private NetworkConst Const = new NetworkConst();

    private ArrayList<Player> onlinePlayers;
    private int[][] mapId;
    private ArrayBlockingQueue<String> bulletQueue;
    private ArrayBlockingQueue<String> extraBroadcasts;

    GameHandler(int[][] mapId, ArrayList<Player> onlinePlayers, ArrayBlockingQueue<String> bulletQueue, ArrayBlockingQueue<String> extraBroadcasts) {
        this.mapId = mapId;
        this.onlinePlayers = onlinePlayers;
        this.bulletQueue = bulletQueue;
        this.extraBroadcasts = extraBroadcasts;
    }

    private int[] findBestSpawn() {
        ArrayList<Player> playerListCopy = (ArrayList<Player>)this.onlinePlayers.clone(); //Use copy to avoid synconize
        int[] bestSpawn = {0,0}; // maybe default?
        for (int i = 0; i < 629; i++) {
            if (mapId[i / 27][i % 27] != 2 && mapId[i / 27][i% 27] != 4) {
                bestSpawn[0] = i / 27;
                bestSpawn[1] = i % 27;
                break;
            }
        }
        //Algorithm to find best spawn <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        return bestSpawn;
    }

    //Synchronized methods
    private void addPlrToList(Player newPlayer) {synchronized(onlinePlayers) {onlinePlayers.add(newPlayer);}}
    private void removePlrFromList(Player targPlayer) {synchronized(onlinePlayers) {onlinePlayers.remove(targPlayer);}}
    
    /**
     * Loop through and see if the player already exists, if does, assign reference "thisPlayer".
     * @param clientIP The IP address of the client
     * @return Whether Player already Exists
     */
    public void removePlayerByIP(String clientIP) {
        if (!ipIsValid(clientIP)) {return;}
        Player plr = getPlayerByIP(clientIP);
        removePlrFromList(plr);
        String kickRequest = Const.getKick()+Const.getFstRegex()+plr.getId();
        extraBroadcasts.add(kickRequest);
    }
    
    private Player getPlayerByIP(String clientIP) {
        if (!ipIsValid(clientIP)) {return Const.getBlankPlayer();}
        Player player;
        synchronized(onlinePlayers) {
            for (int i=0; i<onlinePlayers.size(); i++) {
                player = onlinePlayers.get(i);
                if (player.getIP().equals(clientIP)) {
                    return player;
                };
            }
        }
        return Const.getBlankPlayer();
    }

    private Player getPlayerByID(int ID) {
        Player player;
        synchronized(onlinePlayers) { // If player is found by index
            if (ID<onlinePlayers.size()) {
                player = onlinePlayers.get(ID);
                synchronized(player) {
                    if (player.getId() == ID) {
                        return player;
                    }
                }
            }
        }
        synchronized(onlinePlayers) { // If player is not found by index, Iterate through
            for (int i=0; i<onlinePlayers.size(); i++) {
                player = onlinePlayers.get(i);
                if (player.getId() == ID) {
                    return player;
                };
            }
        }
        return Const.getBlankPlayer();
    }

    private String mapToString() {
        String result = "";
        for (int i=0; i<mapId.length;i++) {
            for (int j=0; j<mapId[i].length;j++) {
                result +=mapId[i][j];
            }
            result+="l";
        }
        return result;
    }

    //Not much synchronized methods
    private Boolean ipIsValid(String clientIP) {
        String[] IPSeg = clientIP.split("\\."); //have to use \\
        if (IPSeg.length != 4) {
            System.out.println(IPSeg[0]+IPSeg[1]+IPSeg[2]+IPSeg[3]);
            System.out.println("This IP address is impossible: "+clientIP);
            return false;
        }
        return true;
    }

    /**
     * Add player to the game, don't forget to check whether the player already exists.
     * @param plrName Player's name
     * @param BulletID Player's characterID (BulletID)
     * @param clientIP player's IP address
     * @return return the response that the server should give
     */
    public String loginPlr(String plrName, String BulletID, String clientIP, Socket clientSocket) {
        removePlayerByIP(clientIP); // see if the player already exists, and remove if exists.
        int[] Spawn = findBestSpawn();
        Player player = new Player(plrName, clientIP,clientSocket, onlinePlayers.size(), Spawn[0], Spawn[1], BulletID); // PlayerID is also the index
        String mapStr = mapToString();
        addPlrToList(player);
        String response = Const.getReqLogin()+Const.getFstRegex()+Spawn[0]+Const.getSecRegex()+Spawn[1]+Const.getSecRegex()+mapStr+Const.getSecRegex()+player.getId();
        return response;
    }

    public void updatePlrPosition(String clientIP, int newX, int newY, int direction) {
        Player player = getPlayerByIP(clientIP);
        synchronized(player){
            player.setXY(newX, newY);
            player.setDirection(direction);
        }
    }

    public Socket dmgPlr(int victimID, int lostHp) {
        Player player = getPlayerByID(victimID);
        Socket plrSocket;
        if (player == Const.getBlankPlayer()) {System.out.println("Player does not exist, ID: "+victimID); return Const.getBlankPlayer().getSocket();}
        synchronized(player) {
            player.setHp(player.getHp() - lostHp);
            plrSocket = player.getSocket();
        }
        return plrSocket;
    }

    public void queueBullet(String clientIP, int rayX, int rayY) {
        Player player = getPlayerByIP(clientIP);
        if (player == Const.getBlankPlayer()) {return;}
        String bulletID;
        bulletID = player.getBulletID();
        bulletQueue.add(rayX+Const.getSecRegex()+rayY+Const.getSecRegex()+bulletID);
    }
}

/**
 * Broadcast Handler is a seperate thread to broadcast the 200 responses.
 */
class BroadcastHandler extends Thread {
    //Load consts
    private NetworkConst Const = new NetworkConst();
    private ArrayList<Player> onlinePlayers;
    private ArrayBlockingQueue<String> bulletQueue;
    private ArrayBlockingQueue<String> extraBroadcast;
    ArrayList<Player> playerListCopy;

    BroadcastHandler(ArrayList<Player> onlinePlayers, ArrayBlockingQueue<String> bulletQueue, ArrayBlockingQueue<String> extraBroadcast) {
        this.onlinePlayers = onlinePlayers;
        this.bulletQueue = bulletQueue;
        this.extraBroadcast = extraBroadcast;
    }

    private String createBroadcastContent() {
        String result = Const.getReqNorm();
        for (int i=0; i<playerListCopy.size(); i++) {
            Player currPlr = playerListCopy.get(i);
            int plrID = currPlr.getId();
            String plrName = currPlr.getName();
            int x = currPlr.getX();
            int y = currPlr.getY();
            int direction = currPlr.getDirection();
            String bulletID = currPlr.getBulletID();
            result = result+Const.getFstRegex()+plrID+Const.getSecRegex()+x+Const.getSecRegex()+y+Const.getSecRegex()+direction+Const.getSecRegex()+bulletID+Const.getSecRegex()+plrName;
        }
        return result;
    }

    private String createBulletBroadCast() {
        if (bulletQueue.isEmpty()) {return "";}
        String result = Const.getReqRay();
        while (bulletQueue.peek() != null) {
            String element = bulletQueue.remove();
            result=result+Const.getFstRegex()+element;
        }
        return result;
    }

    private String createExtraBroadcast() {
        if (this.extraBroadcast.isEmpty()) {
            return "";
        }
        return extraBroadcast.remove();
    }

    @Override
    public void run() {  
        while (true) {
            synchronized(onlinePlayers) {playerListCopy = (ArrayList<Player>)this.onlinePlayers.clone();} //Use a copy to optimize synconize
            try{TimeUnit.MILLISECONDS.sleep(Const.getTick());} catch(Exception err) {err.printStackTrace();};
            if (playerListCopy.isEmpty()) {continue;}
            String res = createBroadcastContent();
            String bulres = createBulletBroadCast();
            String extBcRes = createExtraBroadcast();
            try{
                for (int i=0; i<playerListCopy.size(); i++) {
                    Player player = playerListCopy.get(i);
                    Socket clientSocket = player.getSocket();
                    if (clientSocket.isClosed()) {
                        continue;
                    }
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    output.println(res);
                    output.flush();
                    if (!bulres.equals("")) {
                        output.println(bulres);
                        output.flush();
                    }
                    if (!extBcRes.equals("")) {
                        output.println(extBcRes);
                        output.flush();
                    }
                }
            } catch(IOException err) {
                err.printStackTrace();
            }
            
        }
    }
}
