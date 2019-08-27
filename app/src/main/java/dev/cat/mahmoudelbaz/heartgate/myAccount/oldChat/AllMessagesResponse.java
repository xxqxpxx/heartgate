package dev.cat.mahmoudelbaz.heartgate.myAccount.oldChat;

/**
 * Created by Ahmed Abdul Fatah on 8/22/19.
 */
public class AllMessagesResponse {


    /**
     * id : 55
     * username : hema
     * userMessage : Hi Mahmoud
     */

    private String id;
    private String username;
    private String userMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
