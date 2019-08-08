package dev.cat.mahmoudelbaz.heartgate.chat;

public class User {

    public User(String id, String name, String socket_id, String online, String updated_at) {
        this.id = id;
        this.name = name;
        this.socket_id = socket_id;
        this.online = online;
        this.updated_at = updated_at;
    }

    /**
     * id : 2
     * name : User2
     * socket_id : iCzu8u6SsQHtnegXAAAJ
     * online : Y
     * updated_at : 2019-04-23T09:41:48.000Z
     */


    private String id;
    private String name;
    private String socket_id;
    private String online;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
