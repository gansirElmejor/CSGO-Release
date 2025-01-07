import java.util.HashMap;

public class Character {
    CharacterConst characterConst = new CharacterConst();
    HashMap<String, CharProperty> knownCharacters = characterConst.getKnownCharacters();
    CharProperty currCharacter;

    Character(String bulletID) {
        if (!knownCharacters.containsKey(bulletID)) {
            System.out.println("Such bulletID does not exist: "+bulletID);
            return;
        }
        currCharacter = knownCharacters.get(bulletID);
    }

    public int getMaxHp() {return currCharacter.getMaxHp();} 
    public int getDamagePerShot() {return currCharacter.getDamagePerShot();} //Should Migrate to client
    public int getSpeed() {return currCharacter.getSpeed();} //Should Migrate to client
}
