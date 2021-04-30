package com.example.android.tidyup;

import android.content.Intent;

public class NotificationSender {
    public Data data;
    public String to;
    public Intent intent;

    public NotificationSender(Data data, String usertoken) {
        this.data = data;
        this.to = usertoken;
    }
}