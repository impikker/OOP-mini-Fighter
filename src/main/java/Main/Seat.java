package Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Seat extends JFrame {
    private Map<String, Integer> seatInventory = new HashMap<>();
    private final int MAX_A = 20;
    private final int MAX_B = 30;
    private final int MAX_C = 30;

    private List<String> cartItems = new ArrayList<>();
    private double totalAmount = 0;
    private JLabel statusLabel;

    public Seat() {
    	
        initInventory();
        setTitle("KKU ARENA - PAYMENT SYSTEM");
        setSize(950, 950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- ส่วนผังที่นั่ง (Center) ---
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new Insets(3, 3, 3, 3);

        Color colorArena = new Color(180, 0, 0);      
        Color colorA = new Color(80, 0, 0);    
        Color colorB = new Color(120, 0, 0);   
        Color colorC = new Color(160, 0, 0);   

        addZone(mainPanel, gbc, 0, 0, 7, 1, "C1", colorC, 200);
        addZone(mainPanel, gbc, 1, 1, 5, 1, "B1", colorB, 500);
        addZone(mainPanel, gbc, 2, 2, 3, 1, "A1", colorA, 1000);
        addZone(mainPanel, gbc, 0, 1, 1, 5, "C4", colorC, 200);
        addZone(mainPanel, gbc, 1, 2, 1, 3, "B4", colorB, 500);
        addZone(mainPanel, gbc, 2, 3, 1, 1, "A4", colorA, 1000);
        
        JLabel arena = new JLabel("ARENA", SwingConstants.CENTER);
        arena.setOpaque(true); arena.setBackground(Color.BLACK);
        arena.setForeground(colorArena); arena.setFont(new Font("Impact", Font.BOLD, 35));
        arena.setBorder(BorderFactory.createLineBorder(colorArena, 3));
        gbc.gridx = 3; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
        mainPanel.add(arena, gbc);
        
        addZone(mainPanel, gbc, 4, 3, 1, 1, "A2", colorA, 1000);
        addZone(mainPanel, gbc, 5, 2, 1, 3, "B2", colorB, 500);
        addZone(mainPanel, gbc, 6, 1, 1, 5, "C2", colorC, 200);
        addZone(mainPanel, gbc, 2, 4, 3, 1, "A3", colorA, 1000);
        addZone(mainPanel, gbc, 1, 5, 5, 1, "B3", colorB, 500);
        addZone(mainPanel, gbc, 0, 6, 7, 1, "C3", colorC, 200);

        // --- ส่วนควบคุมด้านล่าง (Bottom) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.setPreferredSize(new Dimension(0, 100));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, colorArena));

        JButton backBtn = new JButton("<< Back");
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.GRAY);
        backBtn.addActionListener(e -> this.dispose());

        statusLabel = new JLabel(" No seats selected | Total: 0 THB", JLabel.LEFT);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton checkoutBtn = new JButton("Pay Now");
        checkoutBtn.setBackground(colorArena);
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        checkoutBtn.addActionListener(e -> processCheckout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(backBtn);
        leftPanel.add(statusLabel);

        bottomPanel.add(leftPanel, BorderLayout.CENTER);
        bottomPanel.add(checkoutBtn, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initInventory() {
        String[] zones = {"A1","A2","A3","A4","B1","B2","B3","B4","C1","C2","C3","C4"};
        for (String z : zones) seatInventory.put(z, z.startsWith("A") ? MAX_A : MAX_B);
    }

    private void addZone(JPanel panel, GridBagConstraints gbc, int x, int y, int w, int h, String name, Color bg, double price) {
        gbc.gridx = x; gbc.gridy = y;
        gbc.gridwidth = w; gbc.gridheight = h;
        JButton btn = new JButton();
        updateButtonText(btn, name);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.addActionListener(e -> showCounterDialog(name, price, btn));
        panel.add(btn, gbc);
    }

    private void updateButtonText(JButton btn, String name) {
        int left = seatInventory.get(name);
        btn.setText("<html><center>" + name + "<br><font size='3'>Available: " + left + "</font></center></html>");
        if (left <= 0) { btn.setEnabled(false); btn.setBackground(Color.DARK_GRAY); }
    }

    private void showCounterDialog(String zoneName, double price, JButton parentBtn) {
        int available = seatInventory.get(zoneName);
        JDialog dialog = new JDialog(this, "Select Amount", true);
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        final int[] count = {1};
        JLabel cLabel = new JLabel("     " + count[0] + "     ");
        cLabel.setFont(new Font("Arial", Font.BOLD, 25));

        JButton pBtn = new JButton("+");
        pBtn.addActionListener(e -> { if(count[0] < available) { count[0]++; cLabel.setText("    "+count[0]+"    "); } });
        
        JButton mBtn = new JButton("-");
        mBtn.addActionListener(e -> { if(count[0] > 1) { count[0]--; cLabel.setText(" "+count[0]+" "); } });

        JButton addBtn = new JButton(" ADD TO CART ");
        addBtn.addActionListener(e -> {
            seatInventory.put(zoneName, available - count[0]);
            cartItems.add(zoneName + " (x" + count[0] + ")");
            totalAmount += (price * count[0]);
            updateButtonText(parentBtn, zoneName);
            statusLabel.setText(" CART: " + cartItems.toString() + " | Total: " + totalAmount + ".-");
            dialog.dispose();
        });

        dialog.add(mBtn); dialog.add(cLabel); dialog.add(pBtn); dialog.add(addBtn);
        dialog.setVisible(true);
    }

    // --- ระบบชำระเงิน (Payment Gateway) ---
    private void processCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select your seats before checkout!");
            return;
        }

        // สร้างหน้าต่างชำระเงินใหม่
        JDialog payDialog = new JDialog(this, "Pay with PromptPay", true);
        payDialog.setSize(400, 600);
        payDialog.setLayout(new BorderLayout());
        payDialog.getContentPane().setBackground(Color.WHITE);

        // ส่วนแสดงยอดเงิน
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(0, 102, 204)); // สีน้ำเงิน PromptPay
        JLabel title = new JLabel("PromptPay QR Code", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        JLabel amountLabel = new JLabel("Total: " + totalAmount + " THB", SwingConstants.CENTER);
        amountLabel.setForeground(Color.YELLOW);
        amountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        header.add(title);
        header.add(amountLabel);
        
       
     // ส่วนจำลอง QR Code (ใช้ JLabel วาดกรอบสี่เหลี่ยมแทนรูปภาพจริง)
        JPanel qrPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(50, 50, 250, 250); // กรอบนอก QR
                g.setColor(Color.WHITE);
                g.fillRect(70, 70, 210, 210); // พื้นที่ QR
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString("QR CODE SIMULATION", 110, 175);
            }
        };
        qrPanel.setPreferredSize(new Dimension(350, 350));
        qrPanel.setBackground(Color.WHITE);
        // ปุ่มยืนยัน
        JButton confirmPayBtn = new JButton("Payment Completed");
        confirmPayBtn.setBackground(new Color(0, 153, 76));
        confirmPayBtn.setForeground(Color.WHITE);
        confirmPayBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        confirmPayBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(payDialog, "Thank you! Your payment proof has been sent for verification. You will receive your E-Ticket shortly");
            cartItems.clear();
            totalAmount = 0;
            statusLabel.setText(" No seats selected | Total: 0 THB");
            payDialog.dispose();
        });

        payDialog.add(header, BorderLayout.NORTH);
        payDialog.add(qrPanel, BorderLayout.CENTER);
        payDialog.add(confirmPayBtn, BorderLayout.SOUTH);
        payDialog.setLocationRelativeTo(this);
        payDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Seat::new);
    }
}
