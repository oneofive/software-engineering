package ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;


public class RegistrationUI extends JFrame {

    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField id;
    private JPasswordField passwordField;
    private JButton RegisterButton;
    private JButton findMyAccountButton;
    private JRadioButton rdbtnMale;
    private JRadioButton rdbtnFemale;
    private JTextField email;
    private JTextField birthdate;

    //버튼 디자인
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
    // 회원가입 클래스
    public RegistrationUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);
        setResizable(false);

        // 패널 구성
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 라벨
        JLabel UserRegister = new JLabel("Goal Buddy 회원가입");
        UserRegister.setHorizontalAlignment(SwingConstants.CENTER);

        UserRegister.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
        UserRegister.setForeground(new Color(0, 0, 0));
        UserRegister.setBounds(14, 50, 373, 50);
        contentPane.add(UserRegister);

        // 이름
        JLabel FirstNameLabel = new JLabel("이름 / First Name");
        FirstNameLabel.setForeground(new Color(0, 0, 0));
        FirstNameLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        FirstNameLabel.setBounds(72, 120, 100, 27);
        contentPane.add(FirstNameLabel);

        firstname = new JTextField();
        firstname.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        firstname.setBounds(69, 140, 262, 33);
        contentPane.add(firstname);
        firstname.setColumns(10); // 최대 문자 10

        // 성
        JLabel lblNewLabel = new JLabel("성 / Last Name");
        lblNewLabel.setForeground(new Color(0, 0, 0));
        lblNewLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblNewLabel.setBounds(72, 170, 100, 27);
        contentPane.add(lblNewLabel);

        lastname = new JTextField();
        lastname.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        lastname.setBounds(69, 190, 262, 33);
        contentPane.add(lastname);
        lastname.setColumns(10);

        // 아이디
        JLabel UserIdLabel = new JLabel("아이디 / ID");
        UserIdLabel.setForeground(new Color(0, 0, 0));
        UserIdLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        UserIdLabel.setBounds(72, 220, 78, 27);
        contentPane.add(UserIdLabel);

        id = new JTextField();
        id.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        id.setBounds(69, 240, 262, 33);
        contentPane.add(id);
        id.setColumns(10);

        // 비밀번호
        JLabel lblPassword = new JLabel("비밀번호 / Password (최소 5글자 이상)");
        lblPassword.setForeground(new Color(0, 0, 0));
        lblPassword.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblPassword.setBounds(72, 270, 130, 27);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        passwordField.setBounds(69, 290, 262, 33);
        contentPane.add(passwordField);

        // 성별 선택 라디오 버튼
        JLabel sexLabel = new JLabel("성별 선택");
        sexLabel.setForeground(new Color(0, 0, 0));
        sexLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        sexLabel.setBounds(72, 320, 130, 27);
        contentPane.add(sexLabel);

        rdbtnMale = new JRadioButton("남성");
        rdbtnFemale = new JRadioButton("여성");
        ButtonGroup genderGroup = new ButtonGroup();
        contentPane.add(sexLabel);
        genderGroup.add(rdbtnMale);
        genderGroup.add(rdbtnFemale);

        rdbtnMale.setForeground(new Color(0, 0, 0));
        rdbtnMale.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        rdbtnMale.setBounds(69, 345, 65, 23);
        contentPane.add(rdbtnMale);

        rdbtnFemale.setForeground(new Color(0, 0, 0));
        rdbtnFemale.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        rdbtnFemale.setBounds(146, 345, 65, 23);
        contentPane.add(rdbtnFemale);

        // 이메일
        JLabel lblEmail = new JLabel("이메일 / Email");
        lblEmail.setForeground(new Color(0, 0, 0));
        lblEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEmail.setBounds(72, 370, 100, 27);
        contentPane.add(lblEmail);

        email = new JTextField();
        email.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        email.setBounds(69, 390, 262, 33);
        contentPane.add(email);
        email.setColumns(10);

        // 생년월일
        JLabel lblBirthdate = new JLabel("생년월일 / Birthdate (HHH-MM-DD)");
        lblBirthdate.setForeground(new Color(0, 0, 0));
        lblBirthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblBirthdate.setBounds(72, 420, 130, 27);
        contentPane.add(lblBirthdate);

        birthdate = new JTextField();
        birthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        birthdate.setBounds(69, 440, 262, 33);
        contentPane.add(birthdate);
        birthdate.setColumns(10);

        // 가입하기 버튼
        RegisterButton = new RoundedButton("가입하기");
        RegisterButton.setBackground(new Color(0, 0, 0));
        RegisterButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        RegisterButton.setBounds(69, 510, 262, 40);
        contentPane.add(RegisterButton);

        // 아이디/비밀번호 찾기
        findMyAccountButton = new RoundedButton("아이디 / 비밀번호 찾기");
        findMyAccountButton.setBackground(new Color(0, 0, 0));
        findMyAccountButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        findMyAccountButton.setBounds(69, 570, 262, 40);
        contentPane.add(findMyAccountButton);

        findMyAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();

                // 로그인 화면으로
                LoginUI ul = new LoginUI();
                ul.setTitle("Login");
                ul.setVisible(true);

                // 다이얼로그 상자를 통해 아이디 찾기 또는 비밀번호 찾기 선택 받기
                String[] options = {"아이디 찾기", "비밀번호 찾기"};
                int choice = JOptionPane.showOptionDialog(null, "아이디 찾기 또는 비밀번호 찾기를 선택하세요.", "찾기 선택", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    // 아이디 찾기 선택 시
                    FindIDUI findIDUI = new FindIDUI();
                    findIDUI.setTitle("Find ID");
                    findIDUI.setVisible(true);
                    findIDUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                } else if (choice == 1) {
                    // 비밀번호 찾기 선택 시
                    FindPasswordUI findPasswordUI = new FindPasswordUI();
                    findPasswordUI.setTitle("Find Password");
                    findPasswordUI.setVisible(true);
                    findPasswordUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            }
        });

        // 뒤로가기
        JButton BacktoMain = new JButton(new ImageIcon(getClass().getResource("/img/backArrow.png")));
        BacktoMain.setBounds(30, 25, 30, 30);
        contentPane.add(BacktoMain);
        BacktoMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // main으로 이동
                dispose();
                LoginUI main = new LoginUI();
                main.setTitle("Login");
                main.setVisible(true);
            }
        });

        // 로고
