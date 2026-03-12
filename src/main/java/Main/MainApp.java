package Main;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private BorderPane mainLayout = new BorderPane();
    private String selectedMatch;
    private String selectedSeat;

    @Override
    public void start(Stage stage) {

        showMatchSelection();

        Scene scene = new Scene(mainLayout, 800, 600);

        stage.setTitle("KKU Street Fighter Ticket Booking");
        stage.setScene(scene);
        stage.show();
    }

    // =========================
    // หน้า 1 : รายการแมตช์
    // =========================
    private void showMatchSelection() {

        VBox container = new VBox(20);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(25));
        container.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("KKU STREET FIGHTER");
        title.setStyle("-fx-text-fill:white; -fx-font-size:30px; -fx-font-weight:bold;");

        Label subtitle = new Label("เลือกแมตช์ที่ต้องการชม");
        subtitle.setStyle("-fx-text-fill:#ff4d4d; -fx-font-size:18px;");

        VBox matchBox = new VBox(15);

        matchBox.getChildren().add(createMatchCard("Tiger", "Dragon", "Muay Thai", "20 May 2026"));
        matchBox.getChildren().add(createMatchCard("Falcon", "Bear", "Kick Boxing", "21 May 2026"));
        matchBox.getChildren().add(createMatchCard("Shadow", "Iron Fist", "MMA", "22 May 2026"));

        container.getChildren().addAll(title, subtitle, matchBox);

        mainLayout.setCenter(container);
    }

    private HBox createMatchCard(String f1, String f2, String type, String date) {

        VBox info = new VBox(5);

        Label fighters = new Label(f1 + "  VS  " + f2);
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

    // =========================
    // หน้า 2 : เลือกที่นั่ง
    // =========================
    private int calculatePrice(String seatZone, MemberType type) {

        int price = 0;

        if(seatZone.equals("A")) price = 1000;
        if(seatZone.equals("B")) price = 500;
        if(seatZone.equals("C")) price = 200;

        if(type == MemberType.STUDENT_GUEST){
            price = price / 2;
        }

        return price;
    }
    private void showSeatSelection() {

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color:#1a0000");

        GridPane seats = new GridPane();
        seats.setHgap(5);
        seats.setVgap(5);
        seats.setAlignment(Pos.CENTER);

        int seatNumber = 1;

        for(int r=0;r<15;r++){

            for(int c=0;c<10;c++){

                String zone;

                if(seatNumber <= 50)
                    zone = "A";
                else if(seatNumber <=100)
                    zone = "B";
                else
                    zone = "C";

                Button seat = new Button(zone + seatNumber);

                seat.setPrefSize(50,50);

                if(zone.equals("A"))
                    seat.setStyle("-fx-background-color:#ff1a1a");

                if(zone.equals("B"))
                    seat.setStyle("-fx-background-color:#ff9933");

                if(zone.equals("C"))
                    seat.setStyle("-fx-background-color:#66ccff");

                int currentSeat = seatNumber;

                seat.setOnAction(e->{

                    selectedSeat = zone + currentSeat;

                    showPayment(zone);

                });

                seats.add(seat,c,r);

                seatNumber++;
            }
        }

        StackPane ring = new StackPane();

        ring.setPrefSize(200,200);
        ring.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:red;" +
                "-fx-border-width:5;"
        );

        Label ringLabel = new Label("MUAY THAI RING");

        ring.getChildren().add(ringLabel);

        layout.setCenter(seats);
        layout.setTop(ring);

        mainLayout.setCenter(layout);
    }

    // =========================
    // หน้า 3 : ชำระเงิน
    // =========================
    private void showPayment(String zone) {

        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color:#1a0000;");

        Label title = new Label("สรุปการจองตั๋ว");
        title.setStyle("-fx-text-fill:white; -fx-font-size:26px; -fx-font-weight:bold;");

        Label matchLabel = new Label("Match : " + selectedMatch);
        matchLabel.setStyle("-fx-text-fill:#ff9999; -fx-font-size:16px;");

        Label seatLabel = new Label("Seat : " + selectedSeat);
        seatLabel.setStyle("-fx-text-fill:#ff9999; -fx-font-size:16px;");

        Label zoneLabel = new Label("Zone : " + zone);
        zoneLabel.setStyle("-fx-text-fill:#ff9999; -fx-font-size:16px;");

        // ช่องกรอกชื่อ
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setMaxWidth(200);

        // เลือกประเภทสมาชิก
        Label memberLabel = new Label("Member Type");
        memberLabel.setStyle("-fx-text-fill:white;");

        ComboBox<MemberType> memberBox = new ComboBox<>();
        memberBox.getItems().addAll(
                MemberType.NORMAL,
                MemberType.STUDENT_GUEST
        );

        memberBox.setValue(MemberType.NORMAL);

        // label แสดงราคา
        Label priceLabel = new Label();
        priceLabel.setStyle("-fx-text-fill:#ffff66; -fx-font-size:18px; -fx-font-weight:bold;");

        int basePrice = calculatePrice(zone, MemberType.NORMAL);
        priceLabel.setText("Price : " + basePrice + " บาท");

        // update price เมื่อเปลี่ยน member
        memberBox.setOnAction(e -> {

            MemberType type = memberBox.getValue();

            int price = calculatePrice(zone, type);

            priceLabel.setText("Price : " + price + " บาท");

        });

        // ปุ่มยืนยัน
        Button confirmBtn = new Button("Confirm Booking");
        confirmBtn.setStyle(
                "-fx-background-color:red;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-font-size:14px;"
        );

        confirmBtn.setOnAction(e -> {

            if(nameField.getText().isEmpty()){

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("กรุณากรอกชื่อ");
                alert.show();

                return;
            }

            showSuccess();

        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showSeatSelection());

        container.getChildren().addAll(
                title,
                matchLabel,
                seatLabel,
                zoneLabel,
                nameField,
                memberLabel,
                memberBox,
                priceLabel,
                confirmBtn,
                backBtn
        );

        mainLayout.setCenter(container);
    }

    // =========================
    // หน้า 4 : สำเร็จ
    // =========================
    
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

    public static void main(String[] args) {
        launch();
    }
}