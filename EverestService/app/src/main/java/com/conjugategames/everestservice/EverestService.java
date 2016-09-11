package com.conjugategames.everestservice;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harman.everestelite.ANCAwarenessPreset;
import com.harman.everestelite.ANCCtrlListner;
import com.harman.everestelite.Bluetooth;
import com.harman.everestelite.BluetoothListener;
import com.harman.everestelite.CommonListner;
import com.harman.everestelite.HeadPhoneCtrl;

import java.io.IOException;

/**
 * Created by steven on 9/10/16.
 */
public class EverestService extends Service implements BluetoothListener {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final Handler handler = new Handler();
    private Bluetooth connector;
    private HeadPhoneCtrl headPhoneCtrl;

    private int numIntent;

    // It's the code we want our Handler to execute to send data
    private Runnable sendData = new Runnable() {
        // the specific method which will be executed by the handler
        public void run() {
            numIntent++;

            // sendIntent is the object that will be broadcast outside our app
            Intent sendIntent = new Intent();

            // We add flags for example to work from background
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_FROM_BACKGROUND|Intent.FLAG_INCLUDE_STOPPED_PACKAGES	);

            // SetAction uses a string which is an important name as it identifies the sender of the itent and that we will give to the receiver to know what to listen.
            // By convention, it's suggested to use the current package name
            sendIntent.setAction("com.conjugategames.everestservice.EverestService");

            // Here we fill the Intent with our data, here just a string with an incremented number in it.
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Intent "+numIntent);
            // And here it goes ! our message is send to any other app that want to listen to it.
            sendBroadcast(sendIntent);

            // In our case we run this method each second with postDelayed
            handler.removeCallbacks(this);
            handler.postDelayed(this, 1000);
        }
    };

    // When service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.d("EVEREST", "onStartCommand()");
        try {
            Log.d("EVEREST","Starting bluetooth");
            connector = new Bluetooth(this, MainActivity.activity, true);
            connector.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        numIntent = 0;
//        // We first start the Handler
//        handler.removeCallbacks(sendData);
//        handler.postDelayed(sendData, 1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        Log.d("EVEREST", "onDestroy(), closing bluetooth");
        connector.close();

    }


    @Override
    public void bluetoothAdapterChangedState(Bluetooth bluetooth, int i, int i1) {
        Log.d("EVEREST", "bluetoothAdapterChangedState");
    }

    @Override
    public void bluetoothDeviceBondStateChanged(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, int i, int i1) {
        Log.d("EVEREST", "bluetoothDeviceBondStateChanged");
    }

    @Override
    public void bluetoothDeviceConnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
        Log.d("EVEREST", "bluetoothDeviceConnected");
        if (headPhoneCtrl != null && headPhoneCtrl.getSocket().equals(bluetoothSocket)) {
            headPhoneCtrl.resetHeadPhoneCtrl(bluetoothSocket);
        } else {
            try {
                headPhoneCtrl.close();
                headPhoneCtrl = null;
            } catch (Exception e) {
            }
            headPhoneCtrl = HeadPhoneCtrl.getInstance(MainActivity.activity, bluetoothSocket);
//            headPhoneCtrl.ancCtrl.getBatteryLevel();
            headPhoneCtrl.commonCtrl.set9AxisSensorStatus(true);
            headPhoneCtrl.commonCtrl.set9AxisPushFrequency(200);
            headPhoneCtrl.commonCtrl.get9AxisRawData();

            headPhoneCtrl.setAncListner(new ANCCtrlListner() {
                @Override
                public void getANCSwitchStateReply(boolean b) {
                    Log.d("EVEREST","getANCSwitchStateReply("+b+")");
                }

                @Override
                public void getANCAwarenessPresetReply(ANCAwarenessPreset ancAwarenessPreset) {
                    Log.d("EVEREST","getANCAwarenessPresetReply("+ancAwarenessPreset.toString()+")");
                }

                @Override
                public void getLeftANCValueReply(long l) {
                    Log.d("EVEREST","getLeftANCValueReply("+l+")");
                }

                @Override
                public void getRightANCValueReply(long l) {
                    Log.d("EVEREST","getRightANCValueReply("+l+")");
                }

                @Override
                public void getBatteryLevelReply(long l) {
                    Log.d("EVEREST","getBatteryLevelReply("+l+")");
                }
            });


            headPhoneCtrl.setCommonListner(new CommonListner() {
                @Override
                public void getProgrammableIndexButtonReply(int i) {
                    Log.d("EVEREST","getProgrammableIndexButtonReply("+i+")");
                }

                @Override
                public void getConfigModelNumberReply(String s) {
                    Log.d("EVEREST","getConfigModelNumberReply("+s+")");
                }

                @Override
                public void getConfigProductNameReply(String s) {
                    Log.d("EVEREST","getConfigProductNameReply("+s+")");
                }

                @Override
                public void getAutoOffFeatureReply(boolean b) {
                    Log.d("EVEREST","getAutoOffFeatureReply("+b+")");
                }

                @Override
                public void getEnableVoicePromptReply(boolean b) {
                    Log.d("EVEREST","getEnableVoicePromptReply("+b+")");
                }

                @Override
                public void getFirmwareVersionReply(int i, int i1, int i2) {
                    Log.d("EVEREST","getFirmwareVersionReply("+i+","+i1+","+i2+")");
                }

                @Override
                public void waitCommandReplyElapsedTime(int i) {
                    Log.d("EVEREST","waitCommandReplyElapsedTime("+i+")");
                }

                @Override
                public void headPhoneError(Exception e) {
                    Log.d("EVEREST","headPhoneError("+e.toString()+")");
                }

                @Override
                public void setAutoOffFeatureReply(boolean b) {
                    Log.d("EVEREST","setAutoOffFeatureReply("+b+")");
                }

                @Override
                public void setEnableVoicePromptReply(boolean b) {
                    Log.d("EVEREST","setEnableVoicePromptReply("+b+")");
                }

                @Override
                public void getCustomButtonReply() {
                    Log.d("EVEREST","getCustomButtonReply()");
                }

                @Override
                public void get9AxisRawDataReply(double v, double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8) {
                    Log.d("EVEREST", "get9AxisRawDataReply(" + v + "," + v1 + "," + v2 + "," + v3 + "," + v4 + "," + v5 + "," + v6 + "," + v7 + "," + v8);
                }

                @Override
                public void get9AxisSensorStatusReply(boolean b) {
                    Log.d("EVEREST","get9AxisSensorStatusReply("+b+")");
                }

                @Override
                public void get9AxisPushFrequencyReply(int i) {
                    Log.d("EVEREST","get9AxisPushFrequencyReply("+i+")");
                }

                @Override
                public void set9AxisSensorStatusReply(boolean b) {
                    Log.d("EVEREST","set9AxisSensorStatusReply("+b+")");
                }

                @Override
                public void set9AxisPushFrequencyReply(boolean b) {
                    Log.d("EVEREST","set9AxisPushFrequencyReply("+b+")");
                }
            });
        }
    }

    @Override
    public void bluetoothDeviceDisconnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
        Log.d("EVEREST", "bluetoothDeviceDisconnected");
    }

    @Override
    public void bluetoothDeviceDiscovered(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
        Log.d("EVEREST", "bluetoothDeviceDiscovered");

    }

    @Override
    public void bluetoothDeviceFailedToConnect(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, Exception e) {
        Log.d("EVEREST", "bluetoothDeviceFailedToConnect");
    }
}
