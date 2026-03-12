package Main;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StudentGuest.class, name = "student"),
    @JsonSubTypes.Type(value = GeneralGuest.class, name = "general")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Guest {
    private String id;
    private String name;

    public Guest() {}
    public Guest(String id, String name) {
        this.id = id;
        this.name = name;
    }
    // Abstract method ลูกแต่ละคนต้องไปคำนวณราคาตั๋วเอง
    public abstract double getDiscountRate();
    public String getId() { return id; }
    public String getName() { return name; }
}

class StudentGuest extends Guest {
    public StudentGuest(String id, String name) { super(id, name); }
    @Override
    public double getDiscountRate() {
        return 0.5; 
    }
}
class GeneralGuest extends Guest {
    public GeneralGuest(String id, String name) { super(id, name); }
    @Override
    public double getDiscountRate() {
        return 1.0;
    }
}