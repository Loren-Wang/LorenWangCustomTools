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
