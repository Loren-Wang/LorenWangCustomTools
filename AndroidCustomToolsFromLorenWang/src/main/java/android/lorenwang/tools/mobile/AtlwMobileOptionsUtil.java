package android.lorenwang.tools.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;

import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

/**
 * 功能作用：手机操作工具类
 * 初始注释时间： 2019/1/29 16:46
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 安装应用--installApp(activity,authority,filePath)
 * 获取App安装的intent--getInstallAppIntent(authority,installAppFilePath)
 * 跳转到权限设置页面--jumpToAppPermissionSettingPage(activity,packageName)
 * 启动其他应用--launchApp(packageName,bundle,activity)
 * 复制内容到剪贴板--copyContentToClipboard(label,content)
 * 使设备震动--vibrate(long milliseconds)
 * 开启相机--openCamera(activity,savePath,requestCode)
 * 拨打电话--makeCall(activity,phoneNo)
 * 开启图片相册选择--openImagePhotoAlbum(activity,requestCode)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public final class AtlwMobileOptionsUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwMobileOptionsUtil optionsInstance;

    private AtlwMobileOptionsUtil() {
    }

    public static AtlwMobileOptionsUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwMobileOptionsUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwMobileOptionsUtil();
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
     * @param authority fileProvider 认证字符串
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
        Intent intent = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(installAppFilePath)), "application/vnd.android.package-archive");
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                if (AtlwConfig.nowApplication.getPackageManager().canRequestPackageInstalls()) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    File file = (new File(installAppFilePath));
                    // 由于没有在Activity环境下启动Activity,设置下面的标签
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                    Uri apkUri = FileProvider.getUriForFile(AtlwConfig.nowApplication, authority, file);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    AtlwLogUtil.logUtils.logD(TAG, "安装异常：没有安装权限");
                }
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
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logD(TAG, "安装异常：" + e.getMessage());
        }
        return intent;
    }

    /**
     * 跳转到权限设置页面
     *
     * @param packageName 包名
     */
    public void jumpToAppPermissionSettingPage(Activity activity, String packageName) {
        AtlwLogUtil.logUtils.logI(TAG, "跳转到APP权限设置页面：" + packageName);
        if (AtlwMobilePhoneBrandUtil.getInstance().isMeiZuMobile()) {
            jumpToMeizuAppPermissionSettingPage(activity, packageName);
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
     * 复制内容到剪贴板
     *
     * @param content 内容
     */
    public void copyContentToClipboard(String label, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) AtlwConfig.nowApplication.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText(label, content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
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
            Vibrator vibrator = (Vibrator) AtlwConfig.nowApplication.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(milliseconds);
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logE(e);
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
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
            AtlwLogUtil.logUtils.logD(TAG, "don't get camera permisstion");
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
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openImagePhotoAlbum(Activity activity, int requestCode) {
        //检查存储卡权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(intent, requestCode);
        } else {
            AtlwLogUtil.logUtils.logD(TAG, "don't get camera permisstion");
        }
    }

}
