安卓自定义工具类

**使用要求：** 必须调用一下两个函数，否则可能导致崩溃，部分函数可以单独操作处理

1、initAndroidCustomTools(Application nowApplication, boolean isDebug, String debugLogFileDirSavePath)

2、AtlwConfig.registerActivityLifecycleCallbacks(Application application)

**其他按需引入的库**
compileOnly 'org.jetbrains:annotations:19.0.0'
compileOnly "androidx.appcompat:appcompat:${rootProject.ext.libVersion.appcompat_version}"
compileOnly "androidx.constraintlayout:constraintlayout:${rootProject.ext.libVersion.constraintlayout_version}"
compileOnly "androidx.recyclerview:recyclerview:${rootProject.ext.libVersion.recyclerview_version}"
//官方变色库
compileOnly 'com.readystatesoftware.systembartint:systembartint:1.0.3'
//fresco图片加载
```java
compileOnly "com.facebook.fresco:fresco:${rootProject.ext.libVersion.fresco_version}"
compileOnly "com.facebook.fresco:animated-gif:${rootProject.ext.libVersion.fresco_version}"
//glide图片加载
compileOnly "com.github.bumptech.glide:glide:${rootProject.ext.libVersion.glide_version}"
//Picasso图片加载
compileOnly "com.squareup.picasso:picasso:${rootProject.ext.libVersion.picasso_version}"
//imageLoad加载库
compileOnly 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
// 高斯模糊工具类
compileOnly 'net.qiujuer.genius:graphics:2.1.1'
//高德定位库
compileOnly 'com.amap.api:location:latest.integration'
//java工具类库
compileOnly project(':JavaCustomToolsFromLorenWang')
//数据格式化
compileOnly project(':JavaDataParseFromLorenWang')
compileOnly "com.google.code.gson:gson:${rootProject.ext.libVersion.gson_version}"
```

**提示：**

1、所有的context上下文实例，最好通过AtlwActivityUtils内的getApplicationContext（context）方法获取，方便在使用的时候使用APP的上下文实例，减少对于Activity的实例的联系，不过特殊的类似 于必须要使用Activity或者service的实例的地方不要通过该方法转换。

