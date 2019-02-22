package android.lorenwang.tools.mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.lorenwang.tools.base.LogUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * 创建时间： 2019/1/29 0029 下午 16:20:28
 * 创建人：LorenWang
 * 功能作用：手机操作工具类
 * 方法介绍：1、安装应用
 *          2、获取App安装的intent
 *          3、使设备震动
 *          4、拨打电话
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public final class MobileOptionsUtils {
	private static final String TAG = "AppUtils";
	private static MobileOptionsUtils baseUtils;
	public static MobileOptionsUtils getInstance(){
		if(baseUtils == null){
			baseUtils = new MobileOptionsUtils();
		}
		return (MobileOptionsUtils) baseUtils;
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
			LogUtils.logE(e);
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
			LogUtils.logD(TAG,"安装异常：" + e.getMessage());
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

}
