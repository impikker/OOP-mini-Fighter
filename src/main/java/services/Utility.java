package services;
import Main.Guest;
import Main.Match;
import Main.Seat;
import Main.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
public class Utility {
	
	
    // 1. เก็บ Data ทั้งหมดไว้ที่นี่
    private List<Match> matches = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();

    // 2. โหลดข้อมูลจากไฟล์ทันทีที่สร้างคลาสนี้
    public Utility() {
        loadAllData();
    }
    //methods 
    public void addNewMatch(Match m) {
        matches.add(m);
    }
    public void registerGuest(Guest g) {
        guests.add(g);
    }
    public Ticket buyTicket(Match m, Seat s, Guest g) {
    	for (Ticket t : tickets) {
    	    if (t.getOwner().getId().equals(g.getId()) &&
    	        t.getMatch().equals(m)) {
    	        return null;
    	    }
    	}
        if (s.isBooked()) return null;
        Ticket t = new Ticket(m, s, g);
        tickets.add(t);
        saveAllData(); // บันทึกทุกครั้งที่มีการจอง
        return t;
    }
    // --- ระบบจัดการไฟล์ (เรียกผ่าน JSONManager อีกที) ---
    public void saveAllData() {
    	JSONManager.saveToFile("matches.json", matches);
    	JSONManager.saveToFile("tickets.json", tickets);
    	JSONManager.saveToFile("guests.json", guests);
        //System.out.println("ระบบ: บันทึกข้อมูลทั้งหมดเรียบร้อย!");
    }
    public void loadAllData() {
        List<Match> loadedMatches = JSONManager.loadFromFile("matches.json", Match.class);
        if (loadedMatches != null) this.matches = loadedMatches;
        
        List<Ticket> loadedTickets = JSONManager.loadFromFile("tickets.json", Ticket.class);
        if (loadedTickets != null) this.tickets = loadedTickets;
        
        List<Guest> loadedGuests = JSONManager.loadFromFile("guests.json", Guest.class);
        if (loadedGuests != null) this.guests = loadedGuests;
    }

    public List<Match> getAllMatches() { return matches; }
    public List<Ticket> getAllTickets() { return tickets; }
}
