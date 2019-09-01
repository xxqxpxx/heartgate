package dev.cat.mahmoudelbaz.heartgate.poll;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmed Abdul Fatah on 9/1/19.
 */
public class SurveryResponseModel {

    /**
     * id : 8
     * survey_name : mahalla 21 march
     */
    @SerializedName("id")
    private String id;
    @SerializedName("survey_name")
    private String survey_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(String survey_name) {
        this.survey_name = survey_name;
    }
}
