package Main;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)


public class Ticket {
    private Match match;
    private Seat seat;
    private Guest owner;
    private LocalDateTime bookingTime;
    public Match getMatch() {
        return match;
    }

    public Guest getOwner() {
        return owner;
    }
    public Ticket() {}
    public Ticket(Match match, Seat seat, Guest owner) {
    	if(seat.isBooked()) {
    	    throw new IllegalStateException("Seat already booked");
    	}
        this.match = match;
        this.seat = seat;
        this.owner = owner;
        this.bookingTime = LocalDateTime.now();
        this.seat.setBooked(true);
    }

    // เมธอดคำนวณราคาสุดท้าย (ดึงส่วนลดมาจากคลาส Guest)
    public double calculateFinalPrice() {
        double discount = owner.getDiscountRate(); 
        return seat.getBasePrice() * discount;
    }

    @Override
    public String toString() {
        return "---------- TICKET ----------\n" +
               "Match: " + match.getMatchTitle() + "\n" +
               "Seat: " + seat.getSeatId() + " (" + seat.getZone() + ")\n" +
              
               "Owner: " + owner.getName() + "\n" +
               "Total Paid: " + calculateFinalPrice() + " THB\n" +
               "----------------------------";
    }
}