package android.lorenwang.bluetooth.callback;

import android.Manifest;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.RequiresPermission;

/**
 * 创建时间： 0004/2018/5/4 下午 4:20
 * 创建人：王亮（Loren wang）
 * 功能作用：蓝牙广播的回调
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public interface BluetoothReceiverCallback {
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    void systemBluetoothStateChange(Integer state);//系统蓝牙状态发生了改变
    void scanFoundBluetoothDevice(BluetoothDevice bluetoothDevice);//扫描到蓝牙设备
    void disconnectBtDevice();//远程蓝牙设备断开连接
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    void connectBtDevice();//远程蓝牙设备已连接
}
