package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindIDUI extends JFrame {

    private JPanel contentPane;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JTextField birthdateField;

    public FindIDUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500, 80, 400, 450);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFindID = new JLabel("아이디 찾기");
        lblFindID.setHorizontalAlignment(SwingConstants.CENTER);
        lblFindID.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
        lblFindID.setForeground(new Color(0, 0, 0));
        lblFindID.setBounds(14, 50, 373, 50);
        contentPane.add(lblFindID);

        // 이름
        JLabel lblFirstName = new JLabel("이름 / First Name");
        lblFirstName.setForeground(new Color(0, 0, 0));
        lblFirstName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblFirstName.setBounds(72, 120, 130, 27);
        contentPane.add(lblFirstName);

        firstnameField = new JTextField();
        firstnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        firstnameField.setBounds(69, 140, 262, 33);
        contentPane.add(firstnameField);
        firstnameField.setColumns(10);

        // 성
        JLabel lblLastName = new JLabel("성 / Last Name");
        lblLastName.setForeground(new Color(0, 0, 0));
        lblLastName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblLastName.setBounds(72, 170, 130, 27);
        contentPane.add(lblLastName);

        lastnameField = new JTextField();
        lastnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        lastnameField.setBounds(69, 190, 262, 33);
        contentPane.add(lastnameField);
        lastnameField.setColumns(10);

        // 이메일
        JLabel lblEmail = new JLabel("이메일 / Email");
        lblEmail.setForeground(new Color(0, 0, 0));
        lblEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEmail.setBounds(72, 220, 100, 27);
        contentPane.add(lblEmail);

        emailField = new JTextField();
        emailField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        emailField.setBounds(69, 240, 262, 33);
        contentPane.add(emailField);
        emailField.setColumns(10);

        // 생년월일
        JLabel lblBirthdate = new JLabel("생년월일 / Birthdate");
        lblBirthdate.setForeground(new Color(0, 0, 0));
        lblBirthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblBirthdate.setBounds(72, 270, 130, 27);
        contentPane.add(lblBirthdate);

        birthdateField = new JTextField();
        birthdateField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        birthdateField.setBounds(69, 290, 262, 33);
        contentPane.add(birthdateField);
        birthdateField.setColumns(10);

        // 아이디 검색하기
        JButton findIDButton = new JButton("아이디 검색하기");
        findIDButton.setBackground(new Color(197, 225, 255));
        findIDButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        findIDButton.setBounds(69, 350, 262, 40);
        findIDButton.setForeground(new Color(0, 0, 0));
        contentPane.add(findIDButton);

        findIDButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstname = firstnameField.getText();
                String lastname = lastnameField.getText();
                String email = emailField.getText();
                String birthdate = birthdateField.getText();

                try {
                    // 상수로 정의한 DB 연결 정보 사용
                    Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);

                    String query = "SELECT id FROM account WHERE firstname=? AND lastname=? AND email=? AND birthdate=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, firstname);
                        preparedStatement.setString(2, lastname);
                        preparedStatement.setString(3, email);
                        preparedStatement.setString(4, birthdate);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String foundID = resultSet.getString("id");

                            // Display found ID in a dialog
                            JOptionPane.showMessageDialog(FindIDUI.this, "찾은 아이디: " + foundID, "아이디 찾기", JOptionPane.INFORMATION_MESSAGE);
                            setLocationRelativeTo(null);
                        } else {
                            // Display a message if no matching record is found
                            JOptionPane.showMessageDialog(FindIDUI.this, "일치하는 정보를 찾을 수 없습니다.", "아이디 찾기", JOptionPane.WARNING_MESSAGE);
                            setLocationRelativeTo(null);
                        }
                    }
                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });
        setLocationRelativeTo(null);
    }
}
