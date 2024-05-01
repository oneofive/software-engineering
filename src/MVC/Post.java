package MVC;

import java.time.LocalDateTime;

public class Post {
    String id;
    Integer num;
    String article;
    int likes; // 좋아요 수 추가
    LocalDateTime createdAt; // 작성 시간 추가

    public Post(Integer num, String id, String article) {
        this.id = id;
        this.num = num;
        this.article = article;
        this.likes = 0; // 초기 좋아요 수는 0으로 설정
        this.createdAt = LocalDateTime.now(); // 현재 시간으로 초기화
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void like() {
        likes++;
    }


}
