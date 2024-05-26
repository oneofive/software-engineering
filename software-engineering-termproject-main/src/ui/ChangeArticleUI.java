package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChangeArticleUI extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JLabel lblEnterNewArticle;

    public ChangeArticleUI(int articleNum) {
        setBounds(500, 120, 400, 180);
        setResizable(false);

        // 패널
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 212, 83, 208));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 수정하는 곳 라벨
        lblEnterNewArticle = new JLabel("수정된 글 :");
        lblEnterNewArticle.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEnterNewArticle.setForeground(new Color(0, 0, 0));
        lblEnterNewArticle.setBounds(34, 32, 72, 27);
        contentPane.add(lblEnterNewArticle);

        // 입력 하는 부분
        textField = new JTextField();
        textField.setFont(new Font("Nanum Gothic", Font.PLAIN, 16));
        textField.setBounds(100, 30, 262, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        // 수정하기 버튼
        JButton modifyButton = new JButton("수정하기");
        modifyButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        modifyButton.setBackground(new Color(221, 221, 221));
        modifyButton.setForeground(new Color(31, 31, 31));
        modifyButton.setBounds(100, 88, 200, 33);
        contentPane.add(modifyButton);

        // 글 수정하기 버튼 클릭 시 실행되는 ActionListener
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String articleContent = textField.getText();

                if (articleContent.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            modifyButton, "글을 입력하세요.", "No Article Wrote", JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }


                try {
                    Connection con = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);


                    // 수정된 부분: 글 내용과 작성 시간을 함께 업데이트
                    PreparedStatement st = con.prepareStatement(
                            "UPDATE article SET article = ?, createdAt = CURRENT_TIMESTAMP WHERE num = ?;"
                    );

                    st.setString(1, articleContent);
                    st.setInt(2, articleNum);

                    st.executeUpdate();
                    JOptionPane.showMessageDialog(
                            modifyButton, "게시글이 수정되었습니다.", "Article changed", JOptionPane.PLAIN_MESSAGE
                    );

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } finally {
                    setVisible(false);
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true); // 이 부분을 setVisible 앞으로 이동
    }
}
