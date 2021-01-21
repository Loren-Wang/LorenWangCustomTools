package android.lorenwang.tools.desktopShortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageUtil;
import android.os.Build;

import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.DESKTOP_SHORTCUT_CREATE_SUCCESS;


public class DesktopShortcutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AtlwFlyMessageUtil.getInstance().sendMsg(DESKTOP_SHORTCUT_CREATE_SUCCESS,true);
        }
    }
}
