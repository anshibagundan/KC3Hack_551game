package com.example.eemon551;

public class UserQuestionData {
    private boolean cor;
    private int qes_id;
    private int user_data_id;

    public UserQuestionData(boolean cor, int qesId, int userId) {
        this.cor = cor;
        this.qes_id = qesId;
        this.user_data_id = userId;
    }
    public boolean get_cor(){
        return cor;
    }
    public int get_qes_id(){
        return qes_id;
    }
    public int getUser_data_id(){
        return user_data_id;
    }

}

