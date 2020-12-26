package com.example.room_iot;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;


public class MainActivity extends AppCompatActivity {

    TextView tv_mqtt;
    TextView tv_fan;
    ImageView img_fan;
    Button btn_on;
    Button btn_off;
    //insert your cloud mqtt address
    final String serverUri = "tcp://m16.cloudmqtt.com:19194";
    final String clientId = MqttClient.generateClientId();
    //Topic for cloudMQTT
    final String topic = "room";
    public static String MQTTstatus="Trying to connect to RPi...";
    MqttAndroidClient mqttAndroidClient;
    MQTTClient mqttClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_mqtt=findViewById(R.id.tv_mqtt_status);
        tv_fan=findViewById(R.id.tv_fan_status);
        img_fan=findViewById(R.id.image_fan);
        btn_off=findViewById(R.id.btn_fan_off);
        btn_on=findViewById(R.id.btn_fan_on);
        mqttClient = new MQTTClient();
        mqttAndroidClient=new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        connectToMqtt();
    }

    private void connectToMqtt() {
        mqttClient.startMqtt(mqttAndroidClient,"test",tv_mqtt);
        tv_mqtt.setText(MQTTstatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fanOn(View view) {
        //Publish message to Rpi
        mqttClient.startMqtt( mqttAndroidClient,"on",tv_fan);
        //Set Animation
        Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        aniRotate.setFillAfter(true);
        img_fan.startAnimation(aniRotate);
        btn_on.setEnabled(false);
        btn_off.setEnabled(true);
    }

    public void fanOff(View view) {
        //Publish message to Rpi
        mqttClient.startMqtt( mqttAndroidClient,"off",tv_fan);
        //Set Animation
        img_fan.clearAnimation();
        btn_on.setEnabled(true);
        btn_off.setEnabled(false);
    }


}
