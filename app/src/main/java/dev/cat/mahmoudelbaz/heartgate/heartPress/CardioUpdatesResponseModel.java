package dev.cat.mahmoudelbaz.heartgate.heartPress;

import com.google.gson.annotations.SerializedName;

public class CardioUpdatesResponseModel {
    /**
     * id : 26
     * title : Day-by-Day Variability of Home Blood Pressure and Incident Cardiovascular Disease in Clinical Practice
     * link : http://hyper.ahajournals.org/content/71/1/177
     * package : 0
     * shown : 0
     */

    private String id;
    private String title;
    private String link;
    @SerializedName("package")
    private String packageX;
    private String shown;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getShown() {
        return shown;
    }

    public void setShown(String shown) {
        this.shown = shown;
    }
}
