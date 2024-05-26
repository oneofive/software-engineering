package MVC;

import ui.DatabaseConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class Controller {
    Connection connect = null;
    ResultSet rs = null;
    Statement st = null;

    // JDBC연결
    public Controller() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(DatabaseConstants.DB_URL, DatabaseConstants.DB_USER, DatabaseConstants.DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 아티클 숫자 가져오기
    public Integer getArticleNextNum() {
        int articleNum = -1;
        try {
            Statement st_num = connect.createStatement();
            rs = st_num.executeQuery("select max(num) from article;");
            while (rs.next()) {
                String numString = rs.getString(1);
                if (numString != null) {
                    articleNum = Integer.parseInt(numString);
                } else {
                    articleNum = Integer.parseInt("0");
                }

                articleNum++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return articleNum;
    }

    // 게시물 추가
    public void insertPost(Post post) {
        try {
            st = connect.createStatement();
            LocalDateTime currentTime = LocalDateTime.now();
            post.setCreatedAt(currentTime); // 작성 시간 설정
            st.executeUpdate("INSERT INTO article (num, ID, article, createdAt) VALUES ('" +
                    post.getNum() + "', '" + post.getId() + "', '" + post.getArticle() + "', '" +
                    post.getCreatedAt() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // 게시물 목록 출력
    public ArrayList<Post> readPost(String id) {
        ArrayList<Post> arr = new ArrayList<>();
        try {
            st = connect.createStatement();
            rs = st.executeQuery("select * from article where ID = '" + id + "';");
            while (rs.next()) {
                arr.add(new Post(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    // 게시물 삭제
    public void deletePost(int postNum) {
        try {
            st = connect.createStatement();
            int stmt = st.executeUpdate("delete from article where num = '" + postNum + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //팔로잉 가져오기
    public ArrayList<String> getFollowing(String ID) {
        ArrayList<String> arr = new ArrayList<>();
        try {
            st = connect.createStatement();
            rs = st.executeQuery("select toUser from follow where fromUser = '" + ID + "';");
            while (rs.next()) {
                arr.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    //팔로워 가져오기
    public ArrayList<String> getFollower(String ID) {
        ArrayList<String> arr = new ArrayList<>();
        try {
            st = connect.createStatement();
            rs = st.executeQuery("select fromUser from follow where toUser = '" + ID + "';");
            while (rs.next()) {
                arr.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    // 전체 유저 검색
    public ArrayList<String> getAllUser(String myID) {
        ArrayList<String> arr = new ArrayList<>();
        System.out.println(arr);
        try {
            st = connect.createStatement();
            rs = st.executeQuery("select id from account where id <> '" + myID + "';");
            while (rs.next()) {
                arr.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    // 팔로우 여부 탐색
    public String setFollowButton(String Myid, String userId) {
        // 팔로우가 되어 있지 않아야 팔로우라고 보여야 함
        // 팔로우 안 되어있는 상태
        if (!checkFollow(Myid, userId)) {
            return "follow";
        }
        return "unfollow";
    }


    public Boolean checkFollow(String Myid, String userId) {
        boolean result = false;
        int res_num = -1;
        try {
            st = connect.createStatement();
            rs = st.executeQuery(
                    "select exists ( select 1 from follow where fromUser = '" + Myid + "'" +
                            " and toUser = '" + userId + "') as cnt;");
            while (rs.next()) {
                res_num = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (res_num == 1) {
            result = true;
        }
        return result;
    }

    public void addFollow(String Myid, String userId) {
        try {
            st = connect.createStatement();
            st.executeUpdate("insert into follow (fromUser, toUser) values ('" + Myid + "', '" + userId + "');");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFollow(String Myid, String userId) {
        try {
            st = connect.createStatement();
            st.executeUpdate("delete from follow where fromUser = '" + Myid + "'" + " and toUser = '" + userId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFollow(String Myid, String userId) {
        if (checkFollow(Myid, userId)) {
            deleteFollow(Myid, userId);
        } else {
            addFollow(Myid, userId);
        }
    }

    public boolean userExists(String userId) {
        boolean result = false;
        try {
            st = connect.createStatement();
            rs = st.executeQuery("select count(*) from account where id = '" + userId + "';");
            while (rs.next()) {
                int count = rs.getInt(1);
                result = count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    class PostNumComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o2.getNum() - o1.getNum();
        }
    }

    public ArrayList<Post> listSort(ArrayList<Post> list) {
        list.sort(new PostNumComparator());
        return list;
    }

    // 게시물에 댓글 작성
    public void writeComment(String fromUserID, String toUserID, int articleNum, String commentText) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            PreparedStatement st = connect.prepareStatement("INSERT INTO comment (articleNum, fromUserID, toUserID, message, createdAt) VALUES (?, ?, ?, ?, ?)");
            st.setInt(1, articleNum);
            st.setString(2, fromUserID);
            st.setString(3, toUserID);
            st.setString(4, commentText);
            st.setTimestamp(5, Timestamp.valueOf(currentTime));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // 게시물의 댓글 조회
    public ArrayList<String> readComments(int articleNum) {
        ArrayList<String> comments = new ArrayList<>();
        try {
            PreparedStatement st = connect.prepareStatement("SELECT fromUserID, message, createdAt FROM comment WHERE articleNum = ?");
            st.setInt(1, articleNum);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String fromUserID = rs.getString("fromUserID");
                String message = rs.getString("message");
                LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
                String comment = "[" + formattedTime + "] " + fromUserID + ": " + message;
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }


}