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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
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

        //actionView.setVisibility(View.INVISIBLE);
        bulbLayer.setVisibility(View.INVISIBLE);
        humancounter.setVisibility(View.INVISIBLE);
        cctvView.setVisibility(View.INVISIBLE);

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
                    setState(state);
                    bulbLayer.setVisibility(View.VISIBLE);
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

    public void setState(String homeStates){
        String[] led_states = new String[4];
        boolean bulb_flag = false;
        StringBuilder sb = new StringBuilder();

        try{
            JSONObject json = new JSONObject(homeStates);

            JSONArray led_Array = json.getJSONArray("Led");
            JSONObject json_led = new JSONObject();
            for(int i=0; i<led_Array.length(); i++)
                json_led = led_Array.getJSONObject(i);

            for(int i=0; i<json_led.length(); i++){
                led_states[i] = json_led.getString("room"+Integer.toString(i+1));
                sb.append("room"+Integer.toString(i+1) +" : " + led_states[i] +"\n");
                if(led_states[i].toString().equals("On"))   bulb_flag = true;
            }
            if(bulb_flag){
                bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
                bulbBtn.setText("On");
            }
            else{
                bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));
                bulbBtn.setText("Off");
            }

            resultTest.setText(sb.toString());

            room1Btn.setText(led_states[0]);
            if(room1Btn.getText().toString().equals("On"))
               room1Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room1Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room2Btn.setText(led_states[1]);
            if(room2Btn.getText().toString().equals("On"))
                room2Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room2Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room3Btn.setText(led_states[2]);
            if(room3Btn.getText().toString().equals("On"))
                room3Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room3Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room4Btn.setText(led_states[3]);
            if(room4Btn.getText().toString().equals("On"))
                room4Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room4Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

        }catch (Exception e){
            resultTest.setText("Exception??");
        }
    }

    public String getState(){
        String tempState = null;
        RequestTask task = new RequestTask();
        try{
            tempState = task.execute(requestUrl+"?state").get();
            //RenewState(tempState);
        }catch (Exception e){
            e.printStackTrace();
        }

        return tempState ;
    }

    public class RequestTask extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... requestContent){
            HomeIotClient client = new HomeIotClient();
            String resource = requestContent[0];
            String states = client.getState(resource);

            return states;
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

    URL url ;
    HttpURLConnection conn ;
    InputStream in ;
    String states ;

    public String getState(String resource){
        HomeIotClient client = new HomeIotClient();

        try{
            url = new URL(resource);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream());
            states = getStringFromInputStream(in);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return states;
    }

    public String getStringFromInputStream(InputStream is){
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
