package Main;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;

enum MemberType {
    NORMAL, STUDENT_GUEST
}

// 1. เพิ่ม Class User เพื่อเก็บข้อมูลให้ครบถ้วน
class User {
    String username;
    String password;
    String email;
    MemberType type;

    User(String username, String password, String email, MemberType type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.type = type;
    }
}

public class MainApp extends Application {

    private BorderPane mainLayout = new BorderPane();
    private String selectedMatch;
    private String selectedSeat;
    private User currentUser; // เปลี่ยนจาก String เป็น User object
    
    // เปลี่ยน HashMap ให้เก็บ User Object
    private HashMap<String, User> userDatabase = new HashMap<>();

    @Override
    public void start(Stage stage) {
        // เพิ่ม User admin เริ่มต้น
        userDatabase.put("admin", new User("admin", "1234", "admin@kkumail.com", MemberType.NORMAL));
        showLogin();

        Scene scene = new Scene(mainLayout, 850, 650);
        stage.setTitle("KKU Street Fighter Ticket Booking");
        stage.setScene(scene);
        stage.show();
    }

    private void showLogin() {
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(50));
        loginBox.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("USER LOGIN");
        title.setStyle("-fx-text-fill:white; -fx-font-size:30px; -fx-font-weight:bold;");

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMaxWidth(250);
        userField.setStyle("-fx-background-color:#330000; -fx-text-fill:white; -fx-border-color:red;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);
        passField.setStyle("-fx-background-color:#330000; -fx-text-fill:white; -fx-border-color:red;");

        Button loginBtn = new Button("LOGIN");
        loginBtn.setMinWidth(250);
        loginBtn.setStyle("-fx-background-color:#ff1a1a; -fx-text-fill:white; -fx-font-weight:bold; -fx-font-size:16px;");

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register here");
        registerLink.setStyle("-fx-text-fill:#cccccc;");
        registerLink.setOnAction(e -> showRegister());

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill:yellow;");

        loginBtn.setOnAction(e -> {
            String userNameInput = userField.getText();
            String passInput = passField.getText();

            if (userDatabase.containsKey(userNameInput) && userDatabase.get(userNameInput).password.equals(passInput)) {
                currentUser = userDatabase.get(userNameInput); // เก็บ Object ของ User ที่ Login
                showMatchSelection();
            } else {
                errorLabel.setText("Invalid username or password!");
            }
        });

        loginBox.getChildren().addAll(title, userField, passField, loginBtn, registerLink, errorLabel);
        mainLayout.setCenter(loginBox);
    }

    private void showRegister() {
        VBox regBox = new VBox(15);
        regBox.setAlignment(Pos.CENTER);
        regBox.setPadding(new Insets(50));
        regBox.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("REGISTER");
        title.setStyle("-fx-text-fill:white; -fx-font-size:30px; -fx-font-weight:bold;");

        TextField newUser = new TextField();
        newUser.setPromptText("New Username");
        newUser.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email (gmail, kkumail, etc.)");
        emailField.setMaxWidth(250);

        ComboBox<MemberType> memberTypeBox = new ComboBox<>();
        memberTypeBox.getItems().addAll(MemberType.NORMAL, MemberType.STUDENT_GUEST);
        memberTypeBox.setValue(MemberType.NORMAL); 
        memberTypeBox.setMinWidth(250);
        memberTypeBox.setStyle("-fx-background-color: #4d0000; -fx-mark-color: white; -fx-border-color: #ff1a1a; -fx-border-width: 2px; -fx-font-weight: bold;");

        // Cell Factory เพื่อให้มองเห็นตัวเลือกชัดเจน
        memberTypeBox.setCellFactory(lv -> new ListCell<MemberType>() {
            @Override protected void updateItem(MemberType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle("-fx-background-color: #330000;"); }
                else { setText(item.toString()); setStyle("-fx-background-color: #330000; -fx-text-fill: white;"); }
            }
        });
        memberTypeBox.setButtonCell(new ListCell<MemberType>() {
            @Override protected void updateItem(MemberType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else { setText(item.toString()); setStyle("-fx-text-fill: white;"); }
            }
        });

        PasswordField newPass = new PasswordField();
        newPass.setPromptText("New Password");
        newPass.setMaxWidth(250);
        
        PasswordField confirmPass = new PasswordField();
        confirmPass.setPromptText("Confirm Password");
        confirmPass.setMaxWidth(250);

        Label errorLabel = new Label(""); 
        errorLabel.setStyle("-fx-text-fill:yellow;");

        Button regBtn = new Button("CREATE ACCOUNT");
        regBtn.setMinWidth(250);
        regBtn.setStyle("-fx-background-color:#008000; -fx-text-fill:white; -fx-font-weight:bold;");

        Button backBtn = new Button("Back to Login");
        backBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:white;");
        backBtn.setOnAction(e -> showLogin());

