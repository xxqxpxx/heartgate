package dev.cat.mahmoudelbaz.heartgate.myAccount;

/**
 * Created by mahmoudelbaz on 9/14/17.
 */

public class ModelMyConnections {


    private int stateId;
    private int id;
    private String name;
    private String jobTitle;
    private String imageUrl;

    public ModelMyConnections() {
    }

    public ModelMyConnections(int stateId, int id, String name, String jobTitle, String imageUrl) {
        this.stateId = stateId;
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.imageUrl = imageUrl;
    }


    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


