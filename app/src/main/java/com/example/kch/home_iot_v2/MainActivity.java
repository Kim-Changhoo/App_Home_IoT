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
    public static final String LED = "led";
    public static final String ROOM1 = "led1";
    public static final String ROOM2 = "led2";
    public static final String ROOM3 = "led3";
    public static final String ROOM4 = "led4";
    public static final String CURTAIN = "curtain";
    public static final String DOORLOCK = "doorlock";

    public static final String CONTROL_BULB_ON = "on";
    public static final String CONTROL_BULB_OFF = "off";
    public static final String CONTROL_CURTAIN_OPEN = "open";
    public static final String CONTROL_CURTAIN_CLOSE = "close";
    public static final String CONTROL_DOORLOCK_LOCK = "lock";
    public static final String CONTROL_DOORLOCK_UNLOCK = "unlock";

    public static final String STATE_BULB_ON = "on";
    public static final String STATE_BULB_OFF = "off";
    public static final String STATE_CURTAIN_OPEND = "opend";
    public static final String STATE_CURTAIN_CLOSED = "closed";
    public static final String STATE_DOORLOCK_LOCK = "locked";
    public static final String STATE_DOORLOCK_UNLOCK = "unlocked";

    // View for Control
    Button bulbBtn;
    Button curtainBtn;
    Button doorlockBtn;
    Button thenumberBtn;
    Button cctvBtn;

    // View for Action
    FrameLayout actionView;
    LinearLayout bulbLayer;
    Button room1Btn;
    Button room2Btn;
    Button room3Btn;
    Button room4Btn;
    Button livingroomBtn;
    TextView humancounter;
    VideoView cctvView;

    // Server resource
    //String requestUrl = "http://192.168.0.55:8080/";
    //String requestUrl = "http://192.168.1.26:8080/";
    //String requestUrl = "http://222.112.247.133:7000/";
    String requestUrl = "http://192.168.1.37:8080/";

    // Temp for View
    String state =null ;
    TextView resultTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize View
        bulbBtn = (Button)findViewById(R.id.bulbBtn);
        curtainBtn = (Button)findViewById(R.id.curtainBtn);
        doorlockBtn = (Button)findViewById(R.id.doorlockBtn);
        thenumberBtn = (Button)findViewById(R.id.countBtn);
        cctvBtn = (Button)findViewById(R.id.cctvBtn);

        actionView = (FrameLayout)findViewById(R.id.detailedView);
        bulbLayer = (LinearLayout)findViewById(R.id.bulbLayer);
        room1Btn = (Button)findViewById(R.id.room1Btn);
        room2Btn = (Button)findViewById(R.id.room2Btn);
        room3Btn = (Button)findViewById(R.id.room3Btn);
        room4Btn = (Button)findViewById(R.id.room4Btn);
        livingroomBtn = (Button)findViewById(R.id.livingroomBtn);
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
        resultTest = (TextView) findViewById(R.id.resultTest);
        resultTest.setText(state);
        setState(state);
    }

    View.OnClickListener onClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View view){
            int workId = view.getId();
            String result = null;

            switch(workId) {
                case R.id.bulbBtn:
                    bulbLayer.setVisibility(View.VISIBLE);
                    humancounter.setVisibility(View.GONE);
                    cctvView.setVisibility(View.GONE);
                    break;
                case R.id.room1Btn:
                    if(room1Btn.getText().toString().equals(STATE_BULB_OFF))
                        result = RequestDeviceControl(ROOM1, CONTROL_BULB_ON);
                    else
                        result = RequestDeviceControl(ROOM1, CONTROL_BULB_OFF);
                    setLedstate(result);
                    break;
                case R.id.room2Btn:
                    if(room1Btn.getText().toString().equals(STATE_BULB_OFF))
                        result = RequestDeviceControl(ROOM2, CONTROL_BULB_ON);
                    else
                        result = RequestDeviceControl(ROOM2, CONTROL_BULB_OFF);
                    setLedstate(result);
                    break;
                case R.id.room3Btn:
                    if(room1Btn.getText().toString().equals(STATE_BULB_OFF))
                        result = RequestDeviceControl(ROOM3, CONTROL_BULB_ON);
                    else
                        result = RequestDeviceControl(ROOM3, CONTROL_BULB_OFF);
                    setLedstate(result);
                    break;
                case R.id.room4Btn:
                    if(room1Btn.getText().toString().equals(STATE_BULB_OFF))
                        result = RequestDeviceControl(ROOM4, CONTROL_BULB_ON);
                    else
                        result = RequestDeviceControl(ROOM4, CONTROL_BULB_OFF);
                    setLedstate(result);
                    break;
                case R.id.curtainBtn:
                    if(curtainBtn.getText().toString().equals(STATE_CURTAIN_CLOSED))
                        result = RequestDeviceControl(CURTAIN,CONTROL_CURTAIN_OPEN);
                    else
                        result = RequestDeviceControl(CURTAIN,CONTROL_CURTAIN_CLOSE);
                    setCurtainstate(result);
                    break;
                case R.id.doorlockBtn:
                    if(doorlockBtn.getText().toString().equals(STATE_DOORLOCK_LOCK))
                        result = RequestDeviceControl(DOORLOCK,CONTROL_DOORLOCK_UNLOCK);
                    else
                        result = RequestDeviceControl(DOORLOCK,CONTROL_DOORLOCK_LOCK);
                    setDoorlockstate(result);
                    break;
                case R.id.countBtn:
                    result = RequestDeviceControl("count","view");
                    bulbLayer.setVisibility(View.GONE);
                    humancounter.setVisibility(View.VISIBLE);
                    cctvView.setVisibility(View.GONE);
                    break;
                case R.id.cctvBtn:
                    result = RequestDeviceControl("cctv","view");
                    bulbLayer.setVisibility(View.GONE);
                    humancounter.setVisibility(View.GONE);
                    cctvView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    public String RequestDeviceControl(String device, String instruction){

        String response = null;
        RequestTask task = new RequestTask();
        try{
            response = task.execute((requestUrl +device +"=" +instruction).toString()).get();
        }catch (Exception e){}

        return response;
    }

    public void setState(String homeStates){
        try{
            //JSONObject json = new JSONObject(homeStates);
            setLedstate(homeStates);
            setCurtainstate(homeStates);
        }catch (Exception e){
        }
    }

    public String setDoorlockstate(String deviceJsonObject){
        try{
            JSONObject json = new JSONObject(deviceJsonObject);
            String doorlockState = json.getString(DOORLOCK);
            Button doorlock = ((Button)findViewById(R.id.doorlockBtn));
            doorlock.setText(doorlockState);
            if (doorlock.getText().toString().equals(STATE_DOORLOCK_LOCK))
                doorlock.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.locked));
            else
                doorlock.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.unlocked));
        }catch (Exception e){
        }

        return null;
    }

    public String setCurtainstate(String deviceJsonObject){

        try{
            JSONObject json = new JSONObject(deviceJsonObject);
            String curtainState = json.getString(CURTAIN);
            Button curtain = ((Button)findViewById(R.id.curtainBtn));
            curtain.setText(curtainState);
            if (curtain.getText().toString().equals(STATE_CURTAIN_OPEND))
                curtain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.curtain));
            else
                curtain.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.drawcurtain));
        }catch (Exception e){
        }

        return null;
    }

    public String setLedstate(String deviceJsonObject){

        String[] led_states = new String[4];
        boolean bulb_flag = false;
        StringBuilder sb = new StringBuilder();

        try{
            // LED JSON Array -> Object 추출
            JSONObject json = new JSONObject(deviceJsonObject);
            JSONArray led_Array = json.getJSONArray(LED);
            JSONObject json_led = new JSONObject();
            for(int i=0; i<led_Array.length(); i++)
                json_led = led_Array.getJSONObject(i);

            // LED 상태 저장
            for(int i=0; i<(json_led.length()); i++){
                led_states[i] = json_led.getString("led"+Integer.toString(i+1));
                sb.append("room"+Integer.toString(i+1) +" : " + led_states[i] +"\n");
                if(led_states[i].toString().equals(STATE_BULB_ON))   bulb_flag = true;
            }
            /*led_states[4] = json_led.getString("livingroom");
            sb.append("livingroom : " +led_states[4] +"\n");
            if(led_states[4].toString().equals("On"))   bulb_flag = true;*/
            resultTest.setText(sb.toString());

            // LED 전체 버튼 Background 바꾸기
            if(bulb_flag){
                bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
                bulbBtn.setText(STATE_BULB_ON);
            }
            else{
                bulbBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));
                bulbBtn.setText(STATE_BULB_OFF);
            }

            // 각 방의 LED 상태 반영.
            room1Btn.setText(led_states[0]);
            if(room1Btn.getText().toString().equals(STATE_BULB_ON))
                room1Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room1Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room2Btn.setText(led_states[1]);
            if(room2Btn.getText().toString().equals(STATE_BULB_ON))
                room2Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room2Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room3Btn.setText(led_states[2]);
            if(room3Btn.getText().toString().equals(STATE_BULB_ON))
                room3Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room3Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

            room4Btn.setText(led_states[3]);
            if(room4Btn.getText().toString().equals(STATE_BULB_ON))
                room4Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                room4Btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));

           /* livingroomBtn.setText(led_states[4]);
            if(livingroomBtn.getText().toString().equals("On"))
                livingroomBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulb));
            else
                livingroomBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bulboff));*/
        }catch (Exception e){
            resultTest.setText("Exception??");
        }
        return null;
    }

    public String getState(){
        String tempState = null;
        RequestTask task = new RequestTask();
        try{
            tempState = task.execute(requestUrl+"state").get();
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
