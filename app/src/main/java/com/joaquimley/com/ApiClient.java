package com.joaquimley.com;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://career.in.mooo.com/";
    private static RetrofitAPI apiService;
    static String resp;

    public static RetrofitAPI getApiService(){
        if (apiService == null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(RetrofitAPI.class);
        }
        return apiService;
    }

    public static void sendPostRequest(String username, String title, String description, String date){
        Massage massage = new Massage(username, title, description, date);

        getApiService().sendMessage(massage).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    System.out.println("Sent Successfully");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public static String addRequest(String username, String name, String password){

        UserModel user = new UserModel(username, name, password);

        getApiService().addUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    System.out.println("Added Successfully");
                    resp = "Success";
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();

            }
        });

        return resp;
    }


    private static final String SMS_URL = "http://career.in.mooo.com/api/getsms.php?username=";
    private static final String URL = "http://career.in.mooo.com/api/";

    public static String registerUser(String username, String name, String password) {
        return makeHttpRequest(URL + "register.php", username, name, password);
    }

    public static String loginUser(String username, String password) {
        return makeHttpRequest(URL + "login.php", username, null, password);
    }

    public static List<Massage> fetchMessageData(String username) {
        List<Massage> messageList = new ArrayList<>();
        try {
            URL url = new URL(SMS_URL + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                // Parse JSON response
                JSONArray jsonArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    messageList.add(new Massage(
                            jsonObject.getString("username"),
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            jsonObject.getString("date")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }

    private static String makeHttpRequest(String apiUrl, String username, String name, String password) {
        String response = "";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            if (name != null) jsonObject.put("name", name);

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();
            os.close();

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                response += scanner.nextLine();
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\":\"Exception: " + e.getMessage() + "\"}";
        }
        return response;
    }
}
