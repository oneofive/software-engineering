package ui;

import MVC.Controller;
import MVC.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchUI extends JFrame {

    private JTextField idField;
    private String myUserID; // 내 userID를 저장하는 변수

    public SearchUI(String myUserID) {
        this.myUserID = myUserID; // 생성자에서 내 userID를 받아서 저장
        setBounds(550, 120, 300, 150);
        setTitle("사용자 검색");
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel idLabel = new JLabel("검색할 ID: ");
        idField = new JTextField(15);

        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedId = idField.getText().trim();
                if (!searchedId.isEmpty()) {
                    // 사용자가 존재하는지 확인하는 로직 추가
                    if (userExists(searchedId)) {
                        // 사용자가 존재하면 해당 사용자의 피드로 이동
                        goToUserFeed(searchedId);
                    } else {
                        JOptionPane.showMessageDialog(SearchUI.this, "존재하지 않는 ID입니다.", "알림", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(SearchUI.this, "검색할 ID를 입력하세요.", "알림", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(searchButton);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private boolean userExists(String userId) {
        Controller controller = new Controller();
        return controller.userExists(userId);
    }

    private void goToUserFeed(String userId) {
        dispose();  // 현재 창 닫기
        System.out.println("검색된 사용자 " + userId + "의 피드로 이동합니다.");

        // 해당 사용자의 피드를 보여주는 UI를 생성하고 표시
        OtherBoardUI otherBoardUI = new OtherBoardUI(userId, myUserID); // 내 userID 전달
        otherBoardUI.setVisible(true);
    }
}
