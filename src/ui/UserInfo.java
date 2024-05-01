package ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private Date birthdate;

    // 생성자
    public UserInfo(ResultSet rs) throws SQLException {
        this.firstname = rs.getString("firstname");
        this.lastname = rs.getString("lastname");
        this.email = rs.getString("email");
        this.gender = rs.getString("gender");
        this.birthdate = rs.getDate("birthdate");
    }

    // 각 필드에 대한 getter 메서드
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }
}
