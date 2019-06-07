package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Second extends AppCompatActivity implements IGetMessageCallBack {
    public static TextView before;
    public static TextView total;
    private Button add;
    private Button delete;
    private Button refresh;
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;
    public static Boolean flagR = false;
    public static Boolean flagG = false;
    static int totalNum = 0;
    static int beforeNum =0;
    private MyServiceConnection mConnection = new MyServiceConnection();
    static String waitnum = "Queue here!";
    String totalnum ="Total number in the queue:";

    static boolean flag= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        refresh = (Button)findViewById(R.id.refresh) ;

        before = (TextView) findViewById(R.id.number);
        total = (TextView) findViewById(R.id.number2);
        add = (Button) findViewById(R.id.button);
        delete = (Button) findViewById(R.id.delete);
        before = (TextView) findViewById(R.id.number);

        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(Second.this);
        Intent intent = new Intent(this, MQTTService.class);

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MQTTService.subscribe();

                } catch (MqttException e) {
                    e.printStackTrace();
                }
                MQTTService.publish("buttonGet");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == true){
                    AlertDialog.Builder altdial = new AlertDialog.Builder(Second.this);
                    altdial.setMessage("Would you like to cancel this date ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    flag = false;
                                    before.setText("Queue here!!");
                                    MQTTService.publish("buttonDelete");
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.show();
                }
                else{
                    AlertDialog.Builder altdial = new AlertDialog.Builder(Second.this);
                    altdial.setMessage("You are not in this queue!").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    before.setText("Queue here!!");
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.show();

                }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(flag == false){
                    AlertDialog.Builder altdial = new AlertDialog.Builder(Second.this);
                    altdial.setMessage("Would you like to have this date ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    flag = true;
                                    MQTTService.publish("buttonAdd");

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.show();
                }else{
                    AlertDialog.Builder altdial = new AlertDialog.Builder(Second.this);
                    altdial.setMessage("You are already in this queue!!").setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = altdial.create();
                    alert.show();
                }

            }
        });


    }

    @Override
    public void setMessage(String message) {
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

}
