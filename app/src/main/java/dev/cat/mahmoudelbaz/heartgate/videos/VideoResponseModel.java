package dev.cat.mahmoudelbaz.heartgate.videos;

public class VideoResponseModel {


    /**
     * id : 1
     * video_name : video 1
     * video_url : videos/healthcare.mp4
     * video_description : Video Description Goes here in this area when user click on video link show all details. Video Description Goes here in this area when user click on video link show all details. Video Description Goes here in this area when user click on video link show all details.
     * tags : heart - gate
     * created_by : admin
     * creation_date : 2019-05-19 11:32:59
     */

    private String id;
    private String video_name;
    private String video_url;
    private String video_description;
    private String tags;
    private String created_by;
    private String creation_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
}
