package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.util.List;

public class JSONManager {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }
    public static <T> void saveToFile(String filePath, List<T> data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
            //System.out.println("บันทึกข้อมูลลง " + filePath + " สำเร็จ!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static <T> List<T> loadFromFile(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return null;
            
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}