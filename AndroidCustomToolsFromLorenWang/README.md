安卓自定义工具类

所有的context上下文实例，最好通过AtlwActivityUtils内的getApplicationContext（context）
方法获取，方便在使用的时候使用APP的上下文实例，减少对于Activity的实例的联系，不过特殊的类似
于必须要使用Activity或者service的实例的地方不要通过该方法转换。

<h3>AtlwActivityUtil---(Activity工具类)

    发起权限请求--goToRequestPermissions(object,permisstions,permissionsRequestCode,permissionRequestCallback)
    接收到权限请求返回--receivePermissionsResult(requestCode,permissions,grantResults)(需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法)
    控制软键盘显示与隐藏--setInputMethodVisibility(activity,view,visibility)
    返回APP级别的实例--getApplicationContext(context)
    允许退出App的判断以及线程--allowExitApp(time)
    检测App版本更新，通过versionName比较--checkAppVersionUpdate(oldVersion, newVersion)
    退出应用--exitApp(activity)
    获得应用是否在前台--isOnForeground()
    获取应用程序名称--getAppName()
    修改页面旋转方向--changeActivityScreenOrientation(activity)
    参数页面当前是否是横屏显示--isPageLandscape(activity)
    是否有服务在运行--isRunSercice(cls)

<h3>AtlwLocationUtil---(AtlwLocationUtil定位库，需要导入相应的定位包或直接使用系统定位)

    检测权限相关--checPermissions(config)
    使用网络定位--startNetworkPositioning(config)
    使用设备定位--startDevicesPositioning(config)
    使用精度定位--startAccuratePositioning(config)
    停止循环定位--stopLoopPositioning()
    请求权限--requestPermissions(context,config)
    设置定位库类型--locationLibraryType(config)

<h3>AtlwFileOptionUtil---(AtlwFileOptionUtil文件操作库)

    读取图片文件并获取字节--readImageFileGetBytes(isCheckPermisstion, isCheckFile, filePath)
    读取文件并获取字节--readBytes(isCheckPermisstion, path/file/inputStream)
    写入文件--writeToFile(isCheckPermisstion, file，inputStream/text/bitmap/buffer,...)
    通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件--writeToFile(intent, saveFile)
    复制单个文件--copyFile(isCheckPermisstion, oldPath, newPath)
    删除文件--deleteFile(isCheckPermisstion, url)
    获取文件大小，单位B--getFileSize(isCheckPermisstion, file, filtrationDir)
    删除文件夹以及目录下的文件--deleteDirectory(isCheckPermisstion, filePath)
    创建文件夹--createDirectory(isCheckPermisstion, path, nowPathIsFile)
    根据正则获取指定目录下的所有文件列表(使用递归扫描方式)--getFileListForMatchRecursionScan(isCheckPermisstion, scanPath, matchRegular)
    根据正则获取指定目录下的所有文件列表(使用队列扫描方式)--getFileListForMatchLinkedQueueScan(isCheckPermisstion, scanPath, matchRegular)
    获取根目录文件夹地址--getBaseStorageDirPath()
    获取App系统文件夹地址--getAppSystemStorageDirPath(applicationId)
    根据uri获取图片文件地址--getUriPath(uri, dbKey)
    获取app缓存文件大小--getAppCacheFileSize(isCheckPermission)
    清除app缓存--clearAppCacheFile(isCheckPermission)

