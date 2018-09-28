package com.mazerapp.moviecatalogapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mazerapp.moviecatalogapp.utils.Constants.MOVIESDB_API_BASE_URL;

/**
 * Created by arthur on 09/09/2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    public static Retrofit buildUrl() {

        Gson gson = new GsonBuilder()
                .create();

        return new Retrofit.Builder()
                .baseUrl(MOVIESDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static boolean isInternetConnected(){
        try {
            Socket mSocket = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            mSocket.connect(sockaddr, 2000);
            mSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