//        JLabel RegiLogo = new JLabel("");
//        Image img = new ImageIcon(this.getClass().getResource("/img/registrationIcon.png")).getImage();
//        RegiLogo.setIcon(new ImageIcon(img));
//        RegiLogo.setBounds(175, 10, 50, 50);
//        contentPane.add(RegiLogo);

        // 회원가입하기
        RegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String ID = id.getText();
                String password = passwordField.getText();
                String gender = rdbtnMale.isSelected() ? "남성" : "여성";
                String userEmail = email.getText();
                String userBirthdate = birthdate.getText();

                int firstName_len = firstName.length();
                int lastName_len = lastName.length();
                int ID_len = ID.length();
                int password_len = password.length();

                String msg = "" + ID;
                msg += " \n";

                if (firstName_len == 0) {
                    JOptionPane.showMessageDialog(RegisterButton, "이름을 입력해주세요.", "Invalid First Name", JOptionPane.PLAIN_MESSAGE);
                } else if (lastName_len == 0) {
                    JOptionPane.showMessageDialog(RegisterButton, "성(Last Name)을 입력해주세요.", "Invalid Last Name", JOptionPane.PLAIN_MESSAGE);
                } else if (ID_len < 5) {
                    JOptionPane.showMessageDialog(RegisterButton, "아이디는 최소 5글자 이상이어야 합니다.", "Invalid Username", JOptionPane.PLAIN_MESSAGE);
                } else if (password_len < 5) {
                    JOptionPane.showMessageDialog(RegisterButton, "비밀번호는 최소 5글자 이상이어야 합니다.", "Invalid Password", JOptionPane.PLAIN_MESSAGE);
                } else if (!userEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(RegisterButton, "올바른 이메일 양식을 입력해주세요.", "Invalid Email", JOptionPane.PLAIN_MESSAGE);
                    // 생년월일 형식 검증
                } else if (!userBirthdate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    JOptionPane.showMessageDialog(RegisterButton, "올바른 생년월일 양식을 입력해주세요. (올바른 양식 : HHHH - MM -DD)", "Invalid Birthdate", JOptionPane.PLAIN_MESSAGE);
                } else {
                    try {
                        // 상수로 정의한 DB 연결 정보 사용
                        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD)) {

                            // 이미 존재하는 계정인지 확인
                            String checkQuery = "SELECT * FROM account WHERE id = ?";
                            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                                checkStatement.setString(1, ID);

                                try (ResultSet resultSet = checkStatement.executeQuery()) {
                                    if (resultSet.next()) {
                                        JOptionPane.showMessageDialog(RegisterButton, "이미 존재하는 계정입니다.");
                                    } else {
                                        // 계정 추가
                                        String insertQuery = "INSERT INTO account (firstname, lastname, id, password, birthdate, email, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                            insertStatement.setString(1, firstName);
                                            insertStatement.setString(2, lastName);
                                            insertStatement.setString(3, ID);
                                            insertStatement.setString(4, password);
                                            insertStatement.setString(5, userBirthdate);
                                            insertStatement.setString(6, userEmail);
                                            insertStatement.setString(7, gender);

                                            int x = insertStatement.executeUpdate();

                                            if (x > 0) {
                                                JOptionPane.showMessageDialog(RegisterButton, "반갑습니다, " + msg + "계정이 성공적으로 생성되었습니다.");
                                                dispose();
                                                LoginUI ul = new LoginUI();
                                                ul.setTitle("Login");
                                                ul.setVisible(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }
}
