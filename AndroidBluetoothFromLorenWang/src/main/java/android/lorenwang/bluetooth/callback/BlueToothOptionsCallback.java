package android.lorenwang.bluetooth.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * 创建时间： 0004/2018/5/4 下午 3:35
 * 创建人：王亮（Loren wang）
 * 功能作用：蓝牙操作回调
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public interface BlueToothOptionsCallback {
    void systemBTDeviceStateChange(boolean isOpen);//蓝牙设备状态改变
    void scanBTDevice(boolean isHaveError, boolean isScan);
    void scanFoundBlueToothDevice(BluetoothDevice bluetoothDevice);//扫描到蓝牙设备
    void disconnectBtDevice();//远程蓝牙设备断开连接
    void connectBtDevice();//远程蓝牙设备已连接
    void onServicesDiscovered(BluetoothGatt gatt, int status);//蓝牙设备服务刷新完成
    void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);//特征读命令发送返回
    void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);//特征写命令发送返回
    void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);//特征改变回调
    void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);//描述读命令发送返回
    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);//描述写命令发送返回
}
