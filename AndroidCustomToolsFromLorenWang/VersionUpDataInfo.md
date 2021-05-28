**V3.1.9**

    AtlwNotificationUtil---创建通知渠道--createNotificationChannel(channelId,name,desc,importance)
    AtlwNotificationUtil---设置进度--setProgress(channelId,notificationId,title,content,smallIconRes,maxProgress,currentProgress,intent)
    AtlwNotificationUtil---移除指定的通知--removeNotification(notificationId)
    AtlwNotificationUtil---移除全部的通知--removeAllNotification()

**V3.1.8**

    AtlwResourcesUtil---获取字体文件的typeFace--getTypeFace(typeFacePath)
    AtlwMobileSystemInfoUtil---获取wifi的mac地址，适配到android Q--getMac()
    AtlwFileOptionUtil---获取app缓存文件大小--getAppCacheFileSize(isCheckPermission)
    AtlwFileOptionUtil---清除app缓存--clearAppCacheFile(isCheckPermission)

**V3.1.7**

    AtlwMobileOptionsUtil---复制内容到剪贴板--copyContentToClipboard(label,content)
    AtlwSpannableClickBean---新增字体加粗处理
        
**V3.1.6**

    AtlwImageCommonUtils---新增根据比例填充背景图片--fillBgAspectRatio(bitmap, ratio, color)
        
**V3.1.5**

    AtlwImageCommonUtils---（修复异常函数）
        
**V3.1.4**

    AtlwImageCommonUtils---（新增函数）
        添加水印到位图中--addWatermarkBitmap(bitmap,textSize,textColor,text,width,height,rotationAngle)
        生成水印--generateWatermarkBitmap(textSize,textColor,text,width,height,rotationAngle)
        在画布上绘制水印--addWatermarkBitmap(canvas,textSize,textColor,text,width,height,rotationAngle)
        在画布上绘制水印--addWatermarkBitmap(canvas,paint,text,width,height,rotationAngle)
        图片格式转换--coverImage(path,savePath,format)
        
**V3.1.3**

    AtlwResourseUtil---（资源相关工具类）
        获取资源字节--getAssets(assetsName)
    
**V3.1.1**

    AtlwResourseUtil---（资源相关工具类）
        获取浮点资源数据--getFloat(resId) 
    
**V3.0.9**

    AtlwSpannableUtil--新增Spannable文本处理工具类
    
    
**V3.0.7**

    AtlwImageCommonUtil--修改水印处理工具类


**V3.0.6**

    新增定位地址信息返回

**V3.0.3**

    修改定位回调逻辑
    新增服务是否运行判断工具


**V3.0.3**

*AtlwScreenUtil*

    修改屏幕高度机型适配


**V3.0.1**

*AtlwActivityUtil*

    修改软键盘显示隐藏调用


**V3.0.0**

1、所有工具类名称修改

*AtlwActivityUtil*

    升级权限请求实例调用--goToRequestPermissions
    新增修改页面旋转方向--changeActivityScreenOrientation
    新增参数页面当前是否是横屏显示--isPageLandscape

*AtlwLocationUtil*

    新增检测权限相关--checPermissions
    新增使用网络定位--startNetworkPositioning
    新增使用设备定位--startDevicesPositioning
    新增使用精度定位--startAccuratePositioning
    新增停止循环定位--stopLoopPositioning
    新增请求权限--requestPermissions
    新增设置定位库类型--locationLibraryType

*AtlwFileOptionUtil*

    新增通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件--writeToFile
