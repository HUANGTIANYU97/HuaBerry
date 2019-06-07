package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
public class MQTTService extends Service {
    public static final String TAG = MQTTService.class.getSimpleName();

    static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private String host = "tcp://172.20.10.8:1883";
    private static String myTopic = "button";
    private String clientId = "androidIdshabi";
    private IGetMessageCallBack IGetMessageCallBack;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(getClass().getName(), "onCreate");
        init();
    }

    public static void publish(String msg){
        Boolean retained = false;
        try {
            if (client != null){
                client.publish(myTopic, msg.getBytes(),0, retained.booleanValue());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public  static  void subscribe() throws MqttException {
        if (client != null) {
            client.subscribe("len2", 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Subscribed111222333!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Failed to subscribe");
                }
            }, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    // message Arrived!
                    final String messageRecive = new String(message.getPayload());

                    String messageTem = "" + messageRecive;
                    if(messageTem.equals("delete")){
                        if(!Second.flag){
                            Second.totalNum-=1;
                            Second.beforeNum = Second.totalNum;
                        }else{
                            Second.beforeNum -= 1;
                            Second.totalNum -= 1;
                        }

                    }else if(messageTem.equals("add")){
                        if(!Second.flag){
                            Second.totalNum+=1;
                            Second.beforeNum +=1;
                        }else {
                                Second.totalNum+=1;
                        }

                    }else {
                        if(!Second.flagR ){
                            Second.totalNum = Integer.valueOf(messageTem);
                            int tem= Second.totalNum;
                            Second.beforeNum = tem;
                            System.out.println(Second.beforeNum);
                            Second.before.setText("people before you:" + Second.beforeNum);
                            Second.flagR = true;
                        }


                    }
                    if(Second.beforeNum>=0){
                        Second.before.setText("people before you:" + Second.beforeNum);
                    }



                    Second.total.setText("The total number of people in this queue is:" + Second.totalNum);


                    if (Second.beforeNum > 0 && Second.flag == true) {
                        Second.before.setText("people before you:" + Second.beforeNum);
                    } else if (Second.beforeNum == 0 && Second.flag == true) {
                        Second.before.setText("It's your time!");
                    }
                }
            });
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）

        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setConnectionTimeout(10);
        conOpt.setKeepAliveInterval(20);
    }



    @Override
    public void onDestroy() {
        stopSelf();
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNormal()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
        }
    };

    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String str1 = new String(message.getPayload());
            if (IGetMessageCallBack != null){
                IGetMessageCallBack.setMessage(str1);
            }
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, "messageArrived:" + str1);
            Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
        }
    };

    /** 判断网络是否连接 */
    private boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getName(), "onBind");
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack){
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    public class CustomBinder extends Binder {
        public MQTTService getService(){
            return MQTTService.this;
        }
    }


    public  void toCreateNotification(String message){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this,MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//3、创建一个通知，属性太多，使用构造器模式

        Notification notification = builder
                .setTicker("测试标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }
}

