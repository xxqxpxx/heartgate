package dev.cat.mahmoudelbaz.heartgate.webServices;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroWeb {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://heartgate.co/api_heartgate/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new okhttp3.OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build())

                    .build();
        }
        return retrofit;

    }

    public static Retrofit getLocationClint() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://maps.google.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }



}
