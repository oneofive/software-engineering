import java.sql.*;

public class SqlTest {
    public static void main(String[] args) {
        Connection con = null;

        String server = "localhost"; // 서버 주소
        String user_name = "root"; //  접속자 id
        String password = ""; // 접속자 pw

        // JDBC 드라이버 로드
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("error to load JDBC driver" + e.getMessage());
            e.printStackTrace();
        }

        // 접속
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + "?useSSL=false", user_name, password);
            System.out.println("connection succeed!");
        } catch(SQLException e) {
            System.err.println("connection error" + e.getMessage());
            e.printStackTrace();
        }

        // 접속 종료
        try {
            if(con != null)
                con.close();
        } catch (SQLException e) {}
    }
}
