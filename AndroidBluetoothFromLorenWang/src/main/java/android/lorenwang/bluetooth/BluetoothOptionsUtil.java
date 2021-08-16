package android.lorenwang.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.core.content.ContextCompat;

/**
 * 功能作用：蓝牙工具类
 * 初始注释时间： 2018/5/4 15:31
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注册广播接收器（一般在实例初始化的时候就已经自动注册了）---registerReceiver()
 * 取消注册广播接收器（一般只有销毁单例的时候才会取消注册）---unRegisterReceiver()
 * 设置操作回调---setBluetoothOptionsCallback(blueToothOptionsCallback)
 * 开启蓝牙---enableBluetooth()
 * 关闭蓝牙---disableBluetooth()
 * 检测权限---checkPermission()
 * 重置蓝牙---reset()
 * 开启设备扫描---startScanBTDevice(deviceName,deviceAddress)
 * 停止扫描---stopScanBTDevice()
 * 重置扫描---resetScan()
 * 连接指定的蓝颜设备---connectBluetoothDevice(bluetoothDevice,stopReCon)
 * 关闭蓝牙连接---connectBTClose()
 * 向蓝牙设备发送指令并让蓝牙返回相应数据---sendOrderToBTDeviceRead(serviceUUid,characteristicUUid,optValue)
 * 向蓝牙设备写入数据或者单纯的发送指令---sendOrderToBTDeviceWrite(serviceUUid,characteristicUUid,optValue)
 * 向蓝牙设备发送通知命令---sendOrderToBTDeviceNotify(serviceUUid,characteristicUUid)
 * 返回蓝牙是否开启---isBluetoothEnable()
 * 是否连接成功---isConnectSuccess()
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class BluetoothOptionsUtil {
    private final String TAG = getClass().getName();
    private static volatile BluetoothOptionsUtil optionsInstance;
    /**
     * application实例上下文
     */
    private final Context context;
    /**
     * 资源文件读取使用
     */
    private final Resources resources;

    private BluetoothOptionsUtil(Context context) {
        //获取application实例的上下文
        this.context = context.getApplicationContext();
        //初始化资源数据获取实例
        resources = this.context.getResources();
        //初始化蓝牙状态接收者
        blueToothStateReceiver = new BluetoothStateReceiver(blueToothReceiverCallback);
        //注册广播接收器
        registerReceiver();
    }

    public static BluetoothOptionsUtil getInstance(Context context) {
        if (optionsInstance == null && context != null) {
            synchronized (BluetoothOptionsUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new BluetoothOptionsUtil(context);
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 蓝牙状态广播接收器
     */
    private final BluetoothStateReceiver blueToothStateReceiver;
    /**
     * 蓝牙回调
     */
    private BluetoothOptionsCallback blueToothOptionsCallback;
    /**
     * 当前正在连接的蓝牙设备的控制器
     */
    private BluetoothGatt bluetoothGatt;
    /**
     * 当前正在连接的设备
     */
    private BluetoothDevice bluetoothDevice;


    /*-----------------------------------------变量------------------------------------------------/

    /**
     * 是否正在扫描
     */
    private boolean isScan = false;
    /**
     * 扫描的蓝牙设备地址
     */
    private String scanBTAddress;
    /**
     * 扫描的蓝牙设备名称
     */
    private String scanBTName;
    /**
     * 是否正在重连
     */
    private boolean isRecon = false;
    /**
     * 重连次数
     */
    private int reconNum = 0;
    /**
     * 是否连接成功
     */
    private boolean isConnectSuccess = false;


    /*-----------------------------------------私有方法--------------------------------------------*/

    /**
     * 获取蓝牙适配器
     *
     * @return 有权限则会返回相应的适配器
     */
    private BluetoothAdapter getBluetoothAdapter() {
        if (checkPermission()) {
            BluetoothManager bluetoothManager;
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                return bluetoothManager.getAdapter();
            }
        }
        return null;
    }

    /**
     * 扫描未绑定远程设备
     */
    @SuppressLint("MissingPermission")
    private void scanUnBoundBTDevice() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            //开始扫描未绑定设备
            boolean state = bluetoothAdapter.startDiscovery();
            //开启设备扫描回调
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.scanBTDevice(!state, isScan);
            }
            handler.sendEmptyMessageDelayed(PAUSE_SCAN_DEVICE, 5000);
        }
    }

    /**
     * 暂停扫描
     */
    @SuppressLint("MissingPermission")
    private void pauseScanUnBoundBTDevice() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            //开始扫描未绑定设备
            bluetoothAdapter.cancelDiscovery();
            handler.sendEmptyMessageDelayed(RESET_SCAN_DEVICE, 5000);
        }
    }

    /**
     * 内部重连方法
     *
     * @param bluetoothDevice 蓝牙设备
     */
    private void reConBTDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null && !TextUtils.isEmpty(bluetoothDevice.getAddress())) {
            this.bluetoothDevice = bluetoothDevice;
            if (isRecon) {
                Log.i(TAG, resources.getString(R.string.connect_bt_device_recon));
            } else {
                Log.i(TAG, resources.getString(R.string.connect_bt_device_start));
            }
            bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
        }
    }

    /**
     * 根据指定的UUID获取蓝牙设备的通道
     *
     * @param serviceUUid        服务UUID
     * @param characteristicUUid 特征UUID
     * @return 蓝牙设备通道实体
     */
    private BluetoothGattCharacteristic getBTGattCharForUUid(UUID serviceUUid, UUID characteristicUUid) {
        BluetoothGattCharacteristic characteristics = null;
        if (bluetoothGatt != null) {
            if (serviceUUid != null) {
                BluetoothGattService service = bluetoothGatt.getService(serviceUUid);
                if (service != null) {
                    Log.i(TAG, resources.getString(R.string.send_order_search_service) + service);
                    characteristics = service.getCharacteristic(characteristicUUid);
                    if (characteristics != null) {
                        Log.i(TAG, resources.getString(R.string.send_order_search_characteristic).replace("cc", characteristics.toString())
                                .replace("vv", String.valueOf(characteristics.getProperties())));
                    }
                }
            } else {
                Iterator<BluetoothGattService> iterator = bluetoothGatt.getServices().iterator();
                BluetoothGattService gattService;
                BluetoothGattCharacteristic characteristic;
                while (iterator.hasNext()) {
                    gattService = iterator.next();
                    characteristic = gattService.getCharacteristic(characteristicUUid);
                    if (characteristic != null) {
                        Log.i(TAG, resources.getString(R.string.send_order_search_characteristic).replace("cc", characteristic.toString())
                                .replace("vv", String.valueOf(characteristic.getProperties())));
                        return characteristic;
                    }
                }
            }
        }
        return characteristics;
    }

    /*------------------------------------------公有方法-------------------------------------------*/

    /**
     * 设置操作回调
     *
     * @param blueToothOptionsCallback 操作回调
     * @return 当前实例
     */
    public BluetoothOptionsUtil setBluetoothOptionsCallback(BluetoothOptionsCallback blueToothOptionsCallback) {
        this.blueToothOptionsCallback = blueToothOptionsCallback;
        return this;
    }

    /**
     * 开启蓝牙
     */
    @SuppressLint("MissingPermission")
    public synchronized void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Log.i(TAG, resources.getString(R.string.ready_open_bluetooth));
            try {
                //首先直接开启蓝牙
                bluetoothAdapter.enable();
            } catch (Exception e) {
                Log.e(TAG, resources.getString(R.string.ready_open_bluetooth_error));
                context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        }
    }

    /**
     * 关闭蓝牙
     */
    @SuppressLint("MissingPermission")
    public synchronized void disableBluetooth() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Log.i(TAG, resources.getString(R.string.ready_close_bluetooth));
            try {
                //直接关闭蓝牙,关闭蓝牙后续操作由广播接收到系统蓝牙关闭了再进行操作
                bluetoothAdapter.disable();
            } catch (Exception e) {
                Log.e(TAG, resources.getString(R.string.ready_close_bluetooth_error));
                context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        }
    }

    /**
     * 注册蓝牙广播接收器
     * 注册要监听的状态类型如下：
     * android.bluetooth.device.action.PAIRING_REQUEST---配对请求
     * BluetoothDevice.ACTION_FOUND---搜索蓝压设备，每搜到一个设备发送一条广播
     * BluetoothDevice.ACTION_BOND_STATE_CHANGED---远程蓝牙设备状态改变的时候发出这个广播，例如设备被匹配, 或者解除配对
     * BluetoothDevice.ACTION_PAIRING_REQUEST---配对时，发起连接
     * BluetoothDevice.ACTION_CLASS_CHANGED---一个远程设备的绑定状态发生改变时发出广播
     * BluetoothDevice.ACTION_UUID---
     * BluetoothDevice.ACTION_ACL_CONNECTED---与远程设备建立了ACL连接发出的广播
     * BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED---ACL连接即将断开
     * BluetoothDevice.ACTION_ACL_DISCONNECTED---与远程设备断开ACL连接后发出的广播
     * BluetoothDevice.ACTION_DISCOVERY_STARTED---开始搜索
     * BluetoothDevice.ACTION_DISCOVERY_FINISHED---搜索结束。重新搜索时，会先终止搜索
     * BluetoothDevice.ACTION_REQUEST_DISCOVERABLE---
     * BluetoothDevice.ACTION_STATE_CHANGED---连接蓝牙，断开蓝牙
     * BluetoothDevice.ACTION_CONNECTION_STATE_CHANGED---
     * BluetoothDevice.ACTION_LOCAL_NAME_CHANGED---更改蓝牙名称，打开蓝牙时，可能会调用多次
     * BluetoothDevice.ACTION_LOCAL_NAME_CHANGED---
     * BluetoothDevice.ACTION_REQUEST_ENABLE---
     * BluetoothDevice.ACTION_SCAN_MODE_CHANGED---搜索模式改变
     */
    public void registerReceiver() {
        // 注册Receiver来获取蓝牙设备相关的结果
        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";//配对请求
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        intent.addAction(BluetoothDevice.ACTION_UUID);
        intent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intent.addAction(ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intent.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        context.registerReceiver(blueToothStateReceiver, intent);
    }

    /**
     * 取消注册广播接收器
     */
    public void unRegisterReceiver() {
        context.unregisterReceiver(blueToothStateReceiver);
    }

    /**
     * 检测权限
     *
     * @return 是否拥有权限
     */
    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 重置蓝牙
     */
    public void reset() {
        connectBTClose();
        stopScanBTDevice();
    }

    /**
     * 开启设备扫描
     *
     * @param deviceName    要查找的指定设备的名称
     * @param deviceAddress 要查找的指定设备的MAC地址
     */
    @SuppressLint("MissingPermission")
    public synchronized void startScanBTDevice(String deviceName, String deviceAddress) {
        if (isScan) {
            return;
        }
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                isScan = true;
                this.scanBTName = deviceName;
                this.scanBTAddress = deviceAddress;

                //开启设备扫描回调
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.scanBTDevice(false, true);
                }

                //开始扫描已绑定设备
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                if (bondedDevices != null) {
                    Iterator<BluetoothDevice> iterator = bondedDevices.iterator();
                    BluetoothDevice bluetoothDevice;
                    while (iterator.hasNext()) {
                        if (!isScan) {
                            break;
                        }
                        bluetoothDevice = iterator.next();
                        if (bluetoothDevice != null && !TextUtils.isEmpty(bluetoothDevice.getAddress())) {
                            blueToothReceiverCallback.scanFoundBluetoothDevice(bluetoothDevice);
                        }
                    }
                }
                //扫描未绑定设备
                scanUnBoundBTDevice();
                Log.i(TAG, resources.getString(R.string.scan_bt_device_start));
            } else {
                //开启设备扫描回调
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.scanBTDevice(true, isScan);
                }
            }
        } else {
            //开启设备扫描回调
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.scanBTDevice(true, isScan);
            }
        }
    }

    /**
     * 停止扫描
     */
    @SuppressLint("MissingPermission")
    public synchronized void stopScanBTDevice() {
        isScan = false;
        handler.removeMessages(PAUSE_SCAN_DEVICE);
        handler.removeMessages(RESET_SCAN_DEVICE);
        this.scanBTAddress = null;
        this.scanBTName = null;
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            boolean state = bluetoothAdapter.cancelDiscovery();
            //开启设备扫描回调
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.scanBTDevice(!state, isScan);
            }
        } else {
            //开启设备扫描回调
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.scanBTDevice(true, isScan);
            }
        }
        Log.i(TAG, resources.getString(R.string.scan_bt_device_stop));
    }

    /**
     * 重置扫描
     */
    public synchronized void resetScan() {
        stopScanBTDevice();
        startScanBTDevice(this.scanBTName, this.scanBTAddress);
    }

    /**
     * 连接指定的蓝颜设备
     *
     * @param bluetoothDevice 蓝牙设备
     * @param stopReCon       是否结束上一次重连
     */
    public void connectBluetoothDevice(BluetoothDevice bluetoothDevice, boolean stopReCon) {
        if (isRecon && stopReCon) {
            //当正在重连同时需要停止重连的时候执行以下代码
            reconNum = -1;
            handler.removeMessages(CONNECT_BT_DEVICE_RECON);
            reConBTDevice(bluetoothDevice);
        } else if (!isRecon) {
            reConBTDevice(bluetoothDevice);
        }
    }

    /**
     * 关闭蓝牙连接
     */
    public void connectBTClose() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
        isConnectSuccess = false;
        isRecon = false;
        reconNum = 0;
        bluetoothDevice = null;
        bluetoothGatt = null;
    }


    /**
     * 向蓝牙设备发送指令并让蓝牙返回相应数据
     *
     * @param serviceUUid        服务UUID
     * @param characteristicUUid 特征UUID
     */
    @SuppressLint("MissingPermission")
    public void sendOrderToBTDeviceRead(UUID serviceUUid, UUID characteristicUUid, byte[] optValue) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            BluetoothGattCharacteristic characteristic = getBTGattCharForUUid(serviceUUid, characteristicUUid);
            if (characteristic != null) {
                Log.i(TAG, resources.getString(R.string.send_order_characteristic_read_ready));
                characteristic.setValue(optValue);
                boolean state = bluetoothGatt.readCharacteristic(characteristic);
                if (state) {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_read_success));
                } else {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_read_fail));
                    connectBTClose();
                    if (blueToothOptionsCallback != null) {
                        blueToothOptionsCallback.onCharacteristicRead(null, null, null);
                    }
                }
            } else {
                connectBTClose();
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.onCharacteristicRead(null, null, null);
                }
            }
        } else {
            connectBTClose();
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicRead(null, null, null);
            }
        }
    }

    /**
     * 向蓝牙设备写入数据或者单纯发送指令
     *
     * @param serviceUUid        服务UUID
     * @param characteristicUUid 特征UUID
     * @param optValue           要发送的特殊数据指令
     */
    @SuppressLint("MissingPermission")
    public void sendOrderToBTDeviceWrite(UUID serviceUUid, UUID characteristicUUid, byte[] optValue) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            BluetoothGattCharacteristic characteristic = getBTGattCharForUUid(serviceUUid, characteristicUUid);
            if (characteristic != null) {
                Log.i(TAG, resources.getString(R.string.send_order_characteristic_write_ready));
                characteristic.setValue(optValue);
                boolean state = bluetoothGatt.writeCharacteristic(characteristic);
                if (state) {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_write_success));
                } else {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_write_fail));
                    connectBTClose();
                    if (blueToothOptionsCallback != null) {
                        blueToothOptionsCallback.onCharacteristicWrite(null, null, null);
                    }
                }
            } else {
                connectBTClose();
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.onCharacteristicWrite(null, null, null);
                }
            }
        } else {
            connectBTClose();
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicWrite(null, null, null);
            }
        }
    }

    /**
     * 向蓝牙设备发送通知命令
     *
     * @param serviceUUid        服务UUID
     * @param characteristicUUid 特征UUID
     */
    @SuppressLint("MissingPermission")
    public void sendOrderToBTDeviceNotify(UUID serviceUUid, UUID characteristicUUid) {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            BluetoothGattCharacteristic characteristic = getBTGattCharForUUid(serviceUUid, characteristicUUid);
            if (characteristic != null) {
                Log.i(TAG, resources.getString(R.string.send_order_characteristic_notify_ready));
                boolean state = bluetoothGatt.setCharacteristicNotification(characteristic, true);
                if (state) {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_notify_success));
                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    for (BluetoothGattDescriptor dp : descriptors) {
                        dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        bluetoothGatt.writeDescriptor(dp);
                    }
                } else {
                    Log.i(TAG, resources.getString(R.string.send_order_characteristic_notify_fail));
                    connectBTClose();
                    if (blueToothOptionsCallback != null) {
                        blueToothOptionsCallback.onCharacteristicRead(null, null, null);
                    }
                }
            } else {
                connectBTClose();
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.onCharacteristicRead(null, null, null);
                }
            }
        } else {
            connectBTClose();
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicRead(null, null, null);
            }
        }
    }

    /**
     * 返回蓝牙是否开启
     *
     * @return 开启返回true，无权限或者未开启返回false
     */
    @SuppressLint("MissingPermission")
    public boolean isBluetoothEnable() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        //获取蓝牙适配器的时候已经检测过权限，无权限时会直接返回null
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    /**
     * 是否连接成功
     *
     * @return 成功为true
     */
    public boolean isConnectSuccess() {
        return isConnectSuccess;
    }

    /*----------------------------------------回调方法-----------------------------------------*/
    private final int PAUSE_SCAN_DEVICE = 0;//暂停设备扫描
    private final int RESET_SCAN_DEVICE = 1;//重启设备扫描
    private final int CONNECT_BT_DEVICE_RECON = 2;//蓝牙连接异常重新连接
    private final int CONNECT_BT_DEVICE_RECON_MAX_NUM = 17;//蓝牙连接重连次数
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @SuppressLint("MissingPermission")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAUSE_SCAN_DEVICE://停止设备扫描并在指定时间之后再次开启扫描
                    if (isScan) {
                        Log.i(TAG, resources.getString(R.string.scan_bt_device_pause));
                        pauseScanUnBoundBTDevice();
                    }
                    break;
                case RESET_SCAN_DEVICE://开始设备扫描并在指定时间之后再次关闭扫描
                    if (isScan) {
                        Log.i(TAG, resources.getString(R.string.scan_bt_device_reset));
                        scanUnBoundBTDevice();
                    }
                    break;
                case CONNECT_BT_DEVICE_RECON:
                    reConBTDevice(bluetoothDevice);
                    break;
                default:
                    break;
            }
        }
    };


    //蓝牙广播接收器接收到的数据
    private final BluetoothReceiverCallback blueToothReceiverCallback = new BluetoothReceiverCallback() {
        @Override
        public void systemBluetoothStateChange(Integer state) {
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i(TAG, resources.getString(R.string.system_bluetooth_off));
                    if (blueToothOptionsCallback != null) {
                        blueToothOptionsCallback.systemBTDeviceStateChange(false);
                    }
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i(TAG, resources.getString(R.string.system_bluetooth_on));
                    if (blueToothOptionsCallback != null) {
                        blueToothOptionsCallback.systemBTDeviceStateChange(true);
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i(TAG, resources.getString(R.string.system_bluetooth_turning_off));
                    connectBTClose();
                    stopScanBTDevice();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i(TAG, resources.getString(R.string.system_bluetooth_turning_on));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void scanFoundBluetoothDevice(BluetoothDevice bluetoothDevice) {
            if (bluetoothDevice != null && !TextUtils.isEmpty(bluetoothDevice.getAddress())) {
                Log.i(TAG, resources.getString(R.string.scan_bt_device_address) + bluetoothDevice.getAddress());
                if (blueToothOptionsCallback != null) {
                    blueToothOptionsCallback.scanFoundBluetoothDevice(bluetoothDevice);
                }
            }
        }

        @Override
        public void disconnectBtDevice() {
            connectBTClose();
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.disconnectBtDevice();
            }
        }

        @Override
        public void connectBtDevice() {
            stopScanBTDevice();
            isConnectSuccess = true;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.connectBtDevice();
            }
        }
    };
    //设备连接回调
    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            bluetoothGatt = gatt;
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                //获取蓝牙设备信息
                gatt.discoverServices();
            } else if (status == BluetoothGatt.GATT_FAILURE && newState == BluetoothProfile.STATE_DISCONNECTED) {
                //空相应，不做任何处理
                Log.i(TAG, "connect fail");
            } else {
                //防止出现code133错误
                if (gatt != null && status == 133) {
                    gatt.disconnect();
                    gatt.close();
                    bluetoothGatt = null;

                    //开启重连，并做判断，超过一定重连次数则不再进行重连
                    if (reconNum % CONNECT_BT_DEVICE_RECON_MAX_NUM > 0) {
                        isRecon = true;
                        handler.sendEmptyMessage(CONNECT_BT_DEVICE_RECON);
                    } else {
                        isRecon = false;
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onServicesDiscovered(gatt, status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicRead(gatt, characteristic, status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicWrite(gatt, characteristic, status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onCharacteristicChanged(gatt, characteristic);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onDescriptorRead(gatt, descriptor, status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            bluetoothGatt = gatt;
            if (blueToothOptionsCallback != null) {
                blueToothOptionsCallback.onDescriptorWrite(gatt, descriptor, status);
            }
        }
    };
}
