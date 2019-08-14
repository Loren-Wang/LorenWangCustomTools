package android.lorenwang.tools.mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.lorenwang.tools.app.AtlwActivityUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.media.AudioManager.STREAM_VOICE_CALL;


/**
 * 创建时间： 2019/1/29 0029 下午 16:20:28
 * 创建人：LorenWang
 * 功能作用：手机操作工具类
 * 方法介绍：
 * 1、安装应用
 * 2、获取App安装的intent
 * 3、使设备震动
 * 4、拨打电话
 * 5、开启相机
 * 6、开启图片相册选择
 * 7、跳转到权限设置页面
 * 8、获取传感器管理器实例
 * 9、注册距离传感器监听
 * 10、取消注册距离传感器监听
 * 11、获取电源设备锁
 * 12、销毁电源设备锁
 * 13、申请电源设备锁，关闭屏幕
 * 14、释放电源设备锁，唤起屏幕
 * 15、获取系统级别音频管理器
 * 16、使用听筒播放正在播放的音频
 * 17、使用扬声器播放正在播放的音频
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public final class AtlwMobileOptionsUtils {
    private static final String TAG = "AppUtils";
    private static AtlwMobileOptionsUtils atlwMobileOptionsUtils;

    /**
     * 私有构造方法
     */
    private AtlwMobileOptionsUtils() {
    }

    public static AtlwMobileOptionsUtils getInstance() {
        synchronized (AtlwMobileOptionsUtils.class) {
            if (atlwMobileOptionsUtils == null) {
                atlwMobileOptionsUtils = new AtlwMobileOptionsUtils();
            }
        }
        return (AtlwMobileOptionsUtils) atlwMobileOptionsUtils;
    }


    /*******************************************硬件部分********************************************/

    /**
     * 使设备震动
     *
     * @param context      上下文
     * @param milliseconds 振动时间
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public void vibrate(Context context, long milliseconds) {
        try {
            Vibrator vibrator = (Vibrator) context
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(milliseconds);
        } catch (Exception e) {
            AtlwLogUtils.logE(e);
        }
    }

    /**
     * 开启相机
     *
     * @param activity    上下文
     * @param savePath    照片保存地址
     * @param requestCode 请求码
     */
    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera(Activity activity, String savePath, int requestCode) {
        //检查相机权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //检测保存路径
            File imagePathFile = new File(savePath);
            if (imagePathFile.isDirectory()) {
                return;
            }


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //7.0及以上
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, savePath);
                Uri uri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                Uri imageUri = Uri.fromFile(imagePathFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            }
            activity.startActivityForResult(intent, requestCode);
        } else {
            AtlwLogUtils.logD(TAG, "don't get camera permisstion");
        }
    }

    /*******************************************软件部分********************************************/

    /**
     * 安装应用
     *
     * @param activity  上下文
     * @param authority fileprovider 认证字符串
     * @param filePath  安装包地址
     */
    public void installApp(Activity activity, String authority, String filePath) {
        Intent intent = getInstallAppIntent(activity, authority, filePath);
        if (intent != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 获取App安装的intent
     *
     * @param context            s上下文
     * @param installAppFilePath 安卓文件地址
     * @param authority          The authority of a {@link android.support.v4.content.FileProvider} defined in a
     *                           {@code <provider>} element in your app's manifest.
     * @return 安装程序的intent
     */
    public synchronized Intent getInstallAppIntent(Context context, String authority, String installAppFilePath) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(installAppFilePath)), "application/vnd.android.package-archive");
                return intent;
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = (new File(installAppFilePath));
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                return intent;
            }
        } catch (Exception e) {
            AtlwLogUtils.logD(TAG, "安装异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 拨打电话
     *
     * @param context 上下文
     * @param phoneNo 要拨打的手机号
     */
    public static void makeCall(Context context, String phoneNo) {
        if (phoneNo != null && !"".equals(phoneNo)) {
            String number = "tel:" + phoneNo;
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse(number));
                context.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开启图片相册选择
     *
     * @param activity    上下文
     * @param requestCode 请求码
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openImagePhotoAlbum(Activity activity, int requestCode) {
        //检查存储卡权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(intent, requestCode);
        } else {
            AtlwLogUtils.logD(TAG, "don't get camera permisstion");
        }
    }

    /**
     * 跳转到权限设置页面
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public void jumpToAppPermisstionSettingPage(Context context, String packageName) {
        AtlwLogUtils.logI(TAG, "跳转到APP权限设置页面：" + packageName);
        if (AtlwMobilePhoneBrandUtils.getInstance().isMeiZuMobile()) {
            jumpToMeizuAppPermisstionSettingPage(context, packageName);
        } else if (AtlwMobilePhoneBrandUtils.getInstance().isXiaoMiMobile()) {
            jumpToXiaoMiAppPermisstionSettingPage(context, packageName);
        } else {
            jumpToDefaultAppPermisstionSettingPage(context, packageName);
        }
    }

    /**
     * 获取小米手机的MIUI版本号
     *
     * @return
     */
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    /**
     * 跳转到小米App权限设置
     *
     * @param context
     * @param packageName
     */
    private void jumpToXiaoMiAppPermisstionSettingPage(Context context, String packageName) {
        String rom = getMiuiVersion();
        AtlwLogUtils.logI(TAG, "jumpToMiaoMiAppPermisstionSettingPage --- rom : " + rom);
        Intent intent = new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else {
            jumpToDefaultAppPermisstionSettingPage(context, packageName);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到魅族App权限设置
     *
     * @param context
     * @param packageName
     */
    private void jumpToMeizuAppPermisstionSettingPage(Context context, String packageName) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", packageName);
            context.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            jumpToDefaultAppPermisstionSettingPage(context, packageName);
        }
    }

    /**
     * 跳转到默认App权限设置页面
     *
     * @param context
     * @param packageName
     */
    private void jumpToDefaultAppPermisstionSettingPage(Context context, String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /********************************************电源部分*******************************************/

    /**
     * 电源设备锁
     */
    private PowerManager.WakeLock powerLocalWakeLock;

    /**
     * 获取电源设备锁
     *
     * @param context 上下文
     * @return 返回电源设备所
     */
    @SuppressLint("InvalidWakeLockTag")
    public PowerManager.WakeLock getPowerLocalWakeLock(Context context) {
        if (powerLocalWakeLock == null) {
            try {
                //获取系统服务POWER_SERVICE，返回一个PowerManager对象
                powerLocalWakeLock = ((PowerManager) AtlwActivityUtils.getInstance().getApplicationContext(context)
                        .getSystemService(Context.POWER_SERVICE)).newWakeLock(32, "MyPower");
            } catch (Exception e) {
            }

        }
        return powerLocalWakeLock;
    }

    /**
     * 销毁电源设备锁
     */
    public void destoryPowerLocalWakeLock() {
        if (powerLocalWakeLock != null) {
            powerLocalWakeLock.setReferenceCounted(false);
            powerLocalWakeLock.release();//释放电源锁，如果不释放finish这个acitivity后仍然会有自动锁屏的效果，不信可以试一试
            powerLocalWakeLock = null;
        }
    }

    /**
     * 申请电源设备锁，关闭屏幕
     *
     * @param context 上下文
     */
    public void applyForPowerLocalWakeLock(Context context) {
        AtlwLogUtils.logI(TAG, "申请电源设备锁");
        if (getPowerLocalWakeLock(context) != null) {
            AtlwLogUtils.logI(TAG, "电源设备锁获取成功，准备申请锁住屏幕。");
            //申请电源设备锁锁住并关闭屏幕，在100ms后释放唤醒锁使其可以运行被唤醒
            getPowerLocalWakeLock(context).acquire(100);// 申请设备电源锁
        }
    }

    /**
     * 释放电源设备锁，唤起屏幕
     *
     * @param context 上下文
     */
    public void releasePowerLocalWakeLock(Context context) {
        AtlwLogUtils.logD(TAG, "释放设备电源锁");
        if (getPowerLocalWakeLock(context) != null) {
            AtlwLogUtils.logI(TAG, "电源设备锁获取成功，准备申请释放屏幕并唤醒。");
            //申请电源设备锁锁住并关闭屏幕，在100ms后释放唤醒锁使其可以运行被唤醒
            getPowerLocalWakeLock(context).setReferenceCounted(false);
            getPowerLocalWakeLock(context).release(); // 释放设备电源锁

        }
    }


    /****************************************传感器部分*********************************************/

    /**
     * 系统级别的传感器管理器
     */
    private SensorManager sensorManager;
    /**
     * 距离传感器所有监听
     */
    private final List<SensorEventListener> proximityListenerList = new ArrayList<>();

    /**
     * 获取传感器管理器实例
     *
     * @param context 上下文
     * @return 传感器实例
     */
    public SensorManager getSensorManager(Context context) {
        if (sensorManager == null) {
            sensorManager = (SensorManager) AtlwActivityUtils.getInstance().getApplicationContext(context)
                    .getSystemService(Context.SENSOR_SERVICE);
        }
        return sensorManager;
    }

    /**
     * 注册距离传感器监听
     *
     * @param context  上下文
     * @param listener 监听回调
     */
    public void registProximitySensorListener(Context context, SensorEventListener listener) {
        synchronized (proximityListenerList) {
            if (listener != null && !proximityListenerList.contains(listener)) {
                getSensorManager(context).registerListener(listener, getSensorManager(context)
                        .getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
                proximityListenerList.add(listener);
            }
        }
    }

    /**
     * 取消注册距离传感器监听
     *
     * @param context  上下文
     * @param listener 监听
     */
    public void unRegistProximitySensorListener(Context context, SensorEventListener listener) {
        synchronized (proximityListenerList) {
            if (listener != null && proximityListenerList.contains(listener)) {
                getSensorManager(context).unregisterListener(listener);
                proximityListenerList.remove(listener);
            }
        }
    }


    /*****************************************音频管理器********************************************/

    /**
     * 音频管理器
     */
    private AudioManager audioManager;

    /**
     * 获取系统级别音频管理器
     *
     * @param context 上下文
     * @return 音频管理器
     */
    public AudioManager getAudioManager(Context context) {
        if (audioManager == null) {
            audioManager = (AudioManager) AtlwActivityUtils.getInstance().getApplicationContext(context)
                    .getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    /**
     * 使用听筒播放正在播放的音频
     *
     * @param activity activity实例
     */
    public void useHandsetToPlay(Activity activity) {
        if (getAudioManager(activity) != null) {
            AtlwLogUtils.logD(TAG, "切换到手机听筒播放");
            activity.setVolumeControlStream(STREAM_VOICE_CALL);
            getAudioManager(activity).setSpeakerphoneOn(false);//关闭扬声器
            getAudioManager(activity).setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            getAudioManager(activity).setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 使用扬声器播放正在播放的音频
     *
     * @param activity activity实例
     */
    public void useSpeakersToPlay(Activity activity) {
        if (getAudioManager(activity) != null) {
            AtlwLogUtils.logD(TAG, "切换到扬声器播放");
            getAudioManager(activity).setSpeakerphoneOn(true);
            getAudioManager(activity).setMode(AudioManager.MODE_NORMAL);
        }
    }

}
