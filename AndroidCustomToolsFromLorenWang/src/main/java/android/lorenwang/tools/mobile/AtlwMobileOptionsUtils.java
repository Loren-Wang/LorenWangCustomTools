package android.lorenwang.tools.mobile;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.net.Uri;
import android.os.Build;
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


/**
 * 创建时间： 2019/1/29 0029 下午 16:20:28
 * 创建人：LorenWang
 * 功能作用：手机操作工具类
 * 方法介绍：
 *          1、安装应用
 *          2、获取App安装的intent
 *          3、使设备震动
 *          4、拨打电话
 *          5、开启相机
 *          6、开启图片相册选择
 *          7、跳转到权限设置页面
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public final class AtlwMobileOptionsUtils {
	private static final String TAG = "AppUtils";
	private static AtlwMobileOptionsUtils atlwMobileOptionsUtils;
	public static AtlwMobileOptionsUtils getInstance(){
		synchronized (atlwMobileOptionsUtils) {
			if (atlwMobileOptionsUtils == null) {
				atlwMobileOptionsUtils = new AtlwMobileOptionsUtils();
			}
		}
		return (AtlwMobileOptionsUtils) atlwMobileOptionsUtils;
	}



	/*******************************************硬件部分********************************************/

	/**
	 * 使设备震动
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
	 * @param activity
	 * @param savePath
	 * @param requestCode
	 */
	@RequiresPermission(allOf = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
	 * @param context 上下文
	 * @param authority fileprovider 认证字符串
	 * @param filePath 安装包地址
	 */
	public void installApp(Context context,String authority,String filePath){
		Intent intent = getInstallAppIntent(context, authority, filePath);
		if(intent != null){
			context.getApplicationContext().startActivity(intent);
		}
	}

	/**
	 * 获取App安装的intent
	 * @param context s上下文
	 * @param installAppFilePath 安卓文件地址
	 * @param authority The authority of a {@link FileProvider} defined in a
	 *            {@code <provider>} element in your app's manifest.
	 * @return
	 */
	public synchronized Intent getInstallAppIntent(Context context,String authority,String installAppFilePath){
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(installAppFilePath)), "application/vnd.android.package-archive");
				return intent;
			}else {
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
		}catch (Exception e){
			AtlwLogUtils.logD(TAG,"安装异常：" + e.getMessage());
			return null;
		}
	}

	/**
	 * 拨打电话
	 * @param context
	 * @param phoneNo
	 */
	public static void makeCall(Context context, String phoneNo) {
		if(phoneNo != null && !"".equals(phoneNo)) {
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
	 * @param activity
	 * @param requestCode
	 */
	@RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
	 * @param context
	 * @param packageName
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

}
