package com.example.android.tidyup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAG2WkrYs:APA91bFsjKmZ9FBkfHiTUz7ro9MszDuvW6RYjmnx0ba0zHndCh_Hn8-7PZsQpntKWivR3Zb1rLD4mnEkCYCLHcCk2siQSlSUooFOYvfBFRnuU3sVME2-yYPqXxvdbVusEbYDVdIuYqUU" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
