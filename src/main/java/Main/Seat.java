package Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Seat extends JFrame {
    // ระบบ Inventory จำลองหลังบ้าน
    private Map<String, Integer> seatInventory = new HashMap<>();
    private final int MAX_A = 20; // ทิศละ 20 (รวม 80)
    private final int MAX_B = 30; // ทิศละ 30 (รวม 120)
    private final int MAX_C = 30; // ทิศละ 30 (รวม 120)

    private List<String> cartItems = new ArrayList<>();
    private double totalAmount = 0;
    private JLabel statusLabel;

    public Seat() {
        initInventory(); // โหลดข้อมูลที่นั่งว่าง
        
        setTitle("KKU ARENA - UNLIMITED BOOKING (RED-BLACK)");
        setSize(950, 950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // แผงหลัก (Grid 7x7)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.BLACK); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new Insets(3, 3, 3, 3);

        // สีธีมดุดัน
        Color colorArena = new Color(180, 0, 0);      
        Color colorA = new Color(80, 0, 0);    
        Color colorB = new Color(120, 0, 0);   
        Color colorC = new Color(160, 0, 0);   

        // วาดผังสี่เหลี่ยมซ้อนทิศทาง 1-4
        addZone(mainPanel, gbc, 0, 0, 7, 1, "C1", colorC, 200);
        addZone(mainPanel, gbc, 1, 1, 5, 1, "B1", colorB, 500);
        addZone(mainPanel, gbc, 2, 2, 3, 1, "A1", colorA, 1000);

        addZone(mainPanel, gbc, 0, 1, 1, 5, "C4", colorC, 200);
        addZone(mainPanel, gbc, 1, 2, 1, 3, "B4", colorB, 500);
        addZone(mainPanel, gbc, 2, 3, 1, 1, "A4", colorA, 1000);
        
        // ARENA (Center)
        JLabel arena = new JLabel("ARENA", SwingConstants.CENTER);
        arena.setOpaque(true);
        arena.setBackground(Color.BLACK);
        arena.setForeground(colorArena);
        arena.setFont(new Font("Impact", Font.BOLD, 35));
        arena.setBorder(BorderFactory.createLineBorder(colorArena, 3));
        gbc.gridx = 3; gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridheight = 1;
        mainPanel.add(arena, gbc);
        
        addZone(mainPanel, gbc, 4, 3, 1, 1, "A2", colorA, 1000);
        addZone(mainPanel, gbc, 5, 2, 1, 3, "B2", colorB, 500);
        addZone(mainPanel, gbc, 6, 1, 1, 5, "C2", colorC, 200);

        addZone(mainPanel, gbc, 2, 4, 3, 1, "A3", colorA, 1000);
        addZone(mainPanel, gbc, 1, 5, 5, 1, "B3", colorB, 500);
        addZone(mainPanel, gbc, 0, 6, 7, 1, "C3", colorC, 200);

        // แถบสรุปผลด้านล่าง
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.setPreferredSize(new Dimension(0, 100));

        statusLabel = new JLabel(" ยังไม่ได้เลือกที่นั่ง | ยอดรวม: 0 บาท", JLabel.LEFT);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        JButton checkoutBtn = new JButton("ยืนยันชำระเงินทั้งหมด");
        checkoutBtn.setBackground(colorArena);
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        checkoutBtn.addActionListener(e -> processCheckout());

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(checkoutBtn, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initInventory() {
        String[] zones = {"A1","A2","A3","A4","B1","B2","B3","B4","C1","C2","C3","C4"};
        for (String z : zones) {
            if (z.startsWith("A")) seatInventory.put(z, MAX_A);
            else seatInventory.put(z, MAX_B); // B และ C ทิศละ 30
        }
        // จำลอง: มีคนจอง A1 ไปแล้ว 5 ที่นั่ง
        seatInventory.put("A1", 15);
    }

    private void addZone(JPanel panel, GridBagConstraints gbc, int x, int y, int w, int h, String name, Color bg, double price) {
        gbc.gridx = x; gbc.gridy = y;
        gbc.gridwidth = w; gbc.gridheight = h;

        JButton btn = new JButton();
        updateButtonText(btn, name);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        btn.addActionListener(e -> showCounterDialog(name, price, btn));
        panel.add(btn, gbc);
    }

    private void updateButtonText(JButton btn, String name) {
        int left = seatInventory.get(name);
        btn.setText("<html><center>" + name + "<br><font size='3'>ว่าง: " + left + "</font></center></html>");
        if (left <= 0) {
            btn.setEnabled(false);
            btn.setBackground(Color.DARK_GRAY);
        }
    }

    // --- หน้าต่าง Pop-up เลือกจำนวนที่นั่งแบบ + / - ---
    private void showCounterDialog(String zoneName, double price, JButton parentBtn) {
        int available = seatInventory.get(zoneName);
        
        JDialog dialog = new JDialog(this, "ระบุจำนวน - โซน " + zoneName, true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(30, 30, 30));
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        final int[] count = {1}; // จำนวนเริ่มต้น
        JLabel countLabel = new JLabel(" " + count[0] + " ", SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 30));
        countLabel.setForeground(Color.WHITE);

        JButton minusBtn = new JButton("-");
        JButton plusBtn = new JButton("+");
        JButton addCartBtn = new JButton("เพิ่มลงตะกร้า");

        plusBtn.addActionListener(e -> {
            if (count[0] < available) {
                count[0]++;
                countLabel.setText(" " + count[0] + " ");
            } else {
                JOptionPane.showMessageDialog(dialog, "ที่นั่งในโซนนี้เหลือเพียง " + available + " ที่เท่านั้น!");
            }
        });

        minusBtn.addActionListener(e -> {
            if (count[0] > 1) {
                count[0]--;
                countLabel.setText(" " + count[0] + " ");
            }
        });

        addCartBtn.addActionListener(e -> {
            // ตัดสต็อกหลังบ้าน
            seatInventory.put(zoneName, available - count[0]);
            
            // เพิ่มลงตะกร้าจำลอง
            cartItems.add(zoneName + " (x" + count[0] + ")");
            totalAmount += (price * count[0]);
            
            updateButtonText(parentBtn, zoneName);
            statusLabel.setText(" ตะกร้า: " + cartItems.toString() + " | ยอดรวม: " + totalAmount + " บาท");
            
            dialog.dispose();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0; c.gridy = 0; dialog.add(minusBtn, c);
        c.gridx = 1; dialog.add(countLabel, c);
        c.gridx = 2; dialog.add(plusBtn, c);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 3; c.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(addCartBtn, c);

        dialog.setVisible(true);
    }

    private void processCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกที่นั่งก่อนชำระเงิน");
            return;
        }
        
        String msg = "สรุปรายการจอง:\n" + String.join("\n", cartItems) + 
                     "\n\nยอดรวมสุทธิ: " + totalAmount + " บาท\nกรุณาชำระเงินและรอ Admin อนุมัติ";
        
        JOptionPane.showMessageDialog(this, msg, "PAYMENT SUMMARY", JOptionPane.INFORMATION_MESSAGE);
        
        // Reset ตะกร้าสำหรับคนถัดไป (หรือการสั่งซื้อใหม่)
        cartItems.clear();
        totalAmount = 0;
        statusLabel.setText(" ยังไม่ได้เลือกที่นั่ง | ยอดรวม: 0 บาท");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Seat::new);
    }
}
