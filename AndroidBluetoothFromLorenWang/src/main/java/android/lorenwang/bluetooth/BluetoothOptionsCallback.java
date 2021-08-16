package android.lorenwang.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * 功能作用：蓝牙操作回调
 * 初始注释时间： 2018/5/4 15:35
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 蓝牙设备状态改变---systemBTDeviceStateChange(isOpen)
 * 蓝牙扫描回调---scanBTDevice(isHaveError,isScan)
 * 蓝牙设备状态改变 已经找到蓝牙设备---scanFoundBluetoothDevice(bluetoothDevice)
 * 远程蓝牙设备断开连接---disconnectBtDevice()
 * 远程蓝牙设备已连接---connectBtDevice()
 * 蓝牙设备服务刷新完成---onServicesDiscovered(gatt,status)
 * 特征读命令发送返回---onCharacteristicRead(gatt,characteristic,status)
 * 特征写命令发送返回---onCharacteristicWrite(gatt,characteristic,status)
 * 特征改变回调---onCharacteristicChanged(gatt,characteristic)
 * 描述读命令发送返回---onDescriptorRead(gatt,descriptor,status)
 * 描述写命令发送返回---onDescriptorWrite(gatt,descriptor,status)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public interface BluetoothOptionsCallback {
    /**
     * 蓝牙设备状态改变
     *
     * @param isOpen 蓝牙开启状态
     */
    void systemBTDeviceStateChange(boolean isOpen);

    /**
     * 蓝牙扫描回调
     *
     * @param isHaveError 扫描是否有异常
     * @param isScan      当前是否是扫描状态
     */
    void scanBTDevice(boolean isHaveError, boolean isScan);

    /**
     * 蓝牙设备状态改变 已经找到蓝牙设备
     *
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
     *
     * @param gatt   gatt协议
     * @param status 蓝牙状态
     */
    void onServicesDiscovered(BluetoothGatt gatt, int status);

    /**
     * 特征读命令发送返回
     *
     * @param gatt           当前正在连接的蓝牙设备的控制器
     * @param characteristic 远端返回的特征信息
     * @param status         {@link BluetoothGatt#GATT_SUCCESS} if the read operation was completed
     */
    void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);

    /**
     * 特征写命令发送返回
     *
     * @param gatt           当前正在连接的蓝牙设备的控制器
     * @param characteristic 远端返回的特征信息
     * @param status         The result of the write operation BluetoothGatt.GATT_SUCCESS if the operation succeeds.
     */
    void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, Integer status);

    /**
     * 特征改变回调
     *
     * @param gatt           当前正在连接的蓝牙设备的控制器
     * @param characteristic 远端返回的特征信息
     */
    void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

    /**
     * 描述读命令发送返回
     *
     * @param gatt       当前正在连接的蓝牙设备的控制器
     * @param descriptor 从关联的远程设备读取的描述符。
     * @param status     {@link BluetoothGatt#GATT_SUCCESS} if the read operation was completed
     */
    void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);

    /**
     * 描述写命令发送返回
     *
     * @param gatt       当前正在连接的蓝牙设备的控制器
     * @param descriptor 从关联的远程设备读取的描述符。
     * @param status     The result of the write operation BluetoothGatt.GATT_SUCCESS if the operation succeeds.
     */
    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, Integer status);
}
