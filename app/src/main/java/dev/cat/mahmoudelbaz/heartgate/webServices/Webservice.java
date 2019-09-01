package dev.cat.mahmoudelbaz.heartgate.webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Webservice {

    private static Webservice instance;
    private Api api;

    public Webservice() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(160, TimeUnit.SECONDS);
        httpClient.readTimeout(160, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Services.MAIN_URL)
                .build();

        api = retrofit.create(Api.class);

    }

    public static Webservice getInstance() {
        if (instance == null) {
            instance = new Webservice();
        }
        return instance;
    }

    public Api getApi() {
        return api;
    }


}
