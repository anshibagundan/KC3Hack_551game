package com.example.eemon551;

public class UserBackgroundUpdateRequest {
    private int user_data_id;
    private int background_id;
    private boolean isOwn;
    private boolean buyOK;
    private boolean use;

    public UserBackgroundUpdateRequest(int user_data_id, int background_id, boolean isOwn, boolean buyOK, boolean use) {
        this.user_data_id = user_data_id;
        this.background_id = background_id;
        this.isOwn = isOwn;
        this.buyOK =buyOK;
        this.use = use;

    }

    // Getter and Setter
}
