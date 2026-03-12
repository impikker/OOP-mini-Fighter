package Main;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Seat {
    private String seatId;  
    private Zone zone;
    private double basePrice; 
    private boolean isBooked;

    public Seat() {}
    public Seat(String seatId,Zone zone) {
        this.seatId = seatId;
        this.zone = zone;
        this.isBooked = false; 
        
        if (zone == Zone.A) {
            this.basePrice = 1000.0;
        } else if (zone == Zone.B) {
            this.basePrice = 500.0;
        } else if(zone == Zone.C){
            this.basePrice = 200.0; // ราคาประหยัด
        }
    }

    public String getSeatId() { return seatId; }
    public Zone getZone() { return zone; }
    public double getBasePrice() { return basePrice; }
    public boolean isBooked() { return isBooked; }

    public void setBooked(boolean booked) { isBooked = booked; }
    
    @Override
    public String toString() {
        return "Seat " + seatId + " [" + zone + "] - Price: " + basePrice + " (" + (isBooked ? "Sold" : "Available") + ")";
    }
}
