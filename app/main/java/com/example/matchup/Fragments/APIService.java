package com.example.matchup.Fragments;

import com.example.matchup.Notifications.MyResponse;
import com.example.matchup.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAGYtfDfQ:APA91bFv1iiuXI2YrM_4BV5lGVfuyXIxDYCUIfy6Tf9DCEVpXJKE11yFWkTeGTKfiSlE3ov_t0dxKQp0m7-9O27cBUNfeVqHoEBGGJQ08j2QMzagJRNwwl0q3rCMO03w56_oschVy0Yf"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
