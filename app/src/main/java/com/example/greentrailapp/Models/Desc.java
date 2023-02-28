package com.example.greentrailapp.Models;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Desc {
    final String BASE_URL="https://en.wikipedia.org/api/rest_v1/page/summary/";
    String subject=null;
    String extract="";

    public Desc(String subject){
        this.subject = subject;
        getData();
    }

    private void getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+subject)
                .get()
                .build();
        try{
            Response response=client.newCall(request).execute();
            String data = response.body().string();
            JSONParser parser = new JSONParser();

            JSONObject jsonObj = (JSONObject)parser.parse(data);

            extract = (String)jsonObj.get("extract");
        }
        catch (IOException | ParseException exception){
            exception.printStackTrace();
        }
    }

    public String getExtract(){
        return extract;
    }
}
