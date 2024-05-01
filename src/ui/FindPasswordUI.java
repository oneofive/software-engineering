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

public class FindPasswordUI extends JFrame {

    private JPanel contentPane;
    private JTextField idField;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JTextField birthdateField;

    private static final String SELECT_PASSWORD_QUERY = "SELECT password FROM account WHERE id=? AND firstname=? AND lastname=? AND email=? AND birthdate=?";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE account SET password=? WHERE id=?";

    // 클래스 시작
    public FindPasswordUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500, 80, 400, 500);
        setResizable(false);

        // 패널
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 비밀번호 찾기 라벨
        JLabel lblFindPassword = new JLabel("비밀번호 찾기");
        lblFindPassword.setHorizontalAlignment(SwingConstants.CENTER);
        lblFindPassword.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
        lblFindPassword.setForeground(new Color(0, 0, 0));
        lblFindPassword.setBounds(14, 50, 373, 50);
        contentPane.add(lblFindPassword);

        // 아이디
        JLabel lblId = new JLabel("아이디 / ID");
        lblId.setForeground(new Color(0, 0, 0));
        lblId.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblId.setBounds(72, 120, 100, 27);
        contentPane.add(lblId);

        idField = new JTextField();
        idField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        idField.setBounds(69, 140, 262, 33);
        contentPane.add(idField);
        idField.setColumns(10);

        // 이름
        JLabel lblFirstName = new JLabel("이름 / First Name");
        lblFirstName.setForeground(new Color(0, 0, 0));
        lblFirstName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblFirstName.setBounds(72, 170, 130, 27);
        contentPane.add(lblFirstName);

        firstnameField = new JTextField();
        firstnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        firstnameField.setBounds(69, 190, 262, 33);
        contentPane.add(firstnameField);
        firstnameField.setColumns(10);

        // 성
        JLabel lblLastName = new JLabel("성 / Last Name");
        lblLastName.setForeground(new Color(0, 0, 0));
        lblLastName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblLastName.setBounds(72, 220, 130, 27);
        contentPane.add(lblLastName);

        lastnameField = new JTextField();
        lastnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        lastnameField.setBounds(69, 240, 262, 33);
        contentPane.add(lastnameField);
        lastnameField.setColumns(10);


        // 이메일
        JLabel lblEmail = new JLabel("이메일 / Email");
        lblEmail.setForeground(new Color(0, 0, 0));
        lblEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEmail.setBounds(72, 270, 100, 27);
        contentPane.add(lblEmail);

        emailField = new JTextField();
        emailField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        emailField.setBounds(69, 290, 262, 33);
        contentPane.add(emailField);
        emailField.setColumns(10);

        // 생년월일
        JLabel lblBirthdate = new JLabel("생년월일 / Birthdate");
        lblBirthdate.setForeground(new Color(0, 0, 0));
        lblBirthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblBirthdate.setBounds(72, 320, 130, 27);
        contentPane.add(lblBirthdate);

        birthdateField = new JTextField();
        birthdateField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        birthdateField.setBounds(69, 340, 262, 33);
        contentPane.add(birthdateField);
        birthdateField.setColumns(10);

        // 비밀번호 재설정
        JButton findPasswordButton = new JButton("비밀번호 재설정하기");
        findPasswordButton.setBackground(new Color(197, 225, 255));
        findPasswordButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        findPasswordButton.setBounds(69, 400, 262, 40);
        contentPane.add(findPasswordButton);

        findPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String firstname = firstnameField.getText();
                String lastname = lastnameField.getText();
                String email = emailField.getText();
                String birthdate = birthdateField.getText();

                try {
                    Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);

                    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PASSWORD_QUERY)) {
                        preparedStatement.setString(1, id);
                        preparedStatement.setString(2, firstname);
                        preparedStatement.setString(3, lastname);
                        preparedStatement.setString(4, email);
                        preparedStatement.setString(5, birthdate);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String foundPassword = resultSet.getString("password");

                            String maskedPassword = maskHalfPassword(foundPassword);
                            JOptionPane.showMessageDialog(FindPasswordUI.this, "찾은 비밀번호: " + maskedPassword, "비밀번호 찾기", JOptionPane.INFORMATION_MESSAGE);

                            int option = JOptionPane.showConfirmDialog(FindPasswordUI.this, "비밀번호를 재설정하시겠습니까?", "비밀번호 재설정", JOptionPane.YES_NO_OPTION);

                            if (option == JOptionPane.YES_OPTION) {
                                // 여기서 newPassword를 정의
                                String newPassword = JOptionPane.showInputDialog(FindPasswordUI.this, "새로운 비밀번호를 입력하세요:");

                                // Check if the entered password is not empty and meets the minimum length requirement
                                if (isValidPassword(newPassword)) {
                                    resetPassword(id, newPassword);
                                    JOptionPane.showMessageDialog(FindPasswordUI.this, "비밀번호가 재설정되었습니다.", "비밀번호 재설정", JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(FindPasswordUI.this, "비밀번호는 최소 5글자 이상이어야 합니다.", "비밀번호 재설정 실패", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(FindPasswordUI.this, "일치하는 정보를 찾을 수 없습니다.", "비밀번호 찾기", JOptionPane.WARNING_MESSAGE);
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

    private String maskHalfPassword(String password) {
        int length = password.length();
        int halfLength = length / 2;
        String maskedPart = new String(new char[halfLength]).replace('\0', '*');
        return password.substring(0, halfLength) + maskedPart;
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 5;
    }

    private void resetPassword(String id, String newPassword) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);

            try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PASSWORD_QUERY)) {
                updateStatement.setString(1, newPassword);
                updateStatement.setString(2, id);
                updateStatement.executeUpdate();
            }

            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
