package com.example.android.tidyup;

public class NotificationSender {
    public Data data;
    public String to;

    public NotificationSender(Data data, String usertoken) {
        this.data = data;
        this.to = usertoken;
    }
}