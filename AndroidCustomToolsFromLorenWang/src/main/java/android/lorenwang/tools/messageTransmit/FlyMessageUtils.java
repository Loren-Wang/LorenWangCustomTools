package android.lorenwang.tools.messageTransmit;

import android.app.Activity;
import android.app.Application;
import android.lorenwang.tools.base.LogUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by LorenWang on 2018/7/26 0026.
 * 创建时间：2018/7/26 0026 下午 05:28
 * 创建人：王亮（Loren wang）
 * 功能作用：消息传递工具类
 * 思路：初步想法：
             (1)通过application记录所有activity信息；
            (2)创建工具类，工具类内新建一个内部类，包含消息类型以及回调；
            (3)在工具类当中有一个消息类型集合，以及activity同内部类集合对应的map集合；
            (4)在activity中需要接收消息的地方设置消息类型以及回调；
            (5)当执行到某一个activity的时候则从列表当中取出相应消息类型的回调，然后传回数据，当某一个操作完成后传递到工具类当中则开始回传数据，如果activity当中没有可以回传的activity那么则判断其他的类型的回传
            (6)对于每一个要发送的消息都要做标记，当相同的标记再次发送的时候需要告诉工具类是否要讲以前的消息队列当中的消息移除掉，同时在对一条消息回传结束后需要知道这一条消息是否要从队列中移除掉，也就是是否仅通知一次，同时如果在队列当中没有这条消息那么即使调用回调也不会进行回调
            消息队列内部使用一个实体类，实体类中记录着消息类型、消息内容、是否要发送完就移除队列中的元素的方法
 * 方法：
 *      1、取消注册消息回调记录
 *      2、发送消息
 *      3、返回生命周期监听
 *      4、获取记录存储集合的key
 *      5、注册消息回调记录
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：需要在application初始化的时候注册activity监听
 *     注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据
 */
public class FlyMessageUtils {
    private final String TAG = getClass().getName();
    private static FlyMessageUtils flyMessageUtils;
    public synchronized static FlyMessageUtils getInstance(){
        if(flyMessageUtils == null){
            flyMessageUtils = new FlyMessageUtils();
        }
        return flyMessageUtils;
    }

