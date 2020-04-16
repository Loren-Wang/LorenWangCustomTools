package android.lorenwang.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.lorenwang.bluetooth.callback.BluetoothReceiverCallback;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.Objects;

import static android.bluetooth.BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED;
import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED;
import static android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED;
import static android.bluetooth.BluetoothDevice.ACTION_CLASS_CHANGED;
import static android.bluetooth.BluetoothDevice.ACTION_FOUND;
import static android.bluetooth.BluetoothDevice.ACTION_PAIRING_REQUEST;

/**
 * 创建时间： 0004/2018/5/4 下午 3:28
 * 创建人：王亮（Loren wang）
 * 功能作用：蓝牙状态广播接收器
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *         intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索蓝压设备，每搜到一个设备发送一条广播
 intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//远程蓝牙设备状态改变的时候发出这个广播, 例如设备被匹配, 或者解除配对
 intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//配对时，发起连接
 intent.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);//一个远程设备的绑定状态发生改变时发出广播
 intent.addAction(BluetoothDevice.ACTION_UUID);
 intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);//与远程设备建立了ACL连接发出的广播
 intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);//ACL连接即将断开
 intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);//与远程设备断开ACL连接后发出的广播
 intent.addAction(ACTION_PAIRING_REQUEST);
 intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始搜索
 intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //搜索结束。重新搜索时，会先终止搜索
 intent.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
 intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//连接蓝牙，断开蓝牙
 intent.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
 intent.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);//更改蓝牙名称，打开蓝牙时，可能会调用多次
 intent.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
 intent.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
 intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//搜索模式改变
 */
public class BluetoothStateReceiver extends BroadcastReceiver {
    private BluetoothReceiverCallback blueToothReceiverCallback;

    public BluetoothStateReceiver(Context context, @NonNull BluetoothReceiverCallback blueToothReceiverCallback) {
        this.blueToothReceiverCallback = blueToothReceiverCallback;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String TAG = "BluetoothOptions";
        if(action != null) {
            switch (action) {
                case ACTION_FOUND://搜索蓝压设备，每搜到一个设备发送一条广播
                    blueToothReceiverCallback.scanFoundBluetoothDevice((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                    break;
                case ACTION_BOND_STATE_CHANGED://远程蓝牙设备状态改变的时候发出这个广播, 例如设备被匹配, 或者解除配对
                    Log.d(TAG, "远程蓝牙设备状态改变");
                    break;
                case ACTION_PAIRING_REQUEST://配对时，发起连接
                    Log.d(TAG, "发起蓝牙配对请求");
                    break;
                case ACTION_CLASS_CHANGED://一个远程设备的绑定状态发生改变时发出广播
//                Log.d(TAG,"远程蓝牙设备的绑定状态发生改变");
                    break;
                case ACTION_ACL_CONNECTED://与远程设备建立了ACL连接发出的广播
                    Log.d(TAG, "远程蓝牙设备连接成功");
                    blueToothReceiverCallback.connectBtDevice();
                    break;
                case ACTION_ACL_DISCONNECT_REQUESTED://ACL连接即将断开
                    Log.d(TAG, "远程设蓝牙备连接即将断开");
                    break;
                case ACTION_ACL_DISCONNECTED://与远程设备断开ACL连接后发出的广播
                    Log.d(TAG, "远程蓝牙设备已断开");
                    blueToothReceiverCallback.disconnectBtDevice();
                    break;
                case ACTION_DISCOVERY_STARTED://开始搜索
//                Log.d(TAG,"开始搜索远程蓝牙设备");
                    break;
                case ACTION_DISCOVERY_FINISHED: //搜索结束。重新搜索时，会先终止搜索
//                Log.d(TAG,"停止搜索远程蓝牙设备");
                    break;
                case ACTION_STATE_CHANGED://连接蓝牙，断开蓝牙
                    int state = Objects.requireNonNull(intent.getExtras()).getInt(BluetoothAdapter.EXTRA_STATE, -1);
                    //系统蓝牙状态回调
                    blueToothReceiverCallback.systemBluetoothStateChange(state);
                    break;
                case ACTION_CONNECTION_STATE_CHANGED:
                    Log.d(TAG, "连接状态发生改变");
                    break;
                default:
                    break;
            }
        }
    }
}
