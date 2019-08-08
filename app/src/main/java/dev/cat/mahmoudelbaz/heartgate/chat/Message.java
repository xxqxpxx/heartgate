package dev.cat.mahmoudelbaz.heartgate.chat;

public class Message {
    /**
     * type : text
     * fileFormat :
     * filePath :
     * fromUserId : 9
     * toUserId : 3
     * toSocketId : WhUEXmypuwjQ_KzdAAAS
     * time : 02:44 PM
     * date : 2019-05-7
     */


    private String nickname;
    private String message ;
    private String type;
    private String fileFormat;
    private String filePath;
    private int fromUserId;
    private int toUserId;
    private String toSocketId;
    private String time;
    private String date;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public  Message(){

    }

    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public Message(String nickname, String message , String date ,  String imageUrl) {
        this.nickname = nickname;
        this.message = message;
        this.imageUrl = imageUrl;
        this.date = date;
    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getToSocketId() {
        return toSocketId;
    }

    public void setToSocketId(String toSocketId) {
        this.toSocketId = toSocketId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

