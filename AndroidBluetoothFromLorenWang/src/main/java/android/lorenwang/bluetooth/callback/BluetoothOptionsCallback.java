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
public interface BluetoothOptionsCallback {
    /**
     * 蓝牙设备状态改变
     * @param isOpen 蓝牙开启状态
     */
    void systemBTDeviceStateChange(boolean isOpen);

    /**
     * 蓝牙扫描回调
     * @param isHaveError 扫描是否有异常
     * @param isScan 当前是否是扫描状态
     */
    void scanBTDevice(boolean isHaveError, boolean isScan);

    /**
     * 蓝牙设备状态改变 已经倒霉到蓝牙设备
     * @param bluetoothDevice 根据设备名称设备mac地址扫描到的设置
     */
    void scanFoundBluetoothDevice(BluetoothDevice bluetoothDevice);

    /**
     * 远程蓝牙设备断开连接
     */
    void disconnectBtDevice();

    /**
     * 远程蓝牙设备已连接
     */
    void connectBtDevice();

    /**
     * 蓝牙设备服务刷新完成
     * @param gatt gatt协议
     * @param status 蓝牙状态
     */
    void onServicesDiscovered(BluetoothGatt gatt, int status);
    void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);//特征读命令发送返回
    void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);//特征写命令发送返回
    void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);//特征改变回调
    void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);//描述读命令发送返回
    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);//描述写命令发送返回
}
