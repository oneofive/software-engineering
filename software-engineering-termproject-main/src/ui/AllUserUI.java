package ui;

import MVC.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AllUserUI extends JFrame {

    private static final String FEED_BUTTON_TEXT = " 방문하기";

    private String id;
    private Controller controller = new Controller();

    public AllUserUI(String id) {
        this.id = id;
        setBounds(500, 120, 400, 300);
        setTitle("User List");
        getContentPane().setLayout(new BorderLayout());
        setResizable(false);

        ArrayList<String> allUserList = controller.getAllUser(id);
        JPanel userPanel = createUserPanel(allUserList);

        getContentPane().add(userPanel, BorderLayout.NORTH);
        setVisible(true);

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        button.addActionListener(actionListener);

        return button;
    }

    private JPanel createUserPanel(List<String> userList) {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(userList.size(), 4, 10, 10));

        for (String userId : userList) {

            JLabel userIdLabel = new JLabel("         " + userId);
            JButton userBoardButton = createButton(userId + FEED_BUTTON_TEXT, e -> openUserBoard(userId));

            userPanel.add(userIdLabel);
            userPanel.add(userBoardButton);
        }

        return userPanel;
    }

    private void openUserBoard(String userId) {
        dispose();
        System.out.println(userId + "'피드 방문 하기");
        OtherBoardUI otherBoardUI = new OtherBoardUI(userId, id);
        otherBoardUI.setVisible(true);
    }

}
