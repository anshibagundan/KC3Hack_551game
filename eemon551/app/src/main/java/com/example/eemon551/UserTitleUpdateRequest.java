package com.example.eemon551;

public class UserTitleUpdateRequest {
    private boolean use;
    private boolean isOwn;
    private boolean buyOK;
    private int title_id;
    private int user_data_id;

    public UserTitleUpdateRequest(boolean use, boolean isOwn, boolean buyOK, int title_id, int user_data_id){
        this.use = use;
        this.isOwn = isOwn;
        this.buyOK = buyOK;
        this.title_id = title_id;
        this.user_data_id = user_data_id;
    }
}
