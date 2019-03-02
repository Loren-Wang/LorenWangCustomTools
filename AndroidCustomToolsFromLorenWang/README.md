安卓自定义工具类


<h3>一、ActivityUtils---(Activity工具类)
      
    *      1、去请求权限
    *      2、权限请求结果返回
    *      3、控制软键盘显示与隐藏

<h3>二、SharedPrefUtils---(ndroid prefence文件读写操作工具类)</h3>

<h3>三、ThreadUtils---(线程工具类)

<h3>四、ToastHintUtils---（提示弹窗单例类）

<h3>五、CheckUtils---（检查工具类，用来检查各种，属于基础工具类）

    *      1、检查是否有文件操作权限
    *      2、检查文件是否存在
    *      3、检查是否是图片文件
    *      4、根据传递进来的参数判断是否有该权限
    *      5、判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
    *      6、检测App是否安装
    *      7、检查App是否在运行
    *      8、检测一个服务是否在后台运行
    *      9、检查io操作工具类权限以及传入参数
 
<h3> 六、LogUtils---（日志工具类）
 
 <h3>七、AndJavaCommonUtils---（和java的通用方法）
 
     *      1、uuid产生器
     *      2、数组转集合
     *      3、判断变量是否为空
  
 <h3> 八、DesktopShortcutUtils---（桌面快捷方式管理类）


<h3>九、FileOptionUtils---（文件操作工具类）

    *      1、读取图片文件并获取字节
    *      2、将InputStream写入File\
    *      3、从指定路径的文件中读取Bytes
    *      4、从File中读取Bytes
    *      5、从InputStream中读取Bytes
    *      6、复制单个文件
    *      7、将bitmap写入File
    *      8、获取文件大小，单位B
    *      9、删除文件夹以及目录下的文件
 
 <h3>十、ImageCommonUtils---（图片处理通用类）
 
     *      1、将图片文件转换为base64字符串
     *      2、对Drawable着色
     *      3、设置背景图片着色
     *      4、设置图片控件的src资源的着色
     *      5、设置文本控件的Drawable左上右下图片着色
  
  <h3>十一、SimpleBitmapLruCacheUtils---（简单位图lrucache）
  
  <h3>十二、FlyMessageUtils---（消息传递工具类）
  
  **ps：需要在application初始化的时候注册activity监听注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据**
  
    *      1、取消注册消息回调记录
    *      2、发送消息
    *      3、返回生命周期监听
    *      4、获取记录存储集合的key
    *      5、注册消息回调记录
 
 <h3>十三、MobileOptionsUtils---（手机操作工具类）
 
    *      1、安装应用
    *      2、获取App安装的intent
    *      3、使设备震动
    *      4、拨打电话
    *      5、开启相机
    *      6、开启图片相册选择
  
 <h3> 十四、MobilePhoneBrandUtils---（手机品牌判断工具类）
  
    *      1、是否是小米手机
    *      2、判断是否是魅族手机
    *      3、是否是华为手机
    *      4、是否是vivo手机
    *      5、是否是oppo手机
    *      6、是否是Coolpad手机
    *      7、是否是samsung手机
    *      8、是否是Sony手机
    *      9、是否是LG手机
   
   
   <h3>十五、MobileSystemInfoUtils---（手机系统信息工具类）
   
    *      1、获取当前手机系统语言
    *      2、获取当前系统上的语言列表(Locale列表)
    *      3、获取当前手机系统版本号
    *      4、获取手机型号
    *      5、获取手机厂商
    *      6、获取手机系统sdk版本号
    *      7、获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
    *      8、获取当前网络类型  return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
