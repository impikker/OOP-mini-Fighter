package Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Main.Zone;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Match {
	Fighter f1;
	Fighter f2;
	private List<Seat> seats = new ArrayList<>();
	private LocalDateTime matchDateTime;
	private Discipline discipline;
	
	
	public Match() {}
	public Match(Fighter f1, Fighter f2, LocalDateTime matchDateTime, Discipline discipline) {
        this.f1 = f1;
        this.f2 = f2;
        this.matchDateTime = matchDateTime;
        this.discipline = discipline;
        generateSeats();
}
	public String getMatchTitle() {
        return f1.getNickname() + " vs " + f2.getNickname();
    }
	public Fighter getFighter1() { return f1; }
	public Fighter getFighter2() { return f2; }
	
	public String getFormattedDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return matchDateTime.format(dtf); 
    }
    public String getFormattedTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return matchDateTime.format(dtf); 
    }
    public List<Seat> getSeats() {
        return seats;
    }
    public void generateSeats() {
        for (int i = 1; i <= 10; i++) {
            seats.add(new Seat("A" + i, Zone.A));
            seats.add(new Seat("B" + i, Zone.B));
            seats.add(new Seat("C" + i, Zone.C));
        }
    }
    
    //ตอนสร้าง match.generateSeats();
    public String toString() {
    	java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    	return "==========================\n" +
        " MATCH DETAILS \n" +
        "==========================\n" +
        "คู่ชก: " + getMatchTitle() + "\n" +
        "กติกา: " + discipline + "\n" +
        "เวลาแข่ง: " + matchDateTime.format(dtf) + "\n" +
        "--------------------------\n" +
        "รายละเอียดนักชกฝ่ายแดง (F1):\n" +
        f1.getNickname() + " from " + f1.getAffiliation() + "\n" +
        "รุ่น "+ f1.getWeightClass()+ "\n" +
        "\nรายละเอียดนักชกฝ่ายน้ำเงิน (F2):\n" +
          f2.getNickname() + " from " + f2.getAffiliation() + "\n" +
        "รุ่น "+ f2.getWeightClass()+ "\n"
       + "--------------------------\n" ;

    }
}
