package com.conjugategames.everestservice;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.harman.everestelite.ANCAwarenessPreset;
import com.harman.everestelite.ANCCtrlListner;
import com.harman.everestelite.Bluetooth;
import com.harman.everestelite.BluetoothListener;
import com.harman.everestelite.CommonListner;
import com.harman.everestelite.HeadPhoneCtrl;

class EverestBluetoothListener implements BluetoothListener {

    private Activity activity;
    private HeadPhoneCtrl headPhoneCtrl;

    EverestBluetoothListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void bluetoothAdapterChangedState(Bluetooth bluetooth, int i, int i1) {

    }

    @Override
    public void bluetoothDeviceBondStateChanged(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, int i, int i1) {

    }

    @Override
    public void bluetoothDeviceConnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket){
        if (headPhoneCtrl != null && headPhoneCtrl.getSocket().equals(bluetoothSocket)) {
            headPhoneCtrl.resetHeadPhoneCtrl(bluetoothSocket);
        } else {
            try {
                headPhoneCtrl.close();
                headPhoneCtrl = null;
            } catch (Exception e) {
            }
            headPhoneCtrl = HeadPhoneCtrl.getInstance(activity, bluetoothSocket);

            headPhoneCtrl.ancCtrl.getBatteryLevel();
//            headPhoneCtrl.commonCtrl.setProgrammableIndexButton();

            headPhoneCtrl.commonCtrl.getProgrammableIndexButton();

            headPhoneCtrl.commonCtrl.get9AxisRawData();

//            get9AxisRawDataReply

            headPhoneCtrl.setAncListner(new ANCCtrlListner() {
                @Override
                public void getBatteryLevelReply(long batteryLevel) {
                    Log.d("EVEREST", "Battery Level: " + batteryLevel);
                }

                // empty method stubs below
                // ..

                @Override
                public void getANCSwitchStateReply(boolean b) {

                }

                @Override
                public void getANCAwarenessPresetReply(ANCAwarenessPreset ancAwarenessPreset) {

                }

                @Override
                public void getLeftANCValueReply(long l) {

                }

                @Override
                public void getRightANCValueReply(long l) {

                }

            });

            headPhoneCtrl.setCommonListner(new CommonListner() {

                @Override
                public void getProgrammableIndexButtonReply(int i) {

                }

                @Override
                public void getConfigModelNumberReply(String s) {

                }

                @Override
                public void getConfigProductNameReply(String s) {

                }

                @Override
                public void getAutoOffFeatureReply(boolean b) {

                }

                @Override
                public void getEnableVoicePromptReply(boolean b) {

                }

                @Override
                public void getFirmwareVersionReply(int i, int i1, int i2) {

                }

                @Override
                public void waitCommandReplyElapsedTime(int i) {

                }

                @Override
                public void headPhoneError(Exception e) {

                }

                @Override
                public void setAutoOffFeatureReply(boolean b) {

                }

                @Override
                public void setEnableVoicePromptReply(boolean b) {

                }

                @Override
                public void getCustomButtonReply() {
                    Log.d("EVERTEST", "button pressed");
                }

                @Override
                public void get9AxisRawDataReply(double v, double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8) {
                    Log.d("EVEREST", "v: " + v);
                    Log.d("EVEREST", "v1: " + v1);
                    Log.d("EVEREST", "v2: " + v2);
                    Log.d("EVEREST", "v3: " + v3);
                    Log.d("EVEREST", "v4: " + v4);
                    Log.d("EVEREST", "v5: " + v5);
                    Log.d("EVEREST", "v6: " + v6);
                    Log.d("EVEREST", "v7: " + v7);
                    Log.d("EVEREST", "v8: " + v8);

                }

                @Override
                public void get9AxisSensorStatusReply(boolean b) {

                }

                @Override
                public void get9AxisPushFrequencyReply(int i) {

                }

                @Override
                public void set9AxisSensorStatusReply(boolean b) {

                }

                @Override
                public void set9AxisPushFrequencyReply(boolean b) {

                }
            });

        }

    }

    @Override
    public void bluetoothDeviceDisconnected(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {
        Log.d("EVEREST", "disconnected");
        headPhoneCtrl = null;
    }

    @Override
    public void bluetoothDeviceDiscovered(Bluetooth bluetooth, BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void bluetoothDeviceFailedToConnect(Bluetooth bluetooth, BluetoothDevice bluetoothDevice, Exception e) {
        Log.d("EVEREST", "failed to connect");
        Log.d("EVEREST", e.getMessage());
        bluetooth.start();
    }
}
