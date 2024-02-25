package com.example.eemon551;

public class UserQuestionDataUpdateRequest {
    private boolean cor;
    private int user_data_id;
    private int qes_id;

    public UserQuestionDataUpdateRequest(boolean cor, int user_data_id, int qes_id) {
        this.cor = cor;
        this.user_data_id = user_data_id;
        this.qes_id = qes_id;
    }

    // 必要に応じて、getterとsetterをここに追加
}