<h3>AtlwImageCommonUtils---（图片处理通用类）

    将图片文件转换为base64字符串--imageFileToBase64String(filePath)
    图片drawable转bitmap--drawableToBitmap(drawable,width,htight)
    获取drawable的宽度--getDrawableWidth(drawable)
    获取drawable的高度--getDrawableHeight(drawable)
    获取圆角bitmap--getRoundedCornerBitmap(bitmap,width,htight,radius)
    获取圆角bitmap--getRoundedCornerBitmap(bitmap,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius)
    获取圆角bitmap--getRoundedCornerBitmap(drawable/bitmap,width,htight,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius)
    获取圆形bitmap--getCircleBitmap(drawable/bitmap,width,height,radius)
    位图压缩，不压缩大小--bitmapCompress(bitmap,format,size)
    位图压缩--bitmapCompressToByte(bitmap,format,size)
    获取位图字节--getBitmapBytes(bitmap)
    十进制颜色值转16进制--toHexEncoding(color)
    图片的缩放方法--zoomImage(bgImage,newWidth,newHeight)
    读取照片exif信息中的旋转角度--readPictureDegree(path)
    旋转指定图片一定的角度--toTurnPicture(img,degree)
    裁剪位图--cropBitmap(bitmap,leftPercentForBitmapWidth,topPercentForBitmapHeight,rightPercentForBitmapWidth,bottomPercentForBitmapHeight)
    从中心裁剪图片到指定的宽高--cropBitmapForCenter(bitmap,cropPercentWidthHeight)
    使背景透明--makeBgTransparent(bitmap)
    合并位图--mergeBitmap(bitmapBg,bitmapTop,topShowWidth,topShowHeight,leftBgPercent,topBgPercent,Float bottomBgPercent)
    图片设置背景--setBitmapBg(bitmap,bgColor,bgContentPadding)
    获取两个位图重叠部分位图--getOverlapBitmap(bottomBitmap,topBitmap,showWidth,showHeight)
    释放bitmap--releaseBitmap(bitmap)
    添加水印到位图中--addWatermarkBitmap(bitmap,textSize,textColor,text,width,height,rotationAngle)
    生成水印--generateWatermarkBitmap(textSize,textColor,text,width,height,rotationAngle)
    在画布上绘制水印--addWatermarkBitmap(canvas,textSize,textColor,text,width,height,rotationAngle)
    在画布上绘制水印--addWatermarkBitmap(canvas,paint,text,width,height,rotationAngle)
    图片格式转换--coverImage(path,savePath,format)
    根据比例填充背景图片--fillBgAspectRatio(bitmap, ratio, color)

<h3>AtlwScreenUtil---（屏幕相关处理类）

    根据手机的分辨率从 dp 的单位 转成为 px(像素)--dip2px(dpValue)
    根据手机的分辨率从 px(像素) 的单位 转成为 dp--px2dip(pxValue)
    将sp值转换为px值，保证文字大小不变--sp2px(spValue)
    将px值转换为sp值，保证文字大小不变--px2sp(pxValue)
    获取屏幕宽度--getScreenWidth()
    获取屏幕高度--getScreenHeight()
    或者状态栏高度--getStatusBarHeight()
    获取需要补充的高度，特殊机型需要补充--getMiSupplementHeight()
    根据宽度获取在屏幕上显示的总的像素值--getShowPixelValueForWidth(layoutShowValue)
    根据高度获取在屏幕上显示的总的像素值--getShowPixelValueForHeight(layoutShowValue)
  
<h3>AtlwSpannableUtil---（Spannable文本处理）

    格式化点击消息数据--paramsClickMessage(message,AtlwSpannableClickBean...)
    格式化消息数据--paramsTagMessage(message,AvlwSpannableTagBean...)
    textview设置字符串，保证spannable的click生效--setSpannableString(textView,SpannableString...)
    
<h3>AtlwResourcesUtil---（资源相关工具类）

    获取浮点资源数据--getFloat(resId) 
    获取资源字节--getAssets(assetsName)
    获取字体文件的typeFace--getTypeFace(typeFacePath)

<h3>AtlwMobileSystemInfoUtil---（手机系统信息工具类）

    获取当前手机系统语言--getSystemLanguage()（返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”）
    获取当前系统上的语言列表(Locale列表)--getSystemLanguageList()
    获取当前手机系统版本号--getSystemVersion()
    获取手机型号--getSystemModel()
    获取手机厂商--getDeviceBrand()
    获取手机系统sdk版本号--getSystemSdkVersion()
    获取手机品牌信息--getMobileBrand()
    获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)--getIMEIInfo()
    获取wifi的mac地址，适配到android Q--getMac()

<h3>二、AtlwSharedPrefUtils---(ndroid prefence文件读写操作工具类)</h3>

<h3>三、AtlwThreadUtils---(线程工具类)

<h3>四、AtlwToastHintUtils---（提示弹窗单例类）

    1、提示自定义view消息
    2、显示提示信息
    3、设置弹窗参数，全局使用，以最后一次变更为准

<h3>五、AtlwCheckUtils---（检查工具类，用来检查各种，属于基础工具类）

    1、检查是否有文件操作权限
    2、检查文件是否存在
    3、检查是否是图片文件
    4、根据传递进来的参数判断是否有该权限
    5、判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
    6、检测App是否安装
    7、检查App是否在运行
    8、检测一个服务是否在后台运行
    9、检查io操作工具类权限以及传入参数
 
