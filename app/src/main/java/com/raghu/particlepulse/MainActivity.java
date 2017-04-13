package com.raghu.particlepulse;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;

import static io.particle.android.sdk.utils.Py.list;


public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button toggleButton;
    private ParticleDevice hal9000;
    private int count = 0;
    private Handler myHandler;
    private TextView textView;
    private Button offView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login);
        toggleButton = (Button) findViewById(R.id.toggle);
        toggleButton.setText("on");
        myHandler = new Handler();
        textView = (TextView) findViewById(R.id.bpmView);
        offView = (Button) findViewById(R.id.off);
        offView.setEnabled(false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async.executeAsync(ParticleCloud.get(v.getContext()), new Async.ApiWork<ParticleCloud, Object>() {
                    @Override
                    public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        particleCloud.logIn("raghu.tjm@gmail.com", "I'mJ'sSR94!!");
                        List<ParticleDevice> devices = particleCloud.getDevices();
                        for (ParticleDevice device : devices) {
                            Log.d("PP", "" + device.getName());
                            if (device.getName().equals("HAL9000")) {
                                hal9000 = device;
                                try {
                                    int resultcode = hal9000.callFunction("startBPM", list("off"));
                                    Log.d("PP","PASS");
                                } catch (ParticleDevice.FunctionDoesNotExistException e) {
                                    Log.d("PP","FAIL");
                                    e.printStackTrace();
                                }
                            }
                        }

                        return -1;
                    }

                    @Override
                    public void onSuccess(Object o) {
                        Toaster.l(MainActivity.this, "Logged in");
                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {
                        Toaster.l(MainActivity.this, "Failed");
                    }
                });
            }
        });

        offView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                offView.setEnabled(false);
                toggleButton.setEnabled(true);

                Async.executeAsync(ParticleCloud.get(v.getContext()), new Async.ApiWork<ParticleCloud, Object>() {

                    @Override
                    public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {

                        try {
                            int resultcode = hal9000.callFunction("startBPM", list("off"));
                            Log.d("PP","PASS");
                        } catch (ParticleDevice.FunctionDoesNotExistException e) {
                            Log.d("PP","FAIL");
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });


                        return null;
                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {

                    }
                });


            }
        });



        toggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                offView.setEnabled(true);
                toggleButton.setEnabled(false);

                Async.executeAsync(ParticleCloud.get(v.getContext()), new Async.ApiWork<ParticleCloud, Object>() {

                    @Override
                    public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {

                        try {
                            int resultcode = hal9000.callFunction("startBPM", list("on"));
                            Log.d("PP","PASS");
                        } catch (ParticleDevice.FunctionDoesNotExistException e) {
                            Log.d("PP","FAIL");
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });


                        return null;
                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {

                    }
                });



            }
        });

        final Handler h = new Handler();
        final int delay = 500; //milliseconds

        final Context context = this;
        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                Async.executeAsync(ParticleCloud.get(context), new Async.ApiWork<ParticleCloud, Object>() {



                    @Override
                    public Object callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        try {
                            if(hal9000!=null) {
                                final Object BPM = hal9000.getVariable("heartbeat");
                                Log.d("PP", "BPM is: " + BPM.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(""+BPM.toString());
                                    }
                                });

                            }
                        } catch (ParticleDevice.VariableDoesNotExistException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onFailure(ParticleCloudException exception) {

                    }
                });
                h.postDelayed(this, delay);
            }
        }, delay);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
