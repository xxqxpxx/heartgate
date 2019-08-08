package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

/**
 * Created by mahmoudelbaz on 4/9/18.
 */

public class Answers_item {
    int id;
    String content,date,userName,profilePic;

    public Answers_item(int id, String content, String date, String userName, String profilePic) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.userName = userName;
        this.profilePic = profilePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
