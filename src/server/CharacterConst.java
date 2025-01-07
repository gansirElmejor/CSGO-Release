import java.util.HashMap;

public class CharacterConst {
    //Bro didn't click commit change ;)
    private HashMap<String, CharProperty> knownCharacters = new HashMap<String,CharProperty>();
    //character data
    /*
    final String[] CHAR_LIST=new String[]{"Standard","Hotshot","Bulky","Stronk","Dash","Hook","Ghost","Freezer","Landmine","Melee","Neutralizer","Joker"};
    final int[] MAXHEALTH_LIST=new int[] {   120    ,   100   ,  250  ,   60   ,  85  ,  75  ,   75  ,   65    ,    65    ,  110  ,     130     ,   70  };
    final int[] DAMAGE_LIST=new int[]    {    12    ,    8    ,   4   ,   20   ,   9  ,  13  ,   10  ,   10    ,     5    ,   3   ,      10     ,   3   };
    final int[] SPEED_LIST=new int[]     {    10    ,    8    ,   7   ,   12   ,  14  ,   9  ,   11  ,   11    ,    10    ,  12   ,      9      ,   6   };
    */
    
    final String CHAR_1 = "Standard"; final int CHAR_1maxHp = 120; final int CHAR_1damagePerShot = 12; final int CHAR_1speed = 10;
    final String CHAR_2 = "Hotshot"; final int CHAR_2maxHp = 100; final int CHAR_2damagePerShot = 8; final int CHAR_2speed = 8;
    final String CHAR_3 = "Bulky"; final int CHAR_3maxHp = 250; final int CHAR_3damagePerShot = 4; final int CHAR_3speed = 7;
    final String CHAR_4 = "Stronk"; final int CHAR_4maxHp = 60; final int CHAR_4damagePerShot = 20; final int CHAR_4speed = 12;
    final String CHAR_5 = "Dash"; final int CHAR_5maxHp = 85; final int CHAR_5damagePerShot = 9; final int CHAR_5speed = 14;
    final String CHAR_6 = "Hook"; final int CHAR_6maxHp = 75; final int CHAR_6damagePerShot = 13; final int CHAR_6speed = 9;
    final String CHAR_7 = "Ghost"; final int CHAR_7maxHp = 75; final int CHAR_7damagePerShot = 10; final int CHAR_7speed = 11;
    final String CHAR_8 = "Freezer"; final int CHAR_8maxHp = 65; final int CHAR_8damagePerShot = 10; final int CHAR_8speed = 11;
    final String CHAR_9 = "Landmine"; final int CHAR_9maxHp = 65; final int CHAR_9damagePerShot = 5; final int CHAR_9speed = 10;
    final String CHAR_10 = "Melee"; final int CHAR_10maxHp = 110; final int CHAR_10damagePerShot = 3; final int CHAR_10speed = 12;
    final String CHAR_11 = "Neutralizer"; final int CHAR_11maxHp = 130; final int CHAR_11damagePerShot = 10; final int CHAR_11speed = 9;
    final String CHAR_12 = "Joker"; final int CHAR_12maxHp = 70; final int CHAR_12damagePerShot = 3; final int CHAR_12speed = 6;
    //might not use? used for joker ability, we use if we switch the joker character whenever the power is used
    final String CHAR_13 = "JokerBuffed";                             final int CHAR_13damagePerShot = 25; final int CHAR_13speed = 18;
    
    
    
    
    //hey waitaminute isn't joker a smash character that does something similar
    /*
    extra data tidbits that we can use
    HP
    high: 130-200        above average: 120        average: 85-110        low: 65-75        very low: 60
    the average character has low hp
    attack
    high: 13-20          above average: 12         average: 9-10          low: 4-8          very low: 3
    
    speed
    high: 12-14          above average: 10-11      average: 8-9           low: 6-7          very low: 5
    the average character has above average speed

    
    */
    CharacterConst() {
        
        knownCharacters.put(CHAR_1,new CharProperty(CHAR_1maxHp,CHAR_1damagePerShot,CHAR_1speed));
        knownCharacters.put(CHAR_2,new CharProperty(CHAR_2maxHp,CHAR_2damagePerShot,CHAR_2speed));
        knownCharacters.put(CHAR_3,new CharProperty(CHAR_3maxHp,CHAR_3damagePerShot,CHAR_3speed));
        knownCharacters.put(CHAR_4,new CharProperty(CHAR_4maxHp,CHAR_4damagePerShot,CHAR_4speed));
        knownCharacters.put(CHAR_5,new CharProperty(CHAR_5maxHp,CHAR_5damagePerShot,CHAR_5speed));
        knownCharacters.put(CHAR_6,new CharProperty(CHAR_6maxHp,CHAR_6damagePerShot,CHAR_6speed));
        knownCharacters.put(CHAR_7,new CharProperty(CHAR_7maxHp,CHAR_7damagePerShot,CHAR_7speed));
        knownCharacters.put(CHAR_8,new CharProperty(CHAR_8maxHp,CHAR_8damagePerShot,CHAR_8speed));
        knownCharacters.put(CHAR_9,new CharProperty(CHAR_9maxHp,CHAR_9damagePerShot,CHAR_9speed));
        knownCharacters.put(CHAR_10,new CharProperty(CHAR_10maxHp,CHAR_10damagePerShot,CHAR_10speed));
        knownCharacters.put(CHAR_11,new CharProperty(CHAR_11maxHp,CHAR_11damagePerShot,CHAR_11speed));
        knownCharacters.put(CHAR_12,new CharProperty(CHAR_12maxHp,CHAR_12damagePerShot,CHAR_12speed));
        
    }

    public HashMap<String, CharProperty> getKnownCharacters() {
        return this.knownCharacters;
    }
}

class CharProperty {
    private int maxHp;
    private int damagePerShot;
    private int speed;
    CharProperty(int maxHp,int damagePerShot,int speed) {
        this.maxHp = maxHp;
        this.damagePerShot = damagePerShot;
        this.speed = speed;
    }
    public int getMaxHp() {return this.maxHp;}
    public int getDamagePerShot() {return this.damagePerShot;}
    public int getSpeed() {return this.speed;}
}
