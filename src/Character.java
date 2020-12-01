public class Character {

    private String name;
    private int occurrences;
    private int proximity;

    public Character(String characterName){
        this.name = characterName;
    }

    public String getName(){ return name; }

    public void incrementOccurrences(){ occurrences++; }

    public int getOccurrences(){ return occurrences; }

    public void incrementProximity(){ proximity++; }

    public int getProximity(){ return proximity; }

    public double getClosenessFactor(){ return (double)proximity / occurrences; }
}
