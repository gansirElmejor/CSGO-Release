//Hey I put your gamemap file to Client, so... the server one could be empty

public class GameMap {
    int MapId;
    private final int MAPLIMIT=27;
    int[][] map=new int[MAPLIMIT][MAPLIMIT];
    /**
    0: open space (can move around here)
    1: grass (gives invisibility to people in the grass)
    2: wall (obstacle that can be broken)
    3: water (slows down players)
    4: border (obstacle that cannot be broken)
    */
    
    GameMap(int MapId,int grassiness,int rockiness,int wateriness){
      
        this.MapId=MapId;
        //generates a wall around playing field that players cannot pass
        for(int x=0;x<27;x++){
            map[0][x]=4;
            map[MAPLIMIT-1][x]=4;
            map[x][0]=4;
            map[x][MAPLIMIT-1]=4;
        }
        
        if (grassiness>10){
          grassiness=10;
        }
        if (rockiness>10){
          rockiness=10;
        }
        if (wateriness>10){
          wateriness=10;
        }
        //map generation: all random, with inclination for stuff to spawn near each other
        //runs once for each block
        //generates recursively to make chunks of block
        
        //if we want we can change this up to make it iterative to save generation time
        for(int x=1;x<MAPLIMIT-1;x++){
            for(int y=1;y<MAPLIMIT-1;y++){
                //100 grassiness means guaranteed grass everywhere, 0 grassiness means no grass
                //small numbers are better
                //grass takes priority over rockiness which takes priority over wateriness
                //in other words grass is almost always going to be the most likely
                if(map[x][y]==0){
                    if ((int)(Math.random()*100)<grassiness){
                        map[x][y]=1;
                        addObject(1,x+1,y,1);
                        addObject(1,x-1,y,1);
                        addObject(1,x,y+1,1);
                        addObject(1,x,y-1,1);
                    }
                    else if ((int)(Math.random()*100)<rockiness){
                        map[x][y]=2;
                        addObject(2,x+1,y,2);
                        addObject(2,x-1,y,2);
                        addObject(2,x,y+1,2);
                        addObject(2,x,y-1,2);
                    }
                    else if ((int)(Math.random()*100)<wateriness){
                        map[x][y]=3;
                        addObject(3,x+1,y,1);
                        addObject(3,x-1,y,1);
                        addObject(3,x,y+1,1);
                        addObject(3,x,y-1,1);
                    }
                }
            }
        }
        for(int x=0;x<27;x++){
        for(int y=0;y<27;y++){
          System.out.print(map[x][y]);
        }
        System.out.println();
      }
        
        //rope off all small 'cages' except for the largest area
        int[][] mapclone=new int[27][27];
        for(int a=0;a<27;a++){
          mapclone[a]=map[a].clone();
        }
        int[] previousarea=new int[3];
        int thisarea;
        for(int x=1;x<26;x++){
          for(int y=1;y<26;y++){
            if (mapclone[x][y]!=2){
              thisarea=findLargestArea(mapclone,x,y,0);
              System.out.println("area discovered!");
              if (thisarea>previousarea[2]){
                if(previousarea[2]!=0&&previousarea[1]!=0&&previousarea[0]!=0){
                  System.out.print("filling area at ");
                  System.out.print(previousarea[0]);
                  System.out.print(" ");
                  System.out.print(previousarea[1]);
                  System.out.print(" with area ");
                  System.out.println(previousarea[2]);
                  fillArea(map,previousarea[0],previousarea[1]);
                }
                
                previousarea[0]=x;
                previousarea[1]=y;
                previousarea[2]=thisarea;
              }
              else{
                  if(previousarea[2]==0||previousarea[1]==0||previousarea[0]==0){
                      previousarea[0]=x;
                      previousarea[1]=y;
                      previousarea[2]=thisarea;
                      
                  }
                  else{
                    System.out.print("filling area at ");
                  System.out.print(x);
                  System.out.print(" ");
                  System.out.println(y);
                  
                  System.out.print(" with area ");
                  System.out.println(thisarea);
                      fillArea(map,x,y);
                  }
              }
            }
          }
        }
        map[13][13] = 0;
    }
    /*
    addObject
    Generates objects for the map
    is recursive, we might need to change this
    */
    public void addObject(int object,int xpos,int ypos,int odds){
        if(xpos<0 ||ypos<0||xpos>=MAPLIMIT||ypos>=MAPLIMIT){ //safety measure, should never actually need to happen
            System.out.println("warning: object was attempted to be spawned out of bounds");
            return;
        }
        if(map[xpos][ypos]>0){
            return;
        }
        if((int)(Math.random()*odds)==0){
            map[xpos][ypos]=object;
            addObject(object,xpos+1,ypos,odds+1);
            addObject(object,xpos-1,ypos,odds+1);
            addObject(object,xpos,ypos+1,odds+1);
            addObject(object,xpos,ypos-1,odds+1);
        }
        else{
            return;
        }
        
    }
    public static int findLargestArea(int[][] map,int x,int y,int sum){
      if(map[x][y]==2 || map[x][y]==4){
        return 0;
      }
      int presum=sum+1;
      map[x][y]=2;
      presum+=findLargestArea(map,x+1,y,sum);
      presum+=findLargestArea(map,x-1,y,sum);
      presum+=findLargestArea(map,x,y+1,sum);
      presum+=findLargestArea(map,x,y-1,sum);
      //System.out.println(presum);
      return presum;
    }
    public static void fillArea(int[][] map,int x,int y){
      if(map[x][y]==2 || map[x][y]==4){
        return;
      }
      map[x][y]=2;
      fillArea(map,x+1,y);
      fillArea(map,x-1,y);
      fillArea(map,x,y+1);
      fillArea(map,x,y-1);
    }
    //Add some wall position for each map; Or write algorithms to random create maps, etc.
    public static void main(String[] args){
      GameMap map = new GameMap(0,5,5,5);
      for(int x=0;x<27;x++){
        for(int y=0;y<27;y++){
          System.out.print(map.map[x][y]);
        }
        System.out.println();
      }
    }
}