<h3>AtlwActivityJumpUtil---(activity页面跳转工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 普通页面跳转 | jump(Context old, Class<?> cls) | void |
| 带参数页面跳转 | jump(Context old, Class<?> cls, Bundle bundle) | void |
| 带参数跳转页面并是否清空栈 | jump(Context old, Class<?> cls, Bundle bundle, boolean clearTop) | void |
| 无动画跳转页面 | jumpNoAnim(Context old, Class<?> cls) | void |
| 带参数无动画跳转页面 | jumpNoAnim(Context old, Class<?> cls, Bundle bundle, boolean clearTop) | void |
| 后退跳转页面 | jumpBack(Context old, Class<?> cls) | void |
| 带参数后退跳转页面 | jumpBack(Context old, Class<?> cls, Bundle bundle) | void |
| 带参数后退跳转页面并是否清空栈 | jumpBack(Context old, Class<?> cls, Bundle bundle, boolean clearTop) | void |
| 需要进行返回的跳转 | jumpForResult(Context old, Class<?> cls) | void |
| 带请求码跳转并需要返回 | jumpForResult(Context old, Class<?> cls, Integer requestCode) | void |
| 带参数跳转并需要返回 | jumpForResult(Context old, Class<?> cls, Bundle bundle) | void |
| 带参数以及请求码跳转并需要返回 | jumpForResult(Context old, Class<?> cls, Integer requestCode, Bundle bundle) | void |
| 带参数以及请求码跳转并需要返回并决定是否需要清空栈 | jumpForResult(Context old, Class<?> cls, Integer requestCode, Bundle bundle, boolean clearTop) | void |
| 无动画需要返回跳转页面 | jumpForResultNoAnim(Context old, Class<?> cls) | void |
| 无动画带请求码需要返回的跳转页面 | jumpForResultNoAnim(Context old, Class<?> cls, Integer requestCode) | void |
| 带参数请求码无动画跳转页面并决定是否需要清空栈 | jumpForResultNoAnim(Context old, Class<?> cls, Integer requestCode, Bundle bundle, boolean clearTop) | void |
| 通过地址跳转到网页 | jumpToWeb(Activity activity, String url) | void |
| 通用方法-带参数跳转页面并是否清空栈并设置进入退出动画 | jump(Context old, Class<?> cls, Bundle bundle, boolean clearTop, @AnimRes Integer enterAnim, @AnimRes Integer exitAnim) | void |
| 通用方法-带参数跳转页面并设置flag以及设置进入退出动画 | jump(Context old, Class<?> cls, Bundle bundle, Integer flag, Integer enterAnim, Integer exitAnim) | void |
| 通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在 | jump(Context old, Class<?> cls, Bundle bundle, Integer flag, Integer requestCode, Integer enterAnim, Integer exitAnim) | void |
| 初始化所有Activity的唯一代码 | initActivityCode() | void |
| 返回Activity的唯一代码 | getActivityCode(Class<?> cls) | void |
| 调用Activity的overridePendingTransition方法 | overridePendingTransition(Activity activity, Integer enterAnim, Integer exitAnim) | void |
| 跳转到应用市场 | jumpApplicationMarket(Activity activity, String marketPkg, String appPkg) | void |

<br> 

-----
<br>

<h3>AtlwActivityUtil---(Activity工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 发起权限请求 | goToRequestPermissions(@NotNull Object context, @NonNull String[] permisstions, int permissionsRequestCode, AtlwPermissionRequestCallback permissionRequestCallback) | void |
| 接收到权限请求返回 | receivePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)(需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法) | void |
| 控制软键盘显示与隐藏 | setInputMethodVisibility(Activity activity, View view, int visibility) | void |
| 返回APP级别的实例 | allowExitApp(long time) | Context |
| 允许退出App的判断以及线程 | allowExitApp(time) | boolean |
| 检测App版本更新，通过versionName比较 | checkAppVersionUpdate(String oldVersion, String newVersion) | boolean |
| 退出应用 | exitApp(Activity activity) | void |
| 获得应用是否在前台 | isOnForeground() | boolean |
| 获取应用程序名称 | getAppName() | String |
| 修改页面旋转方向 | changeActivityScreenOrientation(@NotNull Activity activity) | void |
| 参数页面当前是否是横屏显示 | isPageLandscape(@NotNull Activity activity) | boolean |
| 是否有服务在运行 | isRunService(@NotNull Class<T> cls) | boolean |

<br> 

-----
<br>

<h3>AtlwBrightnessChangeUtil---(安卓端亮度调节工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取当前屏幕亮度(0-1) | getScreenBrightness() | float |
| 获取当前屏幕亮度(0-1) | getScreenBrightness(Activity activity) | float |
| 更新手机系统亮度模式 | updateMobileSystemBrightnessMode(boolean isAuto) | void |
| 更新手机系统亮度模式 | updateMobileSystemBrightnessMode(Activity activity, boolean isAuto) | void |
| 设置亮度 | setBrightness(float brightness, boolean isCallback) | void |
| 设置亮度 | setBrightness(Activity activity, float brightness, boolean isCallback) | void |
| 判断Activity界面亮度是否是自动的 | isActivityAutoBrightness() | boolean |
| 判断Activity界面亮度是否是自动的 | isActivityAutoBrightness(Activity activity) | boolean |
| 判断是否开启了自动亮度调节 | isMobileSystemAutoBrightness() | boolean |
| 判断是否开启了自动亮度调节 | isMobileSystemAutoBrightness(Activity activity) | boolean |
| 保存亮度设置状态 | saveBrightnessToMobileSystem(int brightness) | void |
| 保存亮度设置状态 | saveBrightnessToMobileSystem(Activity activity, int brightness) | void |
| 设置屏幕亮度跟随系统 | setBrightnessFollowMobileSystem() | void |
| 设置屏幕亮度跟随系统 | setBrightnessFollowMobileSystem(Activity activity) | void |
| 注册亮度观察者 | registerBrightObserver(Activity activity, AtlwBrightnessChangeContentObserver mBrightObserver) | void |
| 解注册亮度观察者 | unregisterBrightObserver(Activity activity) | void |
| 获取过滤蓝光后的颜色值 | getColor(int blueFilterPercent) | int |

<br> 

-----
<br>

<h3>AtlwNotificationUtil---（通知工具类）

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 创建通知渠道 | createNotificationChannel(@NotNull String channelId, @NotNull String name, String desc, int importance) | String |
| 设置进度 | setProgress(@NotNull String channelId, int notificationId, @NotNull String title, @NotNull String content, @DrawableRes int smallIconRes, int maxProgress, int currentProgress, PendingIntent intent) | void |
| 移除指定的通知 | removeNotification(int notificationId) | void |
| 移除全部的通知 | removeAllNotification() | void |

<br> 

-----
<br>

<h3>AtlwResourcesUtil---（资源相关工具类）

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取浮点资源数据 | getFloat(@IntegerRes int resId) | float |
| 获取资源字节 | getAssets(@NotNull String assetsName) | byte[] |
| 获取字体文件的typeFace | getTypeFace(@NotNull String typeFacePath) | Typeface |

<br> 

-----
<br>

<h3>AtlwScreenUtil---（屏幕相关处理类）

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 根据手机的分辨率从 dp 的单位 转成为 px(像素) | dip2px(float dpValue) | float |
| 根据手机的分辨率从 px(像素) 的单位 转成为 dp | px2dip(float pxValue) | float |
| 将sp值转换为px值，保证文字大小不变 | sp2px(float spValue) | float |
| 将px值转换为sp值，保证文字大小不变 | px2sp(float pxValue) | float |
| 获取屏幕宽度 | getScreenWidth() | int |
| 获取屏幕高度 | getScreenHeight() | int |
| 或者状态栏高度 | getStatusBarHeight() | int |
| 获取需要补充的高度，特殊机型需要补充 | getMiSupplementHeight() | int |
| 根据宽度获取在屏幕上显示的总的像素值 | getShowPixelValueForWidth(int layoutShowValue) | int |
| 根据高度获取在屏幕上显示的总的像素值 | getShowPixelValueForHeight(int layoutShowValue) | int |

<br> 

-----
<br>

<h3>AtlwSharedPrefUtil---（Android SharedPreference文件读写操作工具类）

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取指定名称的共享存储（模式为私有） | getSharedPreferences(String name) | SharedPreferences |
| 获取指定名称指定模式的共享存储 | getSharedPreferences(String name, int mode) | SharedPreferences |
| 清除当前默认共享存储 | clear() | boolean |
| 清除指定共享存储 | clear(SharedPreferences pref) | boolean |
| 移除默认key的共享存储内容 | remove(String key) | boolean |
| 移除指定存储的key的共享存储内容 | remove(SharedPreferences pref, String key) | boolean |
| 默认共享存储是否包含该key内容 | contains(String key) | boolean |
| 指定存储的key的共享存储是否存在 | contains(SharedPreferences pref, String key) | boolean |
| 获取默认共享存储整型数据 | getInt(String key, int defValue) | int |
| 获取共享存储整型数据 | getInt(SharedPreferences pref, String key, int defValue) | int |
| 获取默认共享存储长整型数据 | getLong(String key, long defValue) | long |
| 获取共享存储长整型数据 | getLong(SharedPreferences pref, String key, long defValue) | long |
| 获取默认共享存储字符串数据 | getString(String key, String defValue) | String |
| 获取共享存储字符串数据 | getString(SharedPreferences pref, String key, String defValue) | String |
| 获取默认共享存储Set<String>数据 | getStringSet(String key, Set<String> defValue) | Set<String> |
| 获取共享存储Set<String>数据 | getStringSet(SharedPreferences pref, String key, Set<String> defValue) | Set<String> |
| 获取默认共享存储布尔值数据 | getBoolean(String key, boolean defValue) | boolean |
| 获取共享存储布尔值数据 | getBoolean(SharedPreferences pref, String key, boolean defValue) | boolean |
| 向默认共享存储整型数据 | putInt(String key, int value) | boolean |
| 向指定共享存储整型数据 | putInt(SharedPreferences pref, String key, int value) | boolean |
| 向默认共享存储长整型数据 | putLong(String key, long value) | boolean |
| 向指定共享存储长整型数据 | putLong(SharedPreferences pref, String key, long value) | boolean |
| 向默认共享存储字符串数据 | putString(String key, String value) | boolean |
| 向指定共享存储字符串数据 | putString(SharedPreferences pref, String key, String value) | boolean |
| 向默认共享存储Set<String>数据 | putStringSet(String key, Set<String> value) | boolean |
| 向指定共享存储Set<String>数据 | putStringSet(SharedPreferences pref, String key, Set<String> value) | boolean |
| 向默认共享存储布尔值数据 | putBoolean(String key, Boolean value) | boolean |
| 向指定共享存储布尔值数据 | putBoolean(SharedPreferences pref, String key, Boolean value) | boolean |
| 插入用户相关记录,格式id_time_value | putUserRecord(@NotNull String id, @NotNull String key, @NotNull String value) | boolean |
| 获取用户相关记录,格式id_time_value | getUserRecord(@NotNull String id, @NotNull String key, boolean clearOld, boolean today) | String |
| 插入每日相关记录 | putDayRecord(String key) | boolean |
| 获取每日相关记录,格式id_time_value | getDayRecord(String key) | String |

<br> 

-----
<br>

<h3>AtlwSpannableUtil---(Spannable文本处理)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 格式化点击消息数据 | paramsClickMessage(@NonNull String msg, AtlwSpannableClickBean... dataBeans) | SpannableString |
| 格式化点击消息数据 | paramsClickMessage(@NonNull String msg, ArrayList<AtlwSpannableClickBean> dataBeans) | SpannableString |
| 格式化消息数据 | paramsTagMessage(@NonNull String msg, AvlwSpannableTagBean... dataBeans) | SpannableString |
| 格式化消息数据 | paramsTagMessage(@NonNull String msg, @NonNull ArrayList<AvlwSpannableTagBean> dataBeans) | SpannableString |
| textview设置字符串，保证spannable的click生效 | setSpannableString(@NotNull TextView textView, @NotNull SpannableString string) | void |
| textview设置字符串，保证spannable的click生效 | setSpannableString(@NotNull TextView textView, @NotNull SpannableStringBuilder string) | void |

<br> 

-----
<br>

<h3>AtlwSpannableUtil---(Spannable文本处理)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 修改状态栏为全透明 | transparencyBar(Activity activity) | void |
| 修改状态栏颜色，支持4.4以上版本 | setStatusBarColor(Activity activity, int color) | void |
| 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色 | setStatusBarLightMode(Activity activity, boolean isFullscreen) | void |
| 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色 | setStatusBarDarkMode(Activity activity, boolean isFullscreen) | void |

<br> 

-----
<br>

<h3>AtlwThreadUtil---(安卓线程处理单例)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取UI线程handler | getUiThreadHandler() | Handler | 
| 获取子线程handler | getChildThreadHandler() | Handler |
| 当前线程是否是主线程 | isRunningOnUiThread() | boolean |
| 当前线程是否是子线程 | isRunningOnChildThread() | boolean |
| 发送到主线程运行 | postOnUiThread(Runnable task) | void |
| 间隔指定时间后再主线程运行 | postOnUiThreadDelayed(Runnable task, Long delayMillis) | void |
| 发送到主线程运行 | postOnUiThread(FutureTask<T> task) | FutureTask<T> |
| 如果当前是主线程则直接运行，否则发送到主线程去运行 | runOnUiThread(Runnable r) | void |
| 发送到子线程运行 | postOnChildThread(Runnable task) | void |
| 间隔指定时间后再子线程运行 | postOnChildThreadDelayed(Runnable task, Long delayMillis) | void |
| 发送到子线程运行 | postOnChildThread(FutureTask<T> task) | FutureTask<T> |
| 如果当前是子线程则直接运行，否则发送到子线程去运行 | runOnChildThread(Runnable r) | void |
| 在主、子线程中移除指定的runnable | removeRunnable(Runnable runnable) | void |
| 在主、子线程中移除指定的runnable | removeRunnableForChild(Runnable runnable) | void |
| 在主、子线程中移除指定的runnable | removeRunnableForUi(Runnable runnable) | void |

<br> 

-----
<br>

<h3>AtlwThreadUtil---(安卓线程处理单例)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 提示自定义view消息 | toastView(View customView) | void |
| 提示自定义view消息 | toastView(View customView, Long showTime) | void |
| 显示提示信息 | toastMsg(String msg) | void |
| 显示吐司提示信息 | toastMsg(@StringRes int msgResId) | void |
| 显示提示信息 | toastMsg(String msg, Long showTime) | void |
| 显示吐司提示信息 | toastMsg(@StringRes int msgResId, Long showTime) | void |
| 设置弹窗参数，全局使用，以最后一次变更为准 | setParams(Long showTime, Integer gravity, Integer xOffset, Integer yOffset, Float horizontalMargin, Float verticalMargin) | void |

<br> 

-----
<br>

<h3>AtlwViewUtil---(控件相关工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取控件的LayoutParams | getViewLayoutParams(View view, Integer width, Integer height) | ViewGroup.LayoutParams |
| 获取控件的LayoutParams | getViewLayoutParams(Class<?> paramsClass, View view, Integer width, Integer height) | <T extends ViewGroup.LayoutParams> |
| 设置控件的宽高 | setViewWidthHeight(View view, Integer width, Integer height) | void |
| 设置控件宽高以及margin属性 | setViewWidthHeightMargin(View view, int width, int height, Integer left, Integer top, Integer right, Integer bottom) | void |
| 设置控件宽高以及margin属性 | setViewWidthHeightMargin(View view, Class<?> paramsClass, int width, int height, Integer left, Integer top, Integer right,Integer bottom) | void |
| 设置view的外边距params | setViewMarginParams(View view, ViewGroup.LayoutParams layoutParams, Integer left, Integer top, Integer right, Integer bottom) | void |
| 对Drawable着色 | tintDrawable(Drawable drawable, ColorStateList colors) | Drawable | 
| 设置背景图片着色 | setBackgroundTint(View view, ColorStateList colorStateList) | void |
| 设置图片控件的src资源的着色 | setImageSrcTint(ImageView imageView, ColorStateList colorStateList) | void |
| 设置文本控件的Drawable左上右下图片着色 | setTextViewDrawableLRTBTint(TextView textView, ColorStateList colorStateList) | void |
| 获取文本字符串宽度 | getStrTextWidth(Paint paint, String text, Integer start, Integer end) | float |
| 获取文本字符串宽度 | getStrTextWidth(Paint paint, String text) | float |
| 获取文本字符串宽度 | getStrTextWidth(int textSize, String text, Integer start, Integer end) | float |
| 获取文本字符串宽度 | getStrTextWidth(int textSize, String text) | float |
| 获取文本高度 | getStrTextHeight(Paint paint) | float |
| 获取文本高度 | getStrTextHeight(int textSize) | float |
| RecycleView 是否在顶部未向下滑动过 | recycleViewIsTheTop(RecyclerView recyclerView) | boolean |
| 获取控件位图 | getViewBitmap(View optionsView) | Bitmap|
| 获取控件位图 | getViewBitmap(View optionsView, Integer[] hideIds) | Bitmap|
| 获取控件位图 | getViewBitmap(View optionsView, Integer useWidth, Integer useHeight, Integer[] hideIds) | Bitmap|

<br> 

-----
<br>

<h3>AtlwAPKPackageNameList---(apk包名称列表)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 应用宝 | MARKET_APPLICATION_OF_TREASURE | 无（字段为常量） |
| 魅族应用商店 | MARKET_FLY_ME | 无（字段为常量） |
| 华为应用商店 | MARKET_HUA_WEI | 无（字段为常量） |
| 酷派应用商店 | MARKET_COOLPAD | 无（字段为常量） |
| oppo应用商店 | MARKET_OPPO | 无（字段为常量） |
| vivo应用商店 | MARKET_VIVO | 无（字段为常量） |
| 三星应用商店 | MARKET_SAMSUNG | 无（字段为常量） |
| 小米应用商店 | MARKET_XIAO_MI | 无（字段为常量） |
| 百度手机助手 | MARKET_BAIDU | 无（字段为常量） |
| 百度地图 | MAP_BAIDU | 无（字段为常量） |
| 腾讯地图 | MAP_TENCENT | 无（字段为常量） |
| 高德地图 | MAP_GAODE | 无（字段为常量） |

<br> 

-----
<br>

<h3>AtlwDesktopShortcutUtil---(桌面快捷方式)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 添加桌面快捷方式 | addDesktopShortcut(Context context, Class<T> openClass, String openUrlKey, String title, String url, Bitmap bitmap) | void |

<br> 

-----
<br>

<h3>AtlwFileOptionUtil---(文件操作工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 读取图片文件并获取字节 | readImageFileGetBytes(boolean isCheckPermission, boolean isCheckFile, String filePath) | byte[] |
| 从指定路径的文件中读取Bytes | readBytes(boolean isCheckPermission, String path) | byte[] |
| 从File中读取Bytes | readBytes(boolean isCheckPermission, File file) | byte[] |
| 从InputStream中读取Bytes | readBytes(boolean isCheckPermission, InputStream inputStream) | byte[] |
| 将InputStream写入File | writeToFile(boolean isCheckPermission, File file, InputStream inputStream, boolean append) | boolean |
| 将文本写入文件 | writeToFile(boolean isCheckPermission, File file, String text) | boolean |
| 将文本写入文件，同时决定是否为追加写入 | writeToFile(boolean isCheckPermission, File file, String text, String encoding, boolean append) | boolean |
| bitmap文件存储 | writeToFile(boolean isCheckPermission, File file, Bitmap bitmap, Bitmap.CompressFormat format) | boolean |
| 将字节写入文件 | writeToFile(boolean isCheckPermission, File file, byte[] buffer) | boolean |
| 将字节写入文件 | writeToFile(boolean isCheckPermission, File file, byte[] buffer, boolean append) | boolean |
| 通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件 | writeToFile(@NotNull Intent data, @NotNull String saveFile) | String |
| 复制单个文件 | copyFile(boolean isCheckPermission, String oldPath, String newPath) | boolean |
| 删除文件 | deleteFile(boolean isCheckPermission, String url) | boolean |
| 获取文件大小，单位B | getFileSize(boolean isCheckPermission, File file, String filtrationDir) | long |
| 删除文件夹以及目录下的文件 | deleteDirectory(boolean isCheckPermission, String filePath) | boolean |
| 创建文件夹 | createDirectory(boolean isCheckPermission, String path, boolean nowPathIsFile) | boolean |
| 根据正则获取指定目录下的所有文件列表(使用递归扫描方式) | getFileListForMatchRecursionScan(boolean isCheckPermission, String scanPath, String matchRegular) | List<File> |
| 根据正则获取指定目录下的所有文件列表(使用队列扫描方式) | getFileListForMatchLinkedQueueScan(boolean isCheckPermission, String scanPath, final String matchRegular) | List<File> |
| 获取根目录文件夹地址 | getBaseStorageDirPath() | String |
| 获取App系统文件夹地址 | getAppSystemStorageDirPath(String applicationId) | String |
| 根据uri获取图片文件地址 | getUriPath(Uri uri) | String |
| 获取app缓存文件大小 | getAppCacheFileSize(boolean isCheckPermission) | long |
| 清除app缓存 | clearAppCacheFile(boolean isCheckPermission) | boolean |

<br> 

-----
<br>

<h3>AtlwImageCommonUtil---(图片处理通用类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 将图片文件转换为base64字符串 | imageFileToBase64String(String filePath) | String |
| 将图片转换为base64字符串 | imageToBase64String(Bitmap bitmap) | String |
| 图片drawable转bitmap | drawableToBitmap(Drawable drawable) | Bitmap |
| 图片drawable转bitmap | drawableToBitmap(Drawable drawable,int width,int height) | Bitmap |
| bitmap转Drawable | bitmapToDrawable(Bitmap bitmap) | Drawable |
| 获取drawable的宽度 | getDrawableWidth(Drawable drawable) | int |
| 获取drawable的高度 | getDrawableHeight(Drawable drawable) | int |
| 获取圆角bitmap | getRoundedCornerBitmap(Bitmap bitmap, int radius) | Bitmap |
| 获取圆角bitmap | getRoundedCornerBitmap(Bitmap bitmap,int width,int height,int radius) | Bitmap |
| 获取圆角bitmap | getRoundedCornerBitmap(Bitmap bitmap, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) | Bitmap |
| 获取圆角bitmap | Bitmap getRoundedCornerBitmap(Drawable drawable, int width, int height, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius) | Bitmap |
| 获取圆形bitmap | getCircleBitmap(Drawable drawable, int width, int height, int radius) | Bitmap |
| 获取圆形bitmap | getCircleBitmap(Bitmap bitmap) | Bitmap |
| 获取圆形bitmap | getCircleBitmap(Bitmap bitmap, int radius) | Bitmap |
| 位图压缩，不压缩大小 | bitmapCompress(Bitmap bitmap, Bitmap.CompressFormat format, int size) | Bitmap |
| 位图压缩 | bitmapCompressToByte(Bitmap bitmap, Bitmap.CompressFormat format, int size) | byte[] |
| 获取位图字节 | getBitmapBytes(Bitmap bitmap) | byte[] |
| 十进制颜色值转16进制 | toHexEncoding(int color) | String |
| 图片的缩放方法 | zoomImage(Bitmap bgImage, double newWidth, double newHeight) | Bitmap |
| 读取照片exif信息中的旋转角度 | readPictureDegree(String path) | int |
| 旋转指定图片一定的角度 | toTurnPicture(Bitmap img, int degree) | Bitmap |
| 裁剪位图 | cropBitmap(Bitmap bitmap, int leftPercentForBitmapWidth, int topPercentForBitmapHeight, int rightPercentForBitmapWidth, int bottomPercentForBitmapHeight) | Bitmap |
| 从中心裁剪图片到指定的宽高 | cropBitmapForCenter(Bitmap bitmap, double cropPercentWidthHeight) | Bitmap |
| 使背景透明 | makeBgTransparent(Bitmap bitmap) | Bitmap |
| 合并位图 | mergeBitmap(Bitmap bitmapBg, @NonNull Bitmap bitmapTop, int topShowWidth, int topShowHeight, @FloatRange(from = 0, to = 1) float leftBgPercent, @FloatRange(from = 0, to = 1) Float topBgPercent, @FloatRange(from = 0, to = 1) Float bottomBgPercent) | Bitmap |
| 图片设置背景 | setBitmapBg(Bitmap bitmap, int bgColor, Integer bgContentPadding) | Bitmap |
| 获取两个位图重叠部分位图 | getOverlapBitmap(@NotNull Bitmap bottom, @NotNull Bitmap top) | Bitmap |
| 获取两个位图重叠部分位图 | getOverlapBitmap(@NotNull Bitmap bottom, @NotNull Bitmap top, Integer showWidth, Integer showHeight) | Bitmap |
| 释放bitmap | releaseBitmap(Bitmap bitmap) | void |
| 添加水印到位图中 | addWatermarkBitmap(@NotNull Bitmap bitmap, int textSize, int textColor, @NotNull String text, int width, int height, int rotationAngle) | Bitmap |
| 生成水印 | generateWatermarkBitmap(int textSize, int textColor, @NotNull String text, int width, int height, int rotationAngle) | Bitmap |
| 在画布上绘制水印 | addWatermarkBitmap(@NotNull Canvas canvas, int textSize, int textColor, @NotNull String text, int width, int height, int rotationAngle) | void |
| 在画布上绘制水印 | addWatermarkBitmap(@NotNull Canvas canvas, @NotNull Paint paint, @NotNull String text, int width, int height, int rotationAngle) | void |
| 图片格式转换 | coverImage(@NotNull String path, @NotNull String savePath, Bitmap.CompressFormat format) | String |
| 根据比例填充背景图片 | fillBgAspectRatio(@NotNull Bitmap bitmap, float ratio, @ColorInt int color) | Bitmap |

<br> 

-----
<br>

<h3>AtlwImageLoadingFactory---(图片加载工厂)

图片加载方式
- IMAGE_LOAD_LIBRARY_TYPE_FRESCO
- IMAGE_LOAD_LIBRARY_TYPE_GLIDE
- IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取图片加载实例 | getImageLoading（int imageLoadLibraryType）| AtlwBaseImageLoading | 
| 清除内存缓存,如果参数为空则清除所有相关实例 | clearMemoryCache(Integer imageLoadLibraryType) | void | 
| 清除本地缓存,如果参数为空则清除所有相关实例 | clearDiskCache(Integer imageLoadLibraryType) | void |
| 暂停加载图片 | pauseLoading() | void |
| 恢复加载图片 | resumeLoading() | void | 

<br> 

-----
<br>

<h3>AtlwBaseImageLoading---(基础图片加载接口)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用 | loadingImage(Object pathOrRes, ImageView imageView) | void |
| 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用 | loadingImage(Object pathOrRes, ImageView imageView, @NotNull AtlwImageLoadConfig config) | void |
| 加载网络图片 | loadingNetImage(String path, ImageView imageView) | void |
| 加载本地图片 | loadingLocalImage(String path, ImageView imageView) | void |
| 加载资源图片 | loadingResImage(@DrawableRes int resId, ImageView imageView) | void |
| 加载网络图片 | loadingNetImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config) | void |
| 加载本地图片 | loadingLocalImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config) | void |
| 加载资源图片 | loadingResImage(@DrawableRes int resId, ImageView imageView, @NotNull AtlwImageLoadConfig config) | void |
| 加载bitmap位图 | loadingBitmapImage(Bitmap bitmap, ImageView imageView, @NotNull AtlwImageLoadConfig config) | void |
| 获取网络图片位图信息 | getNetImageBitmap(String path, AtlwImageLoadConfig config) | void |
| 获取指定列表的图片位图 | getNetImageBitmap(ArrayList<String> list, final AtlwImageLoadConfig config) | void |
| 清除内存缓存 | clearMemoryCache() | void |
| 清除本地缓存 | clearDiskCache() | void |
| 暂停加载图片 | pauseLoading() | void |
| 恢复加载图片 | resumeLoading() | void | 

<br> 

-----
<br>

<h3>AtlwLocationUtil---(定位单例)

定位类型 AtlwLocationLibraryTypeEnum
- 默认使用系统定位 DEFAULT
- 高德地图定位 GAODE
- 百度地图定位 BAIDU
- 腾讯地图定位 TENCENT

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 检测权限相关 | checPermissions(@NotNull AtlwLocationConfig config) | void |
| 使用网络定位 | startNetworkPositioning(@NotNull AtlwLocationConfig config) | void |
| 使用设备定位 | startDevicesPositioning(@NotNull AtlwLocationConfig config) | void |
| 使用精度定位 | startAccuratePositioning(@NotNull AtlwLocationConfig config) | void |
| 停止循环定位 | stopLoopPositioning() | void |
| 请求权限 | requestPermissions(@NotNull Object context,@NotNull AtlwLocationConfig config) | void |
| 设置定位库类型 | locationLibraryType(@NotNull AtlwLocationConfig config) | void |

<br> 

-----
<br>

<h3>AtlwFlyMessageUtil---(消息传递工具类)

默认传递方式，可继承 AtlwFlyMessageMsgTypes
- 桌面快捷方式创建成功（DESKTOP_SHORTCUT_CREATE_SUCCESS） = -10001;
- Activity生命周期监听之onActivityCreated（ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE） = -10002;
- Activity生命周期监听之onActivityStart（ACTIVITY_LIFECYCLE_CALLBACKS_ON_START） = -10004;
- Activity生命周期监听之onActivityResumed （ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED） = -10005;
- Activity生命周期监听之onActivityPaused （ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED） = -10006;
- Activity生命周期监听之onActivityStopped （ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED） = -10007;
- Activity生命周期监听之onActivitySaveInstanceState （ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE） = -10008;
- Activity生命周期监听之onActivityDestroyed （ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED） = -10009;

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 注册消息回调记录 | registerMsgCallback(Object object, int msgType, FlyMessgeCallback flyMessgeCallback, boolean isOnlyMsgType,boolean isActivity) | void |
| 取消注册消息回调记录 | unRegisterMsgCallback(Object object) | void |
| 发送消息 | sendMsg(int msgType, boolean isFinishRemove, Object... msgs) | void |

<br> 

-----
<br>

<h3>AtlwMobileContentUtil---(获取手机各个内容)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取所有的联系人信息，包括手机存储以及sim卡中的 | getAllContacts() | List<AtlwMobileContactInfoBean> |
| 获取系统短消息 | getSystemSms() | List<AtlwMobileSmsInfoBean> |
| 获取所有的短信，包括手机存储以及sim卡中的 | getAllSms() | List<AtlwMobileSmsInfoBean> |

<br> 

-----
<br>

<h3>AtlwMobileOptionsUtil---(手机操作工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 安装应用 | installApp(Activity activity, String authority, String filePath) | void |
| 获取App安装的intent | getInstallAppIntent(String authority, String installAppFilePath) | Intent |
| 跳转到权限设置页面 | jumpToAppPermissionSettingPage(Activity activity, String packageName) | void |
| 启动其他应用 | launchApp(String packageName, Bundle bundle, Activity activity) | void |
| 复制内容到剪贴板 | copyContentToClipboard(String label, String content) | void |
| 使设备震动 | vibrate(long milliseconds) | void |
| 开启相机 | openCamera(Activity activity, String savePath, int requestCode) | void |
| 拨打电话 | makeCall(Activity activity, String phoneNo) | void |
| 开启图片相册选择 | openImagePhotoAlbum(Activity activity, int requestCode) | void |

<br> 

-----
<br>

<h3>AtlwMobilePhoneBrandUtil---(手机品牌判断工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 是否是小米手机 | isXiaoMiMobile() | boolean |
| 判断是否是魅族手机 | isMeiZuMobile() | boolean |
| 是否是华为手机 | isHuaWeiMobile() | boolean |
| 是否是vivo手机 | isVivoMobile() | boolean |
| 是否是oppo手机 | isOPPOMobile() | boolean |
| 是否是Coolpad手机 | isCoolpadMobile() | boolean |
| 是否是samsung手机 | isSamsungMobile() | boolean |
| 是否是Sony手机 | isSonyMobile() | boolean |
| 是否是LG手机 | isLGMobile() | boolean |

<br> 

-----
<br>

<h3>AtlwMobileSystemInfoUtil---(手机系统信息工具类)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 获取当前手机系统语言 | getSystemLanguage()（返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”） | String |
| 获取当前系统上的语言列表(Locale列表) | getSystemLanguageList() | Locale[] | 
| 获取当前手机系统版本号 | getSystemVersion() | String |
| 获取手机型号 | getSystemModel() | String |
| 获取手机厂商 | getDeviceBrand() | String |
| 获取手机系统sdk版本号 | getSystemSdkVersion() | int |
| 获取手机品牌信息 | getMobileBrand() | String |
| 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限) | getIMEIInfo() | String |
| 获取当前网络类型 | getNetworkType() | int |
| 获取wifi的mac地址，适配到android Q | getMac() | String |
| 获取手机IP地址 | getIpAddress() | String |
| 获取系统相机包名 | getSystemCameraPackageName() | String |

<br> 

-----
<br>

<h3>AtlwMediaPlayUtil---(音频播放工具)

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 设置音频播放回调 | setMediaPlayCallback(AtlwMediaPlayCallback atlwMediaPlayCallback) | void |
| 开始播放 | start(Activity activity, final String playPath, @AtlwMediaPlayOutputType int type, boolean isEndRecord, boolean isCancelLastPlay) | boolean |
| 停止播放，要取消监听器 | stop(Activity activity) | boolean |
| 是否在播放 | isPlaying() | boolean |
| 设置播放类型 | setPlayState(boolean isUsePowerWakeLock, boolean isAllowChangeOutputType) | void |

<br> 

-----
<br>

<h3>AtlwRecordUtil---(录音工具类) | void |

| 功能 | 函数/常量 | 返回值 |
| :----: | :----: | :----: |
| 设置录音回调 | setRecordCallback(AtlwRecordCallback atlwRecordCallback) | boolean |
| 开启录音 | start(Activity activity, String savePath, boolean isCancelLastRecord, boolean isEndPlaying) | boolean |
| 结束录音 | stop() | boolean |
| 取消录音 | cancel() | boolean |
| 获取音量值等级价，会根据传递进来的最高等级返回相应的指定等级 | getVoiceLevel(int level) | void |
| 是否正在录音 | isRecording() | boolean |
