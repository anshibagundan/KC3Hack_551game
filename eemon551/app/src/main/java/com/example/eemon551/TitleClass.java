package com.example.eemon551;

import android.widget.TextView;

public class TitleClass {
    private TextView textView;
    private int intValue;

    public TitleClass(TextView textView, int intValue) {
        this.textView = textView;
        this.intValue = intValue;
    }

    public TextView getText() {
        return textView;
    }

    public void setText(TextView textView) {
        this.textView = textView;
    }

    public int getInt() {
        return intValue;
    }

    public void setInt(int intValue) {
        this.intValue = intValue;
    }
}

