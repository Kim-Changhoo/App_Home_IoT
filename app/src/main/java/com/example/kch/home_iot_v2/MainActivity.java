package com.example.kch.home_iot_v2;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    // View for Control
    Button bulbBtn;
    ToggleButton curtainBtn;
    ToggleButton doorlockBtn;
    Button thenumberBtn;
    Button cctvBtn;

    // View for Action
    FrameLayout actionView;
    LinearLayout bulbLayer;
    ToggleButton room1Btn;
    ToggleButton room2Btn;
    ToggleButton room3Btn;
    ToggleButton room4Btn;
    ToggleButton livingroomBtn;
    TextView humancounter;
    VideoView cctvView;

    // Server resource
    //String requestUrl = "http://192.168.0.55:8080";
    String requestUrl = "http://192.168.1.48:8080/";
    SendRequest sendRequest = null;

    // Temp for View
    String state =null ;
    TextView resultTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View
        bulbBtn = (Button)findViewById(R.id.bulbBtn);
        curtainBtn = (ToggleButton)findViewById(R.id.curtainBtn);
        doorlockBtn = (ToggleButton)findViewById(R.id.doorlockBtn);
        thenumberBtn = (Button)findViewById(R.id.countBtn);
        cctvBtn = (Button)findViewById(R.id.cctvBtn);

        actionView = (FrameLayout)findViewById(R.id.detailedView);
        bulbLayer = (LinearLayout)findViewById(R.id.bulbLayer);
        room1Btn = (ToggleButton)findViewById(R.id.room1Btn);
        room2Btn = (ToggleButton)findViewById(R.id.room2Btn);
        room3Btn = (ToggleButton)findViewById(R.id.room3Btn);
        room4Btn = (ToggleButton)findViewById(R.id.room4Btn);
        livingroomBtn = (ToggleButton)findViewById(R.id.livingroomBtn);
        humancounter = (TextView)findViewById(R.id.humanCounter);
        cctvView = (VideoView)findViewById(R.id.videoView);

        // set onclick
        bulbBtn.setOnClickListener(onClickListener);
        curtainBtn.setOnClickListener(onClickListener);
        doorlockBtn.setOnClickListener(onClickListener);
        thenumberBtn.setOnClickListener(onClickListener);
        cctvBtn.setOnClickListener(onClickListener);

        room1Btn.setOnClickListener(onClickListener);
        room2Btn.setOnClickListener(onClickListener);
        room3Btn.setOnClickListener(onClickListener);
        room4Btn.setOnClickListener(onClickListener);
        livingroomBtn.setOnClickListener(onClickListener);

        // Request Initial Home State
        state = getState();
        resultTest = (TextView)findViewById(R.id.resultTest);
        resultTest.setText(state);
    }

    View.OnClickListener onClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View view){
            int workId = view.getId();

            switch(workId) {
                case R.id.bulbBtn:
                    break;
                case R.id.curtainBtn:
                    break;
                case R.id.doorlockBtn:
                    break;
                case R.id.countBtn:
                    break;
                case R.id.cctvBtn:
                    break;
            }
        }
    };

    public String getState(){
        String tempState = null;
        sendRequest = new SendRequest() ;
        try{
            tempState = sendRequest.execute("?state").get();
            RenewState(tempState);
        }catch (Exception e){
            e.printStackTrace();
        }

        return tempState ;
    }

    private void setState(View view, String turnningWhat){
        int target = view.getId();
        switch(target){
            case  R.id.bulbBtn:
                bulbBtn.setText(turnningWhat);
                if(bulbBtn.getText() == "On")
                    bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
                else
                    bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));
                break;
            case R.id.curtainBtn:
                curtainBtn.setText(turnningWhat);
                if(curtainBtn.getText() == "Draw")
                    curtainBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.drawcurtain));
                else
                    curtainBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.curtain));
                break;
            case R.id.doorlockBtn:
                doorlockBtn.setText(turnningWhat);
                if(doorlockBtn.getText() == "Lock")
                    doorlockBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.locked));
                else
                    doorlockBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.unlocked));
                break;
            case R.id.countBtn:
                break;
        }
    }

    public class RequestTask extends AsyncTask<String, Void, String> {
        URL url ;
        HttpURLConnection conn ;
        InputStream in ;
        String response ;

        @Override
        public String doInBackground(String... requestContent){
            HomeIotClient client = new HomeIotClient();
            String 
            StringBuilder sb = new StringBuilder();

            sb.append(requestUrl);
            sb.append(requestContent[0]);
            String resource = sb.toString();

            try{
                url = new URL(resource);
                conn = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(conn.getInputStream());
                response = getResponse(in);
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                conn.disconnect();
            }
            return response;
        }
    }
/*
    private void RenewState(String states){

        String[] device_state = new String[4];
        int device_num=0;

        StringTokenizer deviceTokenizer = new StringTokenizer(states, "%");
        while(deviceTokenizer.hasMoreTokens()){
            device_state[device_num] = deviceTokenizer.nextToken();
            switch (device_num){
                case 0:
                    setState(bulbBtn,device_state[device_num]);
                    break;
                case 1:
                    setState(curtainBtn,device_state[device_num]);
                    break;
                case 2:
                    setState(doorlockBtn,device_state[device_num]);
                    break;
                case 3:
                    setState(thenumberBtn,device_state[device_num]);
                    break;
            }
        }
    }
    */
}

class HomeIotClient {
    public String getResponse(InputStream is){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String lines = null;

        try{
            br = new BufferedReader(new InputStreamReader(is));
            while((lines = br.readLine()) != null){
                sb.append(lines);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
