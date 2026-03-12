package Main;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Fighter implements Serializable {
    private String nickname;
    private String affiliation;
    private double weight;
    private double height;
    private String weightClass; 

    public Fighter() {}
    public Fighter(String nickname, String affiliation, double weight, double height) {
        this.nickname = nickname;
        this.affiliation = affiliation;
        this.weight = weight;
        this.height = height;
        this.weightClass = determineWeightClass(weight); 
    }

    private String determineWeightClass(double weight) {
        if (weight < 50) return "Strawweight";
        if (weight < 60) return "Flyweight";
        if (weight < 70) return "Lightweight";
        return "Heavyweight";
    }

    public String getNickname() { return nickname; }
    public String getAffiliation() { return affiliation; }
    public double getWeight() {return weight;}
    public double getHeight() {return height;}
    public String getWeightClass() { return weightClass; }
    
}