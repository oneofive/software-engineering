package ui;

import MVC.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyBoardUI extends JFrame {

    String ID;
    private JPanel contentPane;
    private JTextArea writeArea;

    private JList list;
    private int articleNum;
    DefaultListModel listModel;
    private Controller controller;


    public MyBoardUI(String ID) {
        this.ID = ID;
        this.controller = new Controller();

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 80, 400, 710);

        // 기본 패널 설정
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));

        // My Home에 들어갔을 때, ID 표시
        JLabel userIdLabel = new JLabel(ID + "의 피드");
        userIdLabel.setFont(new Font("Nanum Gothic", Font.BOLD, 16));
        userIdLabel.setForeground(new Color(0, 0, 0));
        userIdLabel.setBounds(40, 130, 200, 30);
        contentPane.add(userIdLabel);

        // 팔로워 기능
        JButton followerButton = new JButton("팔로워");
        followerButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        followerButton.setForeground(new Color(254, 255, 255));

        followerButton.setBounds(220, 130, 80, 30);
        contentPane.add(followerButton);

        followerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> followerList = controller.getFollower(ID);
                showFollowList("내 팔로워 목록", followerList);

            }
        });

        // 팔로잉 기능
        JButton followingButton = new JButton("팔로잉");
        followingButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));

        followingButton.setBounds(290, 130, 80, 30);
        followingButton.setForeground(new Color(254, 255, 255));
        contentPane.add(followingButton);

        followingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> followingList = controller.getFollowing(ID);
                showFollowList("내 팔로잉 목록", followingList);
            }
        });

        // 로그아웃
        JButton Logout = new JButton("로그아웃");
        Logout.setForeground(new Color(220, 45, 59));
        Logout.setBackground(UIManager.getColor("Button.disabledForeground"));
        Logout.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        JFrame frame = new JFrame("Logout Window");
        Logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Yes", "No"};
                int a = JOptionPane.showOptionDialog(frame, "Are you sure?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    LoginUI obj = new LoginUI();
                    obj.setTitle("User-Login");
                    obj.setVisible(true);
                }
            }
        });

        Logout.setBounds(285, 20, 100, 30);
        contentPane.add(Logout);

        // 개인정보 변경
        JButton InfoChangeButton = new JButton("개인정보 변경");
        InfoChangeButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        InfoChangeButton.setForeground(new Color(254, 255, 255));
        InfoChangeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 기존 비밀번호 확인
                String oldPassword = JOptionPane.showInputDialog("기존 비밀번호를 입력하세요:");

                if (isOldPasswordCorrect(oldPassword)) {
                    // 비밀번호가 일치하는 경우
                    ChangeInfoUI infoUI = new ChangeInfoUI(ID);
                    infoUI.setTitle("Change Password");
                    infoUI.setVisible(true);
                } else {
                    // 비밀번호가 일치하지 않는 경우
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        InfoChangeButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        InfoChangeButton.setBounds(285, 47, 100, 30);
        contentPane.add(InfoChangeButton);

        // 구성
        Controller controller = new Controller();

        setContentPane(contentPane);
        contentPane.setLayout(null);

        writeArea = new JTextArea("");
        writeArea.setBounds(40, 170, 320, 73);
        writeArea.setBorder(BorderFactory.createCompoundBorder(
                writeArea.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        contentPane.add(writeArea);

        // 글 읽어오는 부분
        list = new JList();
        list.setBounds(40, 300, 320, 300);
        list.setForeground(new Color(255, 191, 81, 255));
        list.setBackground(new Color(20, 20, 20));
        contentPane.add(list);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    String postText = (String) list.getSelectedValue();
                    if (postText != null) {
                        String[] strArr = postText.split(" ");
                        articleNum = Integer.parseInt(String.valueOf(strArr[0]));
                    }
                }
            }
        });

        // 홈페이지 방문시, 글 업데이트 부분
        ArrayList<Post> arr;
        arr = controller.readPost(ID);
        arr = controller.listSort(arr);

        updatePostList(arr);

        //글 작성
        JButton articleWriteButton = new JButton("글 작성하기");
        articleWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleWriteButton.setBounds(210, 250, 100, 30);
        articleWriteButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleWriteButton);

        articleWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String article = writeArea.getText();
                int num = controller.getArticleNextNum();
                controller.insertPost(new Post(num, ID, article));

                list.clearSelection();

                ArrayList<Post> arr = controller.readPost(ID);
                arr = controller.listSort(arr);
                updatePostList(arr);
            }
        });

        // 새로고침
        JButton articleReadButton = new JButton("새로고침");
        articleReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleReadButton.setBounds(100, 250, 90, 30);
        articleReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleReadButton);

        articleReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Post> arr = controller.readPost(ID);
                arr = controller.listSort(arr);
                updatePostList(arr);
            }
        });

        // 글 삭제
        JButton articleDeleteButton = new JButton("글 삭제하기");
        articleDeleteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleDeleteButton.setBounds(30, 630, 90, 30);
        articleDeleteButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleDeleteButton);

        articleDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 글 삭제 여부 다이얼로그 표시
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "게시물을 삭제하시겠습니까?",
                        "게시물 삭제 확인",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    // YES를 선택한 경우에만 글 삭제 진행
                    controller.deletePost(articleNum);
                    System.out.println(articleNum + "번 게시물이 삭제되었습니다!");

                    // 삭제 후 업데이트된 포스트 목록 가져오기
                    ArrayList<Post> updatedPostList = controller.readPost(ID);
                    updatedPostList = controller.listSort(updatedPostList);

                    // 업데이트된 포스트 목록으로 UI 업데이트
                    updatePostList(updatedPostList);
                }
            }
        });


        // 글 수정
        JButton articleUpdateButton = new JButton("글 수정하기");
        articleUpdateButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        articleUpdateButton.setBounds(110, 630, 90, 30);
        articleUpdateButton.setForeground(new Color(254, 255, 255));
        contentPane.add(articleUpdateButton);

        articleUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeArticleUI changePostUI = new ChangeArticleUI(articleNum);
                changePostUI.setTitle("Change Post");
                changePostUI.setVisible(true);
            }
        });

        // 댓글 보기
        JButton commentReadButton = new JButton("댓글 보기");
        commentReadButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentReadButton.setBounds(190, 630, 90, 30);
        commentReadButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentReadButton);

        commentReadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readComments();
            }
        });

        // 댓글 달기
        JButton commentWriteButton = new JButton("댓글 달기");
        commentWriteButton.setFont(new Font("Nanum Gothic", Font.BOLD, 12));
        commentWriteButton.setBounds(270, 630, 90, 30);  // 위치를 수정했습니다.
        commentWriteButton.setForeground(new Color(254, 255, 255));
        contentPane.add(commentWriteButton);
        commentWriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeComments();
            }
        });

        // 로고 사진
