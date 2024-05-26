package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;

public class  LoginUI extends JFrame {

    private JTextField textField;
    private JPasswordField passwordField;
    private JButton LoginButton;
    private JButton RegisterButton;
    private JPanel contentPane;

    //라운드 버튼 디자인
    public class RoundedButton extends JButton {
        public RoundedButton(String text) { super(text); decorate(); }
        protected void decorate() { setBorderPainted(false); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Color c=new Color(239,243,244); //배경색 결정
            Color o=new Color(15,20,25); //글자색 결정
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) { graphics.setColor(c.darker()); }
            else if (getModel().isRollover()) { graphics.setColor(c.brighter()); }
            else { graphics.setColor(c); }
            graphics.fillRoundRect(0, 0, width, height, 40, 40);
            FontMetrics fontMetrics = graphics.getFontMetrics();
            Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
            int textX = (width - stringBounds.width) / 2;
            int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
            graphics.setColor(o);
            graphics.setFont(getFont());
            graphics.drawString(getText(), textX, textY);
            graphics.dispose();
            super.paintComponent(g);
        }
    }

    // 로그인
    public LoginUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);
        setResizable(false);

        // 기본 패널
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 라벨
        JLabel HomeLabel = new JLabel("Goal Buddy");
        HomeLabel.setBounds(14, 199, 373, 93);
        HomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        HomeLabel.setForeground(new Color(0, 0, 0));
        HomeLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 30));
        contentPane.add(HomeLabel);

        // 아이디 입력 창
        JLabel lblUsername = new JLabel("사용자 아이디");
        lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsername.setBounds(66, 316, 76, 27);
        lblUsername.setForeground(new Color(0, 0, 0));
        lblUsername.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        contentPane.add(lblUsername);

        textField = new JTextField();
        textField.setBounds(69, 343, 262, 33);
        textField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        contentPane.add(textField);
        textField.setColumns(10);

        // 비밀번호 입력
        JLabel lblPassword = new JLabel("비밀번호");
        lblPassword.setBounds(71, 378, 45, 27);
        lblPassword.setForeground(new Color(0, 0, 0));
        lblPassword.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(69, 403, 262, 33);
        passwordField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        contentPane.add(passwordField);

        // 로그인 버튼
        LoginButton = new RoundedButton("로그인하기");
        LoginButton.setBackground(new Color(0, 0, 0));
        LoginButton.setForeground(new Color(0, 0, 0));
        LoginButton.setBounds(69, 474, 262, 40);
        LoginButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        contentPane.add(LoginButton);

        LoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = textField.getText();
                String password = passwordField.getText();
                try {
                    // 상수로 정의한 DB 연결 정보 사용
                    try (Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD)) {
                        // 수정된 부분: PreparedStatement를 사용하고 try-with-resources 사용
                        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, password FROM account WHERE id=? AND password=?")) {
                            preparedStatement.setString(1, id);
                            preparedStatement.setString(2, password);

                            try (ResultSet rs = preparedStatement.executeQuery()) {
                                if (rs.next()) {
                                    dispose();
                                    MainBoardUI main = new MainBoardUI(id);
                                    main.setTitle("GoalBuddy");
                                    main.setVisible(true);
                                    // 수정된 부분: 다이얼로그의 부모로 null을 사용하여 화면 중앙에 표시
                                    JOptionPane.showMessageDialog(null, "환영합니다", "Login", JOptionPane.PLAIN_MESSAGE);
                                } else {
                                    // 수정된 부분: 다이얼로그의 부모로 null을 사용하여 화면 중앙에 표시
                                    JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 틀렸습니다", "Failed to Login", JOptionPane.PLAIN_MESSAGE);
                                }
                            }
                        }
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });

        // 회원가입 버튼
        RegisterButton = new RoundedButton("새로 가입하기" );
        RegisterButton.setForeground(new Color(0, 0, 0));
        RegisterButton.setBounds(69, 522, 262, 40);
        RegisterButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegistrationUI ur = new RegistrationUI();
                ur.setTitle("Register");
                ur.setVisible(true);
            }
        });

        contentPane.add(RegisterButton);

        // 로고 사진
//        JLabel LoginLogo = new JLabel("");
//        Image  = new ImageIcon(this.getClass().getResource("/img/loginLogo.png")).getImage();
//        LoginLogo.setIcon(new ImageIcon(img));
//        LoginLogo.setBounds(145, 85, 110, 110);
//        contentPane.add(LoginLogo);

        setLocationRelativeTo(null);
    }
}
