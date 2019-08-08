package dev.cat.mahmoudelbaz.heartgate.advisoryBoard;

import java.util.List;

/**
 * Created by mahmoudelbaz on 4/9/18.
 */

public class Questions_item {

    /**
     * id : 12
     * q_title : question
     * insert_date : 2019-06-20
     * order_id : 12
     * create_user_id : 0
     * creation_date : 2019-06-20 18:21:51
     * update_user_id : null
     * update_date : null
     * validity : 1
     * answers : []
     */

    private String id;
    private String q_title;
    private String insert_date;
    private String order_id;
    private String create_user_id;
    private String creation_date;
    private Object update_user_id;
    private Object update_date;
    private String validity;
    private List<?> answers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQ_title() {
        return q_title;
    }

    public void setQ_title(String q_title) {
        this.q_title = q_title;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Object getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Object update_user_id) {
        this.update_user_id = update_user_id;
    }

    public Object getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Object update_date) {
        this.update_date = update_date;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public List<?> getAnswers() {
        return answers;
    }

    public void setAnswers(List<?> answers) {
        this.answers = answers;
    }
}
