package com.lorenwang.test.android.activity.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.lorenwang.bluetooth.BluetoothOptionsUtil
import android.lorenwang.bluetooth.BluetoothOptionsCallback
import android.os.Bundle
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.R

/**
 * 功能作用：蓝牙测试页面
 * 初始注释时间： 2020/4/7 2:22 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
 */
class BluetoothActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_bluetooth)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        //蓝牙操作回调
        BluetoothOptionsUtil.getInstance(this).setBluetoothOptionsCallback(object :
            BluetoothOptionsCallback {
            override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int?) {
            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int?) {
            }

            override fun scanBTDevice(isHaveError: Boolean, isScan: Boolean) {
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            }

            override fun systemBTDeviceStateChange(isOpen: Boolean) {

            }

            override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int?) {
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            }

            override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int?) {
            }

            override fun disconnectBtDevice() {
            }

            override fun connectBtDevice() {
            }

            override fun scanFoundBluetoothDevice(bluetoothDevice: BluetoothDevice?) {
            }
        })
    }
}