<h3> 六、AtlwLogUtils---（日志工具类）
 
 <h3>七、AndJavaCommonUtils---（和java的通用方法）
 
     1、uuid产生器
     2、数组转集合
     3、判断变量是否为空
  
 <h3> 八、AtlwDesktopShortcutUtils---（桌面快捷方式管理类）


  
  <h3>十一、AtlwSimpleBitmapLruCacheUtils---（简单位图lrucache）
  
  <h3>十二、AtlwFlyMessageUtils---（消息传递工具类）
  
  **ps：需要在application初始化的时候注册activity监听注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据**
  
    1、取消注册消息回调记录
    2、发送消息
    3、返回生命周期监听
    4、获取记录存储集合的key
    5、注册消息回调记录
 
 <h3>十三、AtlwMobileOptionsUtils---（手机操作工具类）
 
      1.1、安装应用
      1.2、获取App安装的intent
      1.3、跳转到权限设置页面
      1.4、启动其他应用
      1.pri、获取小米手机的MIUI版本号
      1.pri、跳转到小米App权限设置
      1.pri、跳转到魅族App权限设置
      1.pri、跳转到默认App权限设置页面
      2.1、使设备震动
      2.2、开启相机
      2.3、拨打电话
      2.4、开启图片相册选择
      3.1、获取电源设备锁
      3.2、销毁电源设备锁
      3.3、申请电源设备锁，关闭屏幕
      3.4、释放电源设备锁，唤起屏幕
      4.1、获取传感器管理器实例
      4.2、注册距离传感器监听
      4.3、取消注册距离传感器监听
      5.1、获取系统级别音频管理器
      5.2、使用听筒播放正在播放的音频
      5.3、使用扬声器播放正在播放的音频
      复制内容到剪贴板--copyContentToClipboard(label,content)
  
 <h3> 十四、AtlwMobilePhoneBrandUtils---（手机品牌判断工具类）  
    1、是否是小米手机
    2、判断是否是魅族手机
    3、是否是华为手机
    4、是否是vivo手机
    5、是否是oppo手机
    6、是否是Coolpad手机
    7、是否是samsung手机
    8、是否是Sony手机
    9、是否是LG手机
   

   


<h3>十七、AtlwBrightnessChangeUtils---（亮度相关工具类）

    1、获取当前屏幕亮度
    2、获取当前屏幕亮度
    3、更新手机系统亮度模式
    4、设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
    5、判断Activity界面亮度是否是自动的
    6、保存亮度设置状态
    7、设置屏幕亮度跟随系统
    8、注册亮度观察者
    9、解注册亮度观察者
    10、获取过滤蓝光后的颜色值
    
    
<h4>十八、AtlwViewUtils---（控件工具类）

      1、获取控件的LayoutParams---getViewLayoutParams（view，width，height）
      2、设置控件的宽高---setViewWidthHeight（view，width，height）
      3、设置控件宽高以及margin属性---setViewWidthHeightMargin（view，width，height，l,t,r,b）
      4、对Drawable着色---tintDrawable(drawable,ColorStateList)
      5、设置背景图片着色---setBackgroundTint(view,ColorStateList)
      6、设置图片控件的src资源的着色---setImageSrcTint(imageView,ColorStateList)
      7、设置文本控件的Drawable左上右下图片着色---setTextViewDrawableLRTBTint(textView,ColorStateList)
    
    
<h4>十九、AtlwRecordUtils---（录音工具类）

    1、开始录音
    2、结束录音
    3、取消录音
    4、获取音量值等级级，会根据传递进来的最高等级返回相应的指定等级
    5、是否正在录音
    
<h4>二十、AtlwActivityJumpUtils---（页面跳转工具类）
* 1.1、普通页面跳转
      1.2、带参数页面跳转
      1.3、带参数跳转页面并是否清空栈
      2.1、无动画跳转页面
      2.2、带参数无动画跳转页面
      3.1、后退跳转页面
      3.2、带参数后退跳转页面
      3.3、带参数后退跳转页面并是否清空栈
      4.1、需要进行返回的跳转
      4.2、带请求码跳转并需要返回
      4.3、带参数跳转并需要返回
      4.4、带参数以及请求码跳转并需要返回
      4.5、带参数以及请求码跳转并需要返回并决定是否需要清空栈
      5.1、无动画需要返回跳转页面
      5.2、无动画带请求码需要返回的跳转页面
      5.3、带参数请求码无动画跳转页面并决定是否需要清空栈
      6.1、通用方法-带参数跳转页面并是否清空栈并设置进入退出动画
      6.2、通用方法-带参数跳转页面并设置flag以及设置进入退出动画
      6.3、通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在
      7.1、初始化所有Activity的唯一代码
      7.2、返回Activity的唯一代码
      7.3、调用Activity的overridePendingTransition方法
      8.1、通过地址跳转到网页
