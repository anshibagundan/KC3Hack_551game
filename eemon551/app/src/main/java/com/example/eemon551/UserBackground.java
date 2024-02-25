package com.example.eemon551;

public class UserBackground {
    private Boolean use;

    private Boolean isOwn;
    private Boolean buyOK;
    private int background_id;
    private int user_data_id;

    public UserBackground(boolean use, boolean isOwn, boolean buyOK,int background_id, int user_data_id) {
        this.use = use;
        this.isOwn = isOwn;
        this.buyOK = buyOK;
        this.background_id = background_id;
        this.user_data_id = user_data_id;

    }
    public Boolean getUse() {
        return use;
    }

    public Boolean getBuyOK() {
        return buyOK;
    }

    public Boolean getisOwn() {
        return isOwn;
    }

    public int getUser_data_id() {
        return user_data_id;
    }

    public int getBackground_id() {
        return background_id;
    }
}