//        JLabel MainLogo = new JLabel("");
//        Image img = new ImageIcon(this.getClass().getResource("/img/mainLogo.png")).getImage();
//        MainLogo.setIcon(new ImageIcon(img));
//        MainLogo.setBounds(184, 25, 30, 30);
//        contentPane.add(MainLogo);

        // 뒤로가기
        JButton BacktoMain = new JButton(new ImageIcon(getClass().getResource("/img/backArrow.png")));
        BacktoMain.setBounds(30, 25, 30, 30);
        contentPane.add(BacktoMain);

        BacktoMain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // main으로 이동
                MainBoardUI main = new MainBoardUI(ID);
                main.setTitle("GoalBuddy");
                main.setVisible(true);
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }

    // MyBoardUI 클래스에 다음 메서드를 추가합니다.
    private void readComments() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(strArr[0]);

            // 댓글 조회 및 표시 로직 구현
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

    private void writeComments() {
        String postText = (String) list.getSelectedValue();
        if (postText != null && !postText.isEmpty()) {
            String[] strArr = postText.split(" ");
            int articleNum = Integer.parseInt(strArr[0]);

            String commentText = JOptionPane.showInputDialog(this, "댓글을 입력하세요:");
            if (commentText != null && !commentText.isEmpty()) {
                // 댓글 작성 로직 구현
                controller.writeComment(ID, ID, articleNum, commentText);

                // 댓글 작성 후 목록 업데이트
                ArrayList<Post> arr = controller.readPost(ID);
                arr = controller.listSort(arr);
                updatePostList(arr);

                // 댓글 작성 후 입력 영역 초기화
                writeArea.setText("");
            }
        }
    }

    private boolean isOldPasswordCorrect(String inputPassword) {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM account WHERE ID=?");
            preparedStatement.setString(1, ID);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(inputPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void showFollowList(String title, ArrayList<String> userList) {
        JFrame followListFrame = new JFrame();
        JPanel jpMain = new JPanel();
        jpMain.setLayout(new BorderLayout());
        JPanel jp = new JPanel();

        followListFrame.setTitle(title);
        followListFrame.setSize(300, 300);
        followListFrame.setLocationRelativeTo(null); // 이 부분을 추가하여 다이얼로그를 화면 중앙에 위치시킵니다.
        followListFrame.setVisible(true);
        jp.setLayout(new GridLayout(userList.size(), 2, 10, 10));

        JLabel[] followUserId = new JLabel[1000];
        JButton[] followerBoardButton = new JButton[1000];

        for (int i = 0; i < userList.size(); i++) {
            String followerId = userList.get(i);
            followUserId[i] = new JLabel(followerId);
            followerBoardButton[i] = new JButton(followerId + "의 피드");
            followerBoardButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(followerId + "'s Board!");
                    OtherBoardUI otherBoardUI = new OtherBoardUI(followerId, ID);
                    otherBoardUI.setVisible(true);

                    // OtherBoardUI로 이동할 때 팔로워 목록 창이 자동으로 닫히도록 함
                    followListFrame.dispose();
                    dispose();
                }
            });

            jp.add(followUserId[i]);
            jp.add(followerBoardButton[i]);
        }

        jpMain.add(jp, BorderLayout.NORTH);
        followListFrame.getContentPane().add(new JScrollPane(jpMain), BorderLayout.CENTER);
    }



    private void updatePostList(ArrayList<Post> arr) {
        if (arr.size() == 0) {
            listModel = new DefaultListModel();
            String info = ID + "님은 아직 트윗하지 않았습니다";
            listModel.addElement(info);
        } else {
            listModel = new DefaultListModel();
            for (Post res : arr) {
                // 게시물의 num을 이용하여 데이터베이스에서 시간 정보를 가져오기
                LocalDateTime createdAt = getCreatedAtFromDatabase(res.getNum());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
                String post = res.getNum() + " " +"(" + res.getId() + ") " + formattedTime + " ] " + res.getArticle() + "\n";
                listModel.addElement(post);
            }
        }
        list.setModel(listModel);
        writeArea.setText("");  // writeArea 초기화
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


