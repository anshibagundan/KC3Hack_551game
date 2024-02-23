package com.example.eemon551;

public class UserTitles {
    private boolean use; // Using primitive type for efficiency
    private boolean isOwn;
    private boolean buyOK;
    private int user_data_id;
    private int title_id;

    // Corrected constructor name to match class name
    public UserTitles(boolean use, boolean isOwn, boolean buyOK, int user_data_id, int title_id) {
        this.use = use;
        this.isOwn = isOwn;
        this.buyOK = buyOK;
        this.user_data_id = user_data_id;
        this.title_id = title_id;
    }

    // Getter methods
    public boolean getUse() {
        return use;
    }

    public boolean getisOwn() { // Corrected method name
        return isOwn;
    }

    public boolean getBuyOK() {
        return buyOK;
    }

    public int getUser_data_id() {
        return user_data_id;
    }

    public int getTitle_id() {
        return title_id;
    }

    // Consider adding setter methods if you need to change the values after object creation
}
