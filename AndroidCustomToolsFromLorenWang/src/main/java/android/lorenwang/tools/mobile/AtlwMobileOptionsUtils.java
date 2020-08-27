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
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import static android.media.AudioManager.STREAM_VOICE_CALL;


/**
 * 创建时间： 2019/1/29 0029 下午 16:20:28
 * 创建人：LorenWang
 * 功能作用：手机操作工具类
 * 方法介绍：
 * 1.1、安装应用
 * 1.2、获取App安装的intent
 * 1.3、跳转到权限设置页面
 * 1.4、启动其他应用
 * 1.pri、获取小米手机的MIUI版本号
 * 1.pri、跳转到小米App权限设置
 * 1.pri、跳转到魅族App权限设置
 * 1.pri、跳转到默认App权限设置页面
 * 2.1、使设备震动
 * 2.2、开启相机
 * 2.3、拨打电话
 * 2.4、开启图片相册选择
 * 3.1、获取电源设备锁
 * 3.2、销毁电源设备锁
 * 3.3、申请电源设备锁，关闭屏幕
 * 3.4、释放电源设备锁，唤起屏幕
 * 4.1、获取传感器管理器实例
 * 4.2、注册距离传感器监听
 * 4.3、取消注册距离传感器监听
 * 5.1、获取系统级别音频管理器
 * 5.2、使用听筒播放正在播放的音频
 * 5.3、使用扬声器播放正在播放的音频
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public final class AtlwMobileOptionsUtils {
    private final String TAG = getClass().getName();
    private static volatile AtlwMobileOptionsUtils optionsInstance;

    private AtlwMobileOptionsUtils() {
    }

    public static AtlwMobileOptionsUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwMobileOptionsUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwMobileOptionsUtils();
                }
            }
        }
        return optionsInstance;
    }

    /*---------------------------------------软件部分---------------------------------------*/

    /**
     * 安装应用
     *
     * @param activity  上下文
     * @param authority fileprovider 认证字符串
     * @param filePath  安装包地址
     */
    public void installApp(Activity activity, String authority, String filePath) {
        Intent intent = getInstallAppIntent(authority, filePath);
        if (intent != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 获取App安装的intent
     *
     * @param installAppFilePath 安卓文件地址
     * @param authority          The authority of a defined in a
     *                           {@code <provider>} element in your app's manifest.
     * @return 安装程序的intent
     */
    public synchronized Intent getInstallAppIntent(String authority, String installAppFilePath) {
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(installAppFilePath)),
                        "application/vnd.android.package-archive");
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                File file = (new File(installAppFilePath));
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(AtlwConfig.nowApplication, authority, file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            AtlwLogUtils.logUtils.logD(TAG, "安装异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 跳转到权限设置页面
     *
     * @param packageName 包名
     */
    public void jumpToAppPermissionSettingPage(Activity activity, String packageName) {
        AtlwLogUtils.logUtils.logI(TAG, "跳转到APP权限设置页面：" + packageName);
        if (AtlwMobilePhoneBrandUtils.getInstance().isMeiZuMobile()) {
            jumpToMeizuAppPermissionSettingPage(activity, packageName);
        } else if (AtlwMobilePhoneBrandUtils.getInstance().isXiaoMiMobile()) {
            jumpToXiaoMiAppPermissionSettingPage(activity, packageName);
        } else {
            jumpToDefaultAppPermissionSettingPage(activity, packageName);
        }
    }

    /**
     * 启动其他应用
     *
     * @param packageName 其他应用的包名，不能为空
     * @param bundle      所需要传递的参数，可以为空
     * @param activity    当期的Activity
     * @author yynie
     */
    public void launchApp(String packageName, Bundle bundle, Activity activity) throws NullPointerException {
        if (activity == null) {
            throw new NullPointerException("activity can't be null!");
        }
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("packageName can't be empty!");
        }

        try {
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null && bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    /**
     * 获取小米手机的MIUI版本号
     *
     * @return 小米miui版本号
     */
    private String getMiuiVersion() {
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
                assert input != null;
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
     * @param packageName 应用包名
     */
    private void jumpToXiaoMiAppPermissionSettingPage(Activity activity, String packageName) {
        String rom = getMiuiVersion();
        AtlwLogUtils.logUtils.logI(TAG, "jumpToMiaoMiAppPermissionSettingPage --- rom : " + rom);
        Intent intent = new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions" +
                    ".AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions" +
                    ".PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else {
            jumpToDefaultAppPermissionSettingPage(activity, packageName);
        }
        activity.startActivity(intent);
    }

    /**
     * 跳转到魅族App权限设置
     *
     * @param packageName 应用包名
     */
    private void jumpToMeizuAppPermissionSettingPage(Activity activity, String packageName) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", packageName);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            jumpToDefaultAppPermissionSettingPage(activity, packageName);
        }
    }

    /**
     * 跳转到默认App权限设置页面
     *
     * @param packageName 应用包名
     */
    private void jumpToDefaultAppPermissionSettingPage(Activity activity, String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*---------------------------------------硬件部分---------------------------------------*/

    /**
     * 使设备震动
     *
     * @param milliseconds 振动时间
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public void vibrate(long milliseconds) {
        try {
            Vibrator vibrator = (Vibrator) AtlwConfig.nowApplication
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(milliseconds);
        } catch (Exception e) {
            AtlwLogUtils.logUtils.logE(e);
        }
    }

    /**
     * 开启相机
     *
     * @param activity    上下文
     * @param savePath    照片保存地址
     * @param requestCode 请求码
     */
    @RequiresPermission(allOf = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openCamera(Activity activity, String savePath, int requestCode) {
        //检查相机权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                Uri uri =
                        activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                Uri imageUri = Uri.fromFile(imagePathFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            }
            activity.startActivityForResult(intent, requestCode);
        } else {
            AtlwLogUtils.logUtils.logD(TAG, "don't get camera permisstion");
        }
    }

    /**
     * 拨打电话
     *
     * @param phoneNo 要拨打的手机号
     */
    public void makeCall(Activity activity, String phoneNo) {
        if (phoneNo != null && !"".equals(phoneNo)) {
            String number = "tel:" + phoneNo;
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse(number));
                activity.startActivity(callIntent);
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
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openImagePhotoAlbum(Activity activity, int requestCode) {
        //检查存储卡权限
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(intent, requestCode);
        } else {
            AtlwLogUtils.logUtils.logD(TAG, "don't get camera permisstion");
        }
    }


    /*---------------------------------------电源部分---------------------------------------*/

    /**
     * 电源设备锁
     */
    private PowerManager.WakeLock powerLocalWakeLock;

    /**
     * 获取电源设备锁
     *
     * @return 返回电源设备所
     */
    @SuppressLint("InvalidWakeLockTag")
    public PowerManager.WakeLock getPowerLocalWakeLock() {
        if (powerLocalWakeLock == null) {
            try {
                //获取系统服务POWER_SERVICE，返回一个PowerManager对象
                powerLocalWakeLock =
                        ((PowerManager) AtlwConfig.nowApplication.getSystemService(Context.POWER_SERVICE)).newWakeLock(32, "MyPower");
            } catch (Exception ignored) {
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
     */
    public void applyForPowerLocalWakeLock() {
        AtlwLogUtils.logUtils.logI(TAG, "申请电源设备锁");
        if (getPowerLocalWakeLock() != null) {
            AtlwLogUtils.logUtils.logI(TAG, "电源设备锁获取成功，准备申请锁住屏幕。");
            //申请电源设备锁锁住并关闭屏幕，在100ms后释放唤醒锁使其可以运行被唤醒
            getPowerLocalWakeLock().acquire(100);// 申请设备电源锁
        }
    }

    /**
     * 释放电源设备锁，唤起屏幕
     */
    public void releasePowerLocalWakeLock() {
        AtlwLogUtils.logUtils.logD(TAG, "释放设备电源锁");
        if (getPowerLocalWakeLock() != null) {
            AtlwLogUtils.logUtils.logI(TAG, "电源设备锁获取成功，准备申请释放屏幕并唤醒。");
            //申请电源设备锁锁住并关闭屏幕，在100ms后释放唤醒锁使其可以运行被唤醒
            getPowerLocalWakeLock().setReferenceCounted(false);
            getPowerLocalWakeLock().release(); // 释放设备电源锁

        }
    }


    /*---------------------------------------传感器部分---------------------------------------*/

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
     * @return 传感器实例
     */
    public SensorManager getSensorManager() {
        if (sensorManager == null) {
            sensorManager =
                    (SensorManager) AtlwConfig.nowApplication.getSystemService(Context.SENSOR_SERVICE);
        }
        return sensorManager;
    }

    /**
     * 注册距离传感器监听
     *
     * @param listener 监听回调
     */
    public void registProximitySensorListener(SensorEventListener listener) {
        synchronized (proximityListenerList) {
            if (listener != null && !proximityListenerList.contains(listener)) {
                getSensorManager().registerListener(listener, getSensorManager()
                        .getDefaultSensor(Sensor.TYPE_PROXIMITY),
                        SensorManager.SENSOR_DELAY_NORMAL);
                proximityListenerList.add(listener);
            }
        }
    }

    /**
     * 取消注册距离传感器监听
     *
     * @param listener 监听
     */
    public void unRegistProximitySensorListener(SensorEventListener listener) {
        synchronized (proximityListenerList) {
            if (listener != null && proximityListenerList.contains(listener)) {
                getSensorManager().unregisterListener(listener);
                proximityListenerList.remove(listener);
            }
        }
    }


    /*---------------------------------------音频管理器---------------------------------------*/

    /**
     * 音频管理器
     */
    private AudioManager audioManager;

    /**
     * 获取系统级别音频管理器
     *
     * @return 音频管理器
     */
    public AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager =
                    (AudioManager) AtlwConfig.nowApplication.getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    /**
     * 使用听筒播放正在播放的音频
     *
     * @param activity activity实例
     */
    public void useHandsetToPlay(Activity activity) {
        if (getAudioManager() != null) {
            AtlwLogUtils.logUtils.logD(TAG, "切换到手机听筒播放");
            activity.setVolumeControlStream(STREAM_VOICE_CALL);
            getAudioManager().setSpeakerphoneOn(false);//关闭扬声器
            getAudioManager().setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE,
                    AudioManager.ROUTE_ALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            getAudioManager().setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 使用扬声器播放正在播放的音频
     */
    public void useSpeakersToPlay() {
        if (getAudioManager() != null) {
            AtlwLogUtils.logUtils.logD(TAG, "切换到扬声器播放");
            getAudioManager().setSpeakerphoneOn(true);
            getAudioManager().setMode(AudioManager.MODE_NORMAL);
        }
    }

}
