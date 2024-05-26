package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ChangeInfoUI extends JFrame {

    private JPanel contentPane;
    private JTextField passwordTextField; // 패스워드 입력칸
    private JTextField dobTextField; // 생년월일 입력칸
    private JTextField emailTextField;  // 이메일 입력칸
    private JLabel lblEnterNewPassword; // 수정할 비밀번
    private JLabel lblEnterNewDOB; // 수정할 생년월일
    private JLabel lblEnterNewEmail;  // 수정할 이메일

    public ChangeInfoUI(String name) {
        setBounds(500, 120, 400, 350);  // 높이를 늘림
        setResizable(false);

        // 전체 패널
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 비밀번호
        lblEnterNewPassword = new JLabel("비밀번호 수정:");
        lblEnterNewPassword.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEnterNewPassword.setForeground(new Color(0, 0, 0));
        lblEnterNewPassword.setBounds(34, 32, 72, 27);
        contentPane.add(lblEnterNewPassword);

        passwordTextField = new JTextField();
        passwordTextField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));
        passwordTextField.setBounds(140, 30, 200, 33);
        contentPane.add(passwordTextField);
        passwordTextField.setColumns(10);

        //이메일
        lblEnterNewEmail = new JLabel("이메일 수정:");  // 추가된 부분
        lblEnterNewEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEnterNewEmail.setForeground(new Color(0, 0, 0));
        lblEnterNewEmail.setBounds(34, 82, 72, 27);
        contentPane.add(lblEnterNewEmail);

        emailTextField = new JTextField();
        emailTextField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));  // 추가된 부분
        emailTextField.setBounds(140, 80, 200, 33);  // 추가된 부분
        contentPane.add(emailTextField);
        emailTextField.setColumns(10);

        // 생년월일
        lblEnterNewDOB = new JLabel("생년월일 수정:");
        lblEnterNewDOB.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEnterNewDOB.setForeground(new Color(0, 0, 0));
        lblEnterNewDOB.setBounds(34, 132, 72, 27);
        contentPane.add(lblEnterNewDOB);

        dobTextField = new JTextField();
        dobTextField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));
        dobTextField.setBounds(140, 130, 200, 33);
        contentPane.add(dobTextField);
        dobTextField.setColumns(10);


        // 버튼
        JButton btnChangeInfo = new JButton("변경하기");
        btnChangeInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = passwordTextField.getText();
                String newDOB = dobTextField.getText();
                String newEmail = emailTextField.getText();

                try {
                    Connection con = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);

                    // 비밀번호 변경
                    if (!newPassword.isEmpty()) {
                        PreparedStatement stPassword = con.prepareStatement("UPDATE account SET password=? WHERE ID=?");
                        stPassword.setString(1, newPassword);
                        stPassword.setString(2, name);
                        stPassword.executeUpdate();
                    }

                    // 생년월일이 비어있지 않을 때 형식 확인
                    if (!newDOB.isEmpty() && !isValidDateFormat(newDOB)) {
                        JOptionPane.showMessageDialog(btnChangeInfo, "올바른 생년월일 형식이 아닙니다. (yyyy-MM-dd) 형식으로 입력해주세요.", "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
                        return; // 생년월일 형식이 올바르지 않으면 메소드를 종료하고 창을 닫지 않음
                    }

                    // 생년월일 변경
                    if (!newDOB.isEmpty()) {
                        PreparedStatement stDOB = con.prepareStatement("UPDATE account SET birthdate=? WHERE ID=?");
                        stDOB.setString(1, newDOB);
                        stDOB.setString(2, name);
                        stDOB.executeUpdate();
                    }

                    // 이메일이 비어있지 않을 때 이메일 형식 확인
                    if (!newEmail.isEmpty() && !isValidEmail(newEmail)) {
                        JOptionPane.showMessageDialog(btnChangeInfo, "올바른 이메일 형식이 아닙니다. 재입력해주세요.", "Invalid Email Format", JOptionPane.ERROR_MESSAGE);
                        return; // 이메일 형식이 올바르지 않으면 메소드를 종료하고 창을 닫지 않음
                    }

                    // 이메일 변경
                    if (!newEmail.isEmpty()) {
                        PreparedStatement stEmail = con.prepareStatement("UPDATE account SET email=? WHERE ID=?");
                        stEmail.setString(1, newEmail);
                        stEmail.setString(2, name);
                        stEmail.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(btnChangeInfo, "정보가 변경되었습니다.", "Information changed", JOptionPane.PLAIN_MESSAGE);
                    dispose();

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });

        btnChangeInfo.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        btnChangeInfo.setBackground(new Color(221, 221, 221));
        btnChangeInfo.setForeground(new Color(31, 31, 31));
        btnChangeInfo.setBounds(100, 250, 200, 33);  // 높이를 늘림
        contentPane.add(btnChangeInfo);

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private boolean isValidDateFormat(String date) {
        try {
            java.sql.Date.valueOf(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // 이메일 형식 확인 메소드
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
}