    //activity生命周期监听
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            nowShowActivity = activity;
            //activity获得到焦点，开始循环队列发送
            msgQueListOptions(false,false,true,null);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            unRegistMsgCallback(activity);
        }
    };
    //需要回调的集合记录,key:className+hashcode结合，value：回调记录集合
    private Map<String,List<CallbackRecodeDto>> recodeMap = new HashMap<>();
    //记录非activity的key列表
    private List<String> notActivityKeyList = new ArrayList<>();
    //当前正在显示的activity
    private Activity nowShowActivity;
    //消息队列实体诶
    private List<MessageQueueDto> messageQueueList = new ArrayList<>();


    /**
     * 注册消息回调记录
     * @param object 上下文
     * @param msgType 消息类型
     * @param flyMessgeCallback 回调
     * @param isOnlyMsgType 在这个key的下面是否只有这一个消息实例
     * @param isActivity 是否是activity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public synchronized void registMsgCallback(Object object, int msgType, FlyMessgeCallback flyMessgeCallback
            , boolean isOnlyMsgType, boolean isActivity){
        if(object == null || flyMessgeCallback == null){
            return;
        }
        String key = getKey(object);
        List<CallbackRecodeDto> list = recodeMap.get(key);
        CallbackRecodeDto callbackRecodeDto;
        //如果在这个key下面只有一个消息实例的话需要将其他的实例移除
        if(isOnlyMsgType && list != null && list.size() > 0){
            List<CallbackRecodeDto> removeList = new ArrayList<>();
            Iterator<CallbackRecodeDto> iterator = list.iterator();
            while (iterator.hasNext()){
                callbackRecodeDto = iterator.next();
                if(Integer.compare(callbackRecodeDto.msgType,msgType) == 0){
                    //这个是要被移除的实例
                    callbackRecodeDto.flyMessgeCallback = null;
                    removeList.add(callbackRecodeDto);
                }
            }
            //移除其他不要的
            list.removeAll(removeList);
            removeList.clear();
            removeList = null;
            iterator = null;
            callbackRecodeDto = null;
        }

        //生成实例添加到记录当中
        if(list == null){
            list = new ArrayList<>();
        }
        callbackRecodeDto = new CallbackRecodeDto();
        callbackRecodeDto.msgType = msgType;
        callbackRecodeDto.flyMessgeCallback = flyMessgeCallback;
        list.add(callbackRecodeDto);
        recodeMap.put(key,list);
        if(isActivity) {
            notActivityKeyList.remove(key);
        }else {
            notActivityKeyList.add(key);
        }
        key = null;

        //注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据
        msgQueListOptions(false,false,true,null);
    }
    /**
     * 取消注册消息回调记录
     * @param object
     */
    public void unRegistMsgCallback(Object object){
        if(object == null){
            return;
        }
        List<CallbackRecodeDto> list = recodeMap.get(getKey(object));
        if(list != null){
            CallbackRecodeDto callbackRecodeDto;
            Iterator<CallbackRecodeDto> iterator = list.iterator();
            while (iterator.hasNext()){
                callbackRecodeDto = iterator.next();
                callbackRecodeDto.flyMessgeCallback = null;
                callbackRecodeDto = null;
            }
        }
        notActivityKeyList.remove(getKey(object));
    }

    /**
     * 发送消息
     * @param msgType
     * @param isFinishRemove 是否回传结束就移除
     * @param msgs
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void sendMsg(int msgType, boolean isFinishRemove, Object... msgs){
        MessageQueueDto messageQueueDto = new MessageQueueDto();
        messageQueueDto.isFinishRemove = isFinishRemove;
        messageQueueDto.msgs = msgs;
        messageQueueDto.msgType = msgType;
        //添加到队列
        msgQueListOptions(true,false,false,messageQueueDto);
        //开始发送
        callbackMsg(messageQueueDto);
    }



    /**
     * 返回生命周期监听
     * @return
     */
    public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return activityLifecycleCallbacks;
    }
    /**
     * 获取记录存储集合的key
     * @param object
     * @return
     */
    private String getKey(Object object){
        return object.getClass().getName() + object.hashCode();
    }



    /**
     * 回传消息，先判断当前正在显示的activity当中是否有要回传这个消息的，有就回传，
     *         同时判断非activity当中是否也有要回传这个消息的，有就回传
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private synchronized void callbackMsg(MessageQueueDto messageQueueDto){
        if(messageQueueDto == null){
            return;
        }
        //先回传activity当中的
        if(nowShowActivity != null){
            callbackMsg(recodeMap.get(getKey(nowShowActivity)),messageQueueDto);
        }
        //回传非activity的
        Iterator<String> iterator = notActivityKeyList.iterator();
        while (iterator.hasNext()){
            callbackMsg(recodeMap.get(iterator.next()),messageQueueDto);
        }
        iterator = null;
    }
    /**
     * 回传列表消息
     * @param list
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private synchronized void callbackMsg(List<CallbackRecodeDto> list, MessageQueueDto messageQueueDto){
        if(list != null && list.size() > 0 && messageQueueDto != null){
            CallbackRecodeDto callbackRecodeDto;
            Iterator<CallbackRecodeDto> iterator = list.iterator();
            while (iterator.hasNext()){
                callbackRecodeDto = iterator.next();
                if(Integer.compare(callbackRecodeDto.msgType,messageQueueDto.msgType) == 0) {
                    callbackMsg(callbackRecodeDto.flyMessgeCallback, messageQueueDto);
                }
            }
        }
    }
    /**
     * 回传单条消息
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private synchronized void callbackMsg(FlyMessgeCallback callback, MessageQueueDto messageQueueDto){
        if(messageQueueDto == null){
            return;
        }
        try {
            callback.msg(messageQueueDto.msgType,messageQueueDto.msgs);
            //如果要移除的话则在队列当中移除
            if(messageQueueDto.isFinishRemove && messageQueueList.contains(messageQueueDto)){
                msgQueListOptions(false,true,false,messageQueueDto);
            }
        }catch (Exception e){
            LogUtils.logE(TAG,"callback msg fail");
        }
    }

    /**
     * 消息集合操作类
     * @param isAdd 是否是添加进入集合
     * @param isRemove 是否是从集合当中删除
     * @param isCallback 是否是要进行线程回调
     * @param optionMsgQueDto 添加或删除时要被操作的实体类
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private synchronized void msgQueListOptions(boolean isAdd, boolean isRemove
            , boolean isCallback, MessageQueueDto optionMsgQueDto){
        //新增数据
        if(isAdd){
            if(optionMsgQueDto != null){
                messageQueueList.add(optionMsgQueDto);
            }
            return;
        }
        //移除数据
        if(isRemove){
            if(optionMsgQueDto != null){
                messageQueueList.remove(optionMsgQueDto);
            }
            return;
        }
        //开始进行数据回调
        if(isCallback){
            try {
                MessageQueueDto messageQueueDto;
                Iterator<MessageQueueDto> iterator = messageQueueList.iterator();
                while (iterator.hasNext()){
                    messageQueueDto = iterator.next();
                    if(messageQueueDto != null) {
                        callbackMsg(messageQueueDto);
                    }
                }
            }catch (Exception e){
                LogUtils.logE(TAG,"data callback error");
            }
        }
    }




    /**
     * 消息回调
     */
    public interface FlyMessgeCallback{
        void msg(int msgType, Object... msgs);
    }
    /**
     * 回调实体类
     */
    private class CallbackRecodeDto{
        int msgType;//消息类型
        FlyMessgeCallback flyMessgeCallback;//消息回调
    }
    /**
     * 消息队列实体类
     */
    private class MessageQueueDto{
        int msgType;
        Object[] msgs;
        boolean isFinishRemove;//是否结束移除
    }
}
