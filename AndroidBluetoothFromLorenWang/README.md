安卓蓝牙相关框架库

# BluetoothOptionsUtil
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

# BluetoothOptionsCallback
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


# 注意
- 在单例初始化的时候首先会初始化一个蓝牙状态接收者的实例，然后根据接收者状态实例注册广播接收器，注册类型如下：

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

- 因为很多的地方都要使用蓝牙适配器，即BluetoothAdapter，所以在获取适配器前都做了一层蓝牙权限验证，所以在调用相关功能前要注意权限是否获取

# 流程：
- 初始化单例
- 设置状态回调
- 开启蓝牙
- 开启成功后开启设备扫描，可以根据设备名称或设备MAC地址扫描
- 如果正在扫描则退出，否则检测蓝牙权限并获取蓝牙适配器
- 根据蓝牙状态以及适配器状态通过callback回调返回给用户
- 首先扫描已绑定的蓝牙设备，判断器内部是否有要查找的设备，有则返回，没有则继续向下
- 已绑定设备中没有找到要查找的则开始扫描未绑定设备列表，此时则通过一开始注册的监听开始查找数据并监听蓝牙处理，同时每五秒重连一次蓝牙
- 查找成功之后则可以通过设备信息以及连接函数链接设备
- 连接成功后则可以通过相应的发送函数发送数据，需要返回的会在callback中处理
