package com.example.room_iot;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTTClient  {

    final String username = "lhqwiyin";
    final String password = "rgaGo8vGawAR";


    //Call this method to connect to RPi
    public void startMqtt(final MqttAndroidClient mqttAndroidClient, final String command, final TextView textView) {

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    switch (command){
                        case "on": try {
                                    publishMessage(mqttAndroidClient,"fanOn",0,"room");
                                    textView.setText("Fan is On");
                                    textView.setTextColor(Color.parseColor("#00574B"));
                                    } catch (MqttException e) {
                                    e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    }
                                    break;
                        case "off": try {
                                    publishMessage(mqttAndroidClient,"fanOff",0,"room");
                                    textView.setText("Fan is Off");
                                    textView.setTextColor(Color.parseColor("#D81B60"));
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                        case "test":textView.setText("Connected to RPi");
                                    break;
                        default: MainActivity.MQTTstatus="Invalid Command";
                    }


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    textView.setText("Can not Connect. Error ->"+exception.toString());
                    Log.w("Mqtt", "Failed to connect to: "  + exception.toString());
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }

    }

    //Call this method to send message to RPi
    public void publishMessage(@NonNull MqttAndroidClient client,
                               @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(5866);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }


}
