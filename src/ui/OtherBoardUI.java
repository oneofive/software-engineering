package ui;

import MVC.Controller;
import MVC.Post;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class OtherBoardUI extends JFrame {

    private String otherUserID;
    private String userID;
    private JPanel contentPane;
    private DefaultListModel listModel;
    private JList list;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel genderLabel;
    private JLabel birthdateLabel;

    private Controller controller;

    public OtherBoardUI(String otherUserID, String userID) {
        this.otherUserID = otherUserID;
        this.userID = userID;
        controller = new Controller();

        setSize(500, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel userIdLabel = new JLabel(otherUserID + "님의 피드");
        userIdLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
        userIdLabel.setForeground(new Color(0, 0, 0));
        userIdLabel.setBounds(40, 130, 200, 30);
        contentPane.add(userIdLabel);

        nameLabel = new JLabel("이름: ");
        nameLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        nameLabel.setForeground(new Color(254, 255, 255));
        nameLabel.setBounds(40, 170, 320, 20);
        contentPane.add(nameLabel);

        emailLabel = new JLabel("이메일: ");
        emailLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(254, 255, 255));
        emailLabel.setBounds(40, 190, 320, 20);
        contentPane.add(emailLabel);

        genderLabel = new JLabel("성별: ");
        genderLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        genderLabel.setForeground(new Color(254, 255, 255));
        genderLabel.setBounds(40, 210, 320, 20);
        contentPane.add(genderLabel);

        birthdateLabel = new JLabel("생년월일: ");
        birthdateLabel.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        birthdateLabel.setForeground(new Color(254, 255, 255));
        birthdateLabel.setBounds(40, 230, 320, 20);
        contentPane.add(birthdateLabel);

        list = new JList();
        list.setBounds(40, 270, 320, 330);
        list.setForeground(new Color(254, 255, 255));
        list.setBackground(new Color(20, 20, 20));
        contentPane.add(list);

        JButton commentWriteButton = new JButton("댓글 작성하기");
        commentWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentWriteButton.setBounds(145, 630, 100, 30);
        commentWriteButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentWriteButton);

        commentWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeComment();
            }
        });

        JButton commentReadButton = new JButton("댓글 보기");
        commentReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentReadButton.setBounds(240, 630, 100, 30);
        commentReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentReadButton);

        commentReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readComments();
            }
        });

        JButton articleReadButton = new JButton("새로고침");
        articleReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleReadButton.setBounds(60, 630, 90, 30);
        articleReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleReadButton);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePostList();
            }
        });

        JButton followButton = createFollowButton(otherUserID);
        followButton.setBounds(275, 130, 90, 30);
        followButton.setForeground(new Color(254, 255, 255));
        contentPane.add(followButton);

//        JLabel MainLogo = new JLabel("");
//        Image img = new ImageIcon(this.getClass().getResource("/img/mainLogo.png")).getImage();
//        MainLogo.setIcon(new ImageIcon(img));
//        MainLogo.setBounds(184, 25, 30, 30);
//        contentPane.add(MainLogo);

        JButton BacktoMy = new JButton(new ImageIcon(getClass().getResource("/img/backArrow.png")));
        BacktoMy.setBounds(30, 25, 30, 30);
        contentPane.add(BacktoMy);
        BacktoMy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainBoardUI mainBoardUI = new MainBoardUI(userID);
                mainBoardUI.setTitle("MyHome");
                mainBoardUI.setVisible(true);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
        displayUserInfo();
        updatePostList();
    }

    private void updatePostList() {
        ArrayList<Post> arr = controller.readPost(otherUserID);
        arr = controller.listSort(arr);

        if (arr.size() == 0) {
            listModel = new DefaultListModel();
            String info = otherUserID + "님은 아직 트윗하지 않았습니다";
            listModel.addElement(info);
        } else {
            listModel = new DefaultListModel();
            for (Post res : arr) {
                LocalDateTime createdAt = getCreatedAtFromDatabase(res.getNum());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
                String post = res.getNum() + " " + "(" + res.getId() + ") " + formattedTime + " ] " + res.getArticle() + "\n";
                listModel.addElement(post);
            }
        }
        list.setModel(listModel);
    }

    private JButton createFollowButton(String userid) {
        JButton button = new JButton(controller.setFollowButton(userID, userid));
        setupFollowButton(button, userid);
        return button;
    }

    private void setupFollowButton(JButton button, String userid) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String followState = controller.setFollowButton(userID, userid);
                if (followState.equals("follow")) {
                    controller.updateFollow(userID, userid);
                    button.setText("unfollow");
                } else if (followState.equals("unfollow")) {
                    controller.updateFollow(userID, userid);
                    button.setText("follow");
                }
            }
        });
    }

    private LocalDateTime getCreatedAtFromDatabase(int num) {
        LocalDateTime createdAt = null;
        try (Connection con = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD)) {
            PreparedStatement st = con.prepareStatement("SELECT createdAt FROM article WHERE num = ?");
            st.setInt(1, num);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdAt;
    }

    private void displayUserInfo() {
        try {
            UserInfo userInfo = getUserInfo(otherUserID);

            nameLabel.setText("이름: " + userInfo.getFirstname() + " " + userInfo.getLastname());
            emailLabel.setText("이메일: " + userInfo.getEmail());
            genderLabel.setText("성별: " + userInfo.getGender());
            birthdateLabel.setText("생년월일: " + userInfo.getBirthdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private UserInfo getUserInfo(String userID) throws SQLException {
        UserInfo userInfo = null;

        String query = "SELECT firstname, lastname, email, gender, birthdate FROM account WHERE id = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, userID);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                userInfo = new UserInfo(rs);
            }
        }

        return userInfo;
    }

    private void writeComment() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(String.valueOf(strArr[0]));

            String commentText = JOptionPane.showInputDialog(this, "댓글을 입력하세요:");

            if (commentText != null && !commentText.isEmpty()) {
                controller.writeComment(userID, otherUserID, articleNum, commentText);
                updatePostList();
            }
        }
    }

    private void readComments() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(String.valueOf(strArr[0]));

            ArrayList<String> comments = controller.readComments(articleNum);
            if (!comments.isEmpty()) {
                StringBuilder commentText = new StringBuilder("댓글 목록:\n");
                for (String comment : comments) {
                    commentText.append(comment).append("\n");
                }
                JOptionPane.showMessageDialog(this, commentText.toString(), "댓글 목록", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "댓글이 없습니다.", "댓글 목록", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
