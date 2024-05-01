package ui;

import MVC.Controller;
import MVC.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainBoardUI extends JFrame {

    String ID;
    private JPanel contentPane;
    private JList list;
    private JButton articleReadButton;
    private final JButton MainBoardButton;
    private final JButton AllUserButton;
    private final JButton MyHomeBoard;
    private final JButton searchButton;

    public MainBoardUI(String ID) {
        this.ID = ID;
        Controller controller = new Controller();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        // 패널 구성
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 글 읽는 부분
        list = new JList();
        list.setBounds(40, 200, 320, 400);
        list.setBackground(new Color(255, 191, 81, 255));
        list.setForeground(new Color(0, 0, 0));
        contentPane.add(list);

        // 타인의 글 읽어오기
        ArrayList<Post> arr = controller.readPost(ID);
        ArrayList<String> followArr = controller.getFollowing(ID);
        for (String s : followArr) {
            ArrayList<Post> tempPost = controller.readPost(s);
            arr.addAll(tempPost);
        }
        arr = controller.listSort(arr);

        createListModel(arr);

        // 새로고침 빈도 설정
        articleReadButton = new JButton(new ImageIcon(getClass().getResource("/img/refreshPage.png")));
        articleReadButton.setBounds(310, 120, 30, 30);
        contentPane.add(articleReadButton);
        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Post> arr = controller.readPost(ID);
                ArrayList<String> followArr = controller.getFollowing(ID);
                for (String s : followArr) {
                    ArrayList<Post> tempPost = controller.readPost(s);
                    arr.addAll(tempPost);
                }
                arr = controller.listSort(arr);

                createListModel(arr);
            }
        });

        // 검색하기
        searchButton = new JButton("Search");  // 추가된 부분
        searchButton.setBounds(200, 620, 80, 40);  // 추가된 부분
        searchButton.setForeground(new Color(0, 0, 0));
        contentPane.add(searchButton);  // 추가된 부분
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Search 버튼을 누를 때 SearchUI를 생성하여 표시
                SearchUI searchUI = new SearchUI(ID);
                searchUI.setVisible(true);
            }
        });

        // 메인보드
        MainBoardButton = new JButton("MainBoard");
        MainBoardButton.setBounds(30, 620, 80, 40);
        MainBoardButton.setForeground(new Color(0, 0, 0));
        contentPane.add(MainBoardButton);
        MainBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("MainBoard!");
            }
        });

        // 전체유저 파악
        AllUserButton = new JButton("All User");
        AllUserButton.setBounds(115, 620, 80, 40);
        AllUserButton.setForeground(new Color(0, 0, 0));
        contentPane.add(AllUserButton);
        AllUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("AllUser!");
                AllUserUI followUI = new AllUserUI(ID);
                followUI.setVisible(true);
            }
        });

        // 마이 홈
        MyHomeBoard = new JButton("MY Home");
        MyHomeBoard.setBounds(290, 620, 80, 40); // 우측 아래로 이동
        MyHomeBoard.setForeground(new Color(0, 0, 0));
        contentPane.add(MyHomeBoard);
        MyHomeBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.out.println("Your Board!");
                MyBoardUI myBoardUI = new MyBoardUI(ID);
                myBoardUI.setVisible(true);
            }
        });

        // 로고 사진
//        ImageIcon mainLogoIcon = new ImageIcon(getClass().getResource("/img/mainLogo.png"));
//        JLabel mainLogo = new JLabel(mainLogoIcon);
//        mainLogo.setBounds(184, 25, mainLogoIcon.getIconWidth(), mainLogoIcon.getIconHeight());
//        contentPane.add(mainLogo);
//
//        setLocationRelativeTo(null);
//        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    private DefaultListModel createListModel(ArrayList<Post> posts) {
        DefaultListModel model = new DefaultListModel();

        if (posts.size() == 0) {
            String info = "아직 작성된 글이 없어요";
            model.addElement(info);
        } else {
            for (Post res : posts) {
                // 게시물의 num을 이용하여 데이터베이스에서 시간 정보를 가져오기
                LocalDateTime createdAt = getCreatedAtFromDatabase(res.getNum());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));

                String post = "[ (" + res.getId() + ") " + formattedTime + " ] " + res.getArticle() + "\n";
                model.addElement(post);
            }
        }

        list.setModel(model); // JList에 모델을 설정
        return model;
    }

    // num을 이용하여 데이터베이스에서 시간 정보를 가져오는 메서드
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

}
