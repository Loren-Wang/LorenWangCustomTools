package android.lorenwang.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.RequiresPermission;

/**
 * 功能作用：蓝牙广播的回调
 * 初始注释时间： 2018/5/4 16:20
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
interface BluetoothReceiverCallback {
    /**
     * 系统蓝牙状态发生了改变
     *
     * @param state 状态值
     */
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    void systemBluetoothStateChange(Integer state);

    /**
     * 扫描到蓝牙设备
     *
     * @param bluetoothDevice 蓝牙设备驱动信息
     */
    void scanFoundBluetoothDevice(BluetoothDevice bluetoothDevice);

    /**
     * 远程蓝牙设备断开连接
     */
    void disconnectBtDevice();

    /**
     * 远程蓝牙设备已连接
     */
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN})
    void connectBtDevice();
}