        regBtn.setOnAction(e -> {
            String user = newUser.getText();
            String email = emailField.getText().toLowerCase().trim();
            String pass = newPass.getText();
            String cpPass = confirmPass.getText();
            MemberType type = memberTypeBox.getValue();

            // สูตรตรวจสอบรูปแบบ Email
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || cpPass.isEmpty()) {
                errorLabel.setText("Please fill in all fields!");
            } 
            else if (!email.matches(emailPattern)) {
                errorLabel.setText("Invalid Email format! (e.g. name@host.com)");
            }
            else if (!(email.endsWith("@gmail.com") || email.endsWith("@kkumail.com") || 
                       email.endsWith("@icloud.com") || email.endsWith("@hotmail.com") ||
                       email.endsWith("@outlook.com") || email.endsWith("@yahoo.com") ||
                       email.endsWith("@live.com"))) {
                errorLabel.setText("Email domain not supported!");
            }
            else if (!pass.equals(cpPass)) {
                errorLabel.setText("Passwords do not match!");
            } 
            else if (userDatabase.containsKey(user)) {
                errorLabel.setText("This username is already taken!");
            }
            else {
                userDatabase.put(user, new User(user, pass, email, type));
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration Successful!");
                alert.showAndWait();
                showLogin();
            }
        });

        regBox.getChildren().addAll(title, newUser, emailField, memberTypeBox, newPass, confirmPass, regBtn, errorLabel, backBtn);
        mainLayout.setCenter(regBox);
    }

    private void showMatchSelection() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(25));
        container.setStyle("-fx-background-color:#1a0000;");

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        
        Button profileBtn = new Button("My Profile 👤");
        profileBtn.setStyle("-fx-background-color:#444444; -fx-text-fill:white;");
        profileBtn.setOnAction(e -> showProfile());
        
        topBar.getChildren().addAll(profileBtn);

        Label title = new Label("KKU STREET FIGHTER");
        title.setStyle("-fx-text-fill:white; -fx-font-size:30px; -fx-font-weight:bold;");

        Label subtitle = new Label("เลือกแมตช์ที่ต้องการชม");
        subtitle.setStyle("-fx-text-fill:#ff4d4d; -fx-font-size:18px;");

        VBox matchBox = new VBox(15);
        matchBox.getChildren().add(createMatchCard("Tiger", "Dragon", "Muay Thai", "20 May 2026"));
        matchBox.getChildren().add(createMatchCard("Falcon", "Bear", "Kick Boxing", "21 May 2026"));
        matchBox.getChildren().add(createMatchCard("Shadow", "Iron Fist", "MMA", "22 May 2026"));

        container.getChildren().addAll(topBar, title, subtitle, matchBox);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1a0000; -fx-border-color: #1a0000;");
        
        mainLayout.setCenter(scrollPane);
    }

    private HBox createMatchCard(String f1, String f2, String type, String date) {
        VBox info = new VBox(5);
        Label fighters = new Label(f1 + " VS " + f2);
        fighters.setStyle("-fx-text-fill:white; -fx-font-size:18px; -fx-font-weight:bold;");
        Label discipline = new Label(type);
        discipline.setStyle("-fx-text-fill:#ff9999;");
        Label time = new Label(date);
        time.setStyle("-fx-text-fill:#cccccc;");
        info.getChildren().addAll(fighters, discipline, time);

        Button bookBtn = new Button("จองตั๋ว");
        bookBtn.setStyle("-fx-background-color:#ff1a1a; -fx-text-fill:white; -fx-font-weight:bold;");
        bookBtn.setOnAction(e -> {
            selectedMatch = f1 + " VS " + f2;
            showSeatSelection();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox card = new HBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color:#330000; -fx-border-color:red;");
        card.getChildren().addAll(info, spacer, bookBtn);
        return card;
    }

    private void showSeatSelection() {
        VBox seatContainer = new VBox(10);
        seatContainer.setAlignment(Pos.CENTER);
        seatContainer.setPadding(new Insets(20));
        seatContainer.setStyle("-fx-background-color:#1a0000");

        Label screenLabel = new Label("--- RING SIDE / STAGE ---");
        screenLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        GridPane seats = new GridPane();
        seats.setHgap(5); seats.setVgap(5);
        seats.setAlignment(Pos.CENTER);

        int seatNumber = 1;
        for(int r=0; r<10; r++){ 
            for(int c=0; c<10; c++){
                String zone;
                if(seatNumber <= 30) zone = "A";
                else if(seatNumber <= 70) zone = "B";
                else zone = "C";

                Button seat = new Button(zone + seatNumber);
                seat.setPrefSize(55,40);
                if(zone.equals("A")) seat.setStyle("-fx-background-color:#ff1a1a; -fx-text-fill:white;");
                else if(zone.equals("B")) seat.setStyle("-fx-background-color:#ff9933; -fx-text-fill:white;");
                else seat.setStyle("-fx-background-color:#66ccff; -fx-text-fill:black;");

                String currentSeatID = zone + seatNumber;
                String currentZone = zone;
                seat.setOnAction(e->{
                    selectedSeat = currentSeatID;
                    showPayment(currentZone);
                });
                seats.add(seat, c, r);
                seatNumber++;
            }
        }
        
        Button backBtn = new Button("Cancel");
        backBtn.setOnAction(e -> showMatchSelection());

        seatContainer.getChildren().addAll(screenLabel, seats, backBtn);
        mainLayout.setCenter(seatContainer);
    }

    private void showPayment(String zone) {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("สรุปการจองตั๋ว");
        title.setStyle("-fx-text-fill:white; -fx-font-size:26px; -fx-font-weight:bold;");

        Label details = new Label("Match: " + selectedMatch + "\nSeat: " + selectedSeat);
        details.setStyle("-fx-text-fill:white; -fx-text-alignment:center;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(200);

        ComboBox<MemberType> memberBox = new ComboBox<>();
        memberBox.getItems().addAll(MemberType.NORMAL, MemberType.STUDENT_GUEST);
        
        // ดึงค่าตั้งต้นจากข้อมูลโปรไฟล์ของผู้ใช้ที่ Login อยู่
        memberBox.setValue(currentUser.type);

        Label priceLabel = new Label("Price : " + calculatePrice(zone, currentUser.type) + " บาท");
        priceLabel.setStyle("-fx-text-fill:#ffff66; -fx-font-size:18px; -fx-font-weight:bold;");

        memberBox.setOnAction(e -> {
            priceLabel.setText("Price : " + calculatePrice(zone, memberBox.getValue()) + " บาท");
        });

        Button confirmBtn = new Button("Confirm Booking");
        confirmBtn.setStyle("-fx-background-color:red; -fx-text-fill:white; -fx-font-weight:bold;");
        confirmBtn.setOnAction(e -> {
            if(nameField.getText().isEmpty()){
                new Alert(Alert.AlertType.WARNING, "กรุณากรอกชื่อ").show();
                return;
            }
            showSuccess();
        });

        Button backBtn = new Button("Back to Seats");
        backBtn.setOnAction(e -> showSeatSelection());

        container.getChildren().addAll(title, details, nameField, memberBox, priceLabel, confirmBtn, backBtn);
        mainLayout.setCenter(container);
    }

    private int calculatePrice(String seatZone, MemberType type) {
        int price = 0;
        if(seatZone.equals("A")) price = 1000;
        else if(seatZone.equals("B")) price = 500;
        else price = 200;
        
        if(type == MemberType.STUDENT_GUEST) price = price / 2;
        return price;
    }

    private void showSuccess() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color:#1a0000;");
        Label icon = new Label("✔");
        icon.setStyle("-fx-font-size:80px; -fx-text-fill:#00ff66;");
        Label text = new Label("จองตั๋วสำเร็จ!");
        text.setStyle("-fx-text-fill:white; -fx-font-size:24px;");
        Button home = new Button("กลับหน้าหลัก");
        home.setOnAction(e -> showMatchSelection());
        container.getChildren().addAll(icon, text, home);
        mainLayout.setCenter(container);
    }

    // 3. ปรับปรุงหน้า Profile ให้แสดงข้อมูลทั้งหมด
    private void showProfile() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("USER PROFILE");
        title.setStyle("-fx-text-fill:white; -fx-font-size:30px; -fx-font-weight:bold;");

        Label userIcon = new Label("👤");
        userIcon.setStyle("-fx-font-size:80px; -fx-text-fill:red;");

        // แสดงข้อมูลจาก Object currentUser
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER);
        
        Label userLabel = new Label("Username: " + currentUser.username);
        Label emailLabel = new Label("Email: " + currentUser.email);
        Label typeLabel = new Label("Member Type: " + currentUser.type);
        
        String style = "-fx-text-fill:white; -fx-font-size:18px;";
        userLabel.setStyle(style);
        emailLabel.setStyle(style);
        typeLabel.setStyle(style + "-fx-text-fill: #ffff66;"); // ไฮไลท์ประเภทสมาชิกเป็นสีเหลือง

        infoBox.getChildren().addAll(userLabel, emailLabel, typeLabel);

        Button logoutBtn = new Button("LOGOUT");
        logoutBtn.setMinWidth(200);
        logoutBtn.setStyle("-fx-background-color:#ff1a1a; -fx-text-fill:white; -fx-font-weight:bold;");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showLogin();
        });

        Button backBtn = new Button("Back to Home");
        backBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:#cccccc;");
        backBtn.setOnAction(e -> showMatchSelection());

        container.getChildren().addAll(title, userIcon, infoBox, logoutBtn, backBtn);
        mainLayout.setCenter(container);
    }

    public static void main(String[] args) {
        launch();
    }
}
