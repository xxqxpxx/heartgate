package dev.cat.mahmoudelbaz.heartgate.heartPress;

import com.google.gson.annotations.SerializedName;

public class onlineLibraryResponseModel {


    /**
     * id : 3
     * title : Assessing Cardiovascular Risk to Guide Hypertension Diagnosis and Treatment
     * link : http://jamanetwork.com/journals/jamacardiology/article-abstract/2549971
     * package : 1
     */

    private String id;
    private String title;
    private String link;
    @SerializedName("package")
    private String packageX;

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
}
