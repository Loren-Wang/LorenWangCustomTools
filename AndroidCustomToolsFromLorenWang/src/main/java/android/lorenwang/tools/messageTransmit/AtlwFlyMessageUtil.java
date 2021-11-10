package android.lorenwang.tools.messageTransmit;

import android.app.Activity;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwLogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：消息传递工具类
 * 初始注释时间： 2018/7/26 16:31
 * 创建人：王亮（Loren）
 * 思路：
 * (1)通过application记录所有activity信息；
 * (2)创建工具类，工具类内新建一个内部类，包含消息类型以及回调；
 * (3)在工具类当中有一个消息类型集合，以及activity同内部类集合对应的map集合；
 * (4)在activity中需要接收消息的地方设置消息类型以及回调；
 * (5)当执行到某一个activity的时候则从列表当中取出相应消息类型的回调，然后传回数据，当某一个操作完成后传递到工具类当中则开始回传数据，如果activity当中没有可以回传的activity那么则判断其他的类型的回传
 * (6)对于每一个要发送的消息都要做标记，当相同的标记再次发送的时候需要告诉工具类是否要讲以前的消息队列当中的消息移除掉，同时在对一条消息回传结束后需要知道这一条消息是否要从队列中移除掉，也就是是否仅通知一次，同时如果在队列当中没有这条消息那么即使调用回调也不会进行回调
 * 消息队列内部使用一个实体类，实体类中记录着消息类型、消息内容、是否要发送完就移除队列中的元素的方法
 * 方法：
 * 注册消息回调记录--registerMsgCallback(Object object, int msgType, FlyMessgeCallback flyMessgeCallback, boolean isOnlyMsgType,boolean isActivity)
 * 取消注册消息回调记录--unRegisterMsgCallback(Object object)
 * 发送消息--sendMsg(int msgType, boolean isFinishRemove, Object... msgs)
 * 注意：
 * 需要在application初始化的时候注册activity监听
 * 注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwFlyMessageUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwFlyMessageUtil optionsInstance;

    public static AtlwFlyMessageUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwFlyMessageUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwFlyMessageUtil();
                }
            }
        }
        return optionsInstance;
    }

    private AtlwFlyMessageUtil() {
        //获取Activity生命周期监听的消息接收
        FlyMessgeCallback flyMessgeCallback = new FlyMessgeCallback() {
            @Override
            public void msg(int msgType, Object... msgs) {
                switch (msgType) {
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE:
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_START:
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED:
                        //                        nowShowActivity = (Activity) msgs[0];
                        //                        //activity获得到焦点，开始循环队列发送
                        //                        msgQueListOptions(false, false, true, null, null);
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED:
                        //                        unregisterBrightObserver(optionsActivity);
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED:
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE:
                        break;
                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED:
                        unRegisterMsgCallback((Activity) msgs[0]);
                        break;
                    default:
                        break;
                }
            }
        };
        //       registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE, flyMessgeCallback, false, false);
        //       registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_START, flyMessgeCallback, false, false);
        registerMsgCallback(this, AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED, flyMessgeCallback, false, false);
        //
        //                registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED, flyMessgeCallback, false, false);
        //       registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED, flyMessgeCallback, false, false);
        //       registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE, flyMessgeCallback, false, false);
        registerMsgCallback(this, AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED, flyMessgeCallback, false, false);


    }

    private final Map<Integer, List<CallbackRecodeDto>> recodeMsgCallbackMap = new ConcurrentHashMap<>();
    private final Map<String, List<Integer>> recodeMsgTypeMap = new ConcurrentHashMap<>();
    //
    //    //需要回调的集合记录,key:className+hashcode结合，value：回调记录集合
    //    private Map<String, List<CallbackRecodeDto>> recodeMap = new HashMap<>();
    //    //记录非activity的key列表
    //    private List<String> notActivityKeyList = new ArrayList<>();
    //    //记录activity的key列表
    //    private List<String> activityKeyList = new ArrayList<>();
    //    //当前正在显示的activity
    //    private Activity nowShowActivity;
    //    //消息队列实体诶
    //    private List<MessageQueueDto> messageQueueList = new ArrayList<>();

    /**
     * 注册消息回调记录
     *
     * @param object            上下文
     * @param msgType           消息类型
     * @param flyMessgeCallback 回调
     * @param isOnlyMsgType     在这个key的下面是否只有这一个消息实例
     * @param isActivity        是否是activity
     */
    public synchronized void registerMsgCallback(Object object, int msgType, FlyMessgeCallback flyMessgeCallback, boolean isOnlyMsgType,
            boolean isActivity) {
        if (object == null || flyMessgeCallback == null) {
            return;
        }
        //记录消息列表
        String key = getKey(object);
        List<Integer> msgList = recodeMsgTypeMap.get(key);
        if (msgList == null) {
            msgList = new ArrayList<>();
        }
        if (!msgList.contains(msgType)) {
            msgList.add(msgType);
        }
        recodeMsgTypeMap.put(key, msgList);

        //记录回调实体类
        List<CallbackRecodeDto> callbackRecodeDtoList = recodeMsgCallbackMap.get(msgType);
        if (callbackRecodeDtoList == null) {
            callbackRecodeDtoList = new ArrayList<>();
        }
        callbackRecodeDtoList.add(new CallbackRecodeDto(msgType, flyMessgeCallback));
        recodeMsgCallbackMap.put(msgType, callbackRecodeDtoList);

        //注册回调后，要直接回调，否则部分界面注册可能晚于回调，导致无法获得数据
        msgQueListOptions(false, false, true, null, null);
    }

    /**
     * 取消注册消息回调记录
     *
     * @param object 实例
     */
    public void unRegisterMsgCallback(Object object) {
        if (object == null) {
            return;
        }
        //移除消息
        List<Integer> msgTypeList = recodeMsgTypeMap.get(getKey(object));
        if (msgTypeList != null) {
            Iterator<Integer> iterator = msgTypeList.iterator();
            int msgType;
            List<CallbackRecodeDto> callbackRecodeDtos;
            while (iterator.hasNext()) {
                msgType = iterator.next();
                callbackRecodeDtos = recodeMsgCallbackMap.get(msgType);
                if (callbackRecodeDtos != null) {
                    callbackRecodeDtos.clear();
                }
                iterator.remove();
            }
            recodeMsgTypeMap.remove(getKey(object));
        }
    }

    /**
     * 发送消息
     *
     * @param msgType        消息类型
     * @param isFinishRemove 是否回传结束就移除
     * @param msgs           消息参数
     */
    public synchronized void sendMsg(int msgType, boolean isFinishRemove, Object... msgs) {
        MessageQueueDto messageQueueDto = new MessageQueueDto();
        messageQueueDto.isFinishRemove = isFinishRemove;
        messageQueueDto.msgs = msgs;
        messageQueueDto.msgType = msgType;
        //添加到队列
        msgQueListOptions(true, false, false, messageQueueDto, null);
        //开始发送
        callbackMsg(messageQueueDto);
    }

    /**
     * 获取记录存储集合的key
     *
     * @param object 实例
     * @return 存储使用的key
     */
    private String getKey(Object object) {
        return object.getClass().getName() + object.hashCode();
    }

    /**
     * 回传消息，先判断当前正在显示的activity当中是否有要回传这个消息的，有就回传，
     * 同时判断非activity当中是否也有要回传这个消息的，有就回传
     */
    private synchronized void callbackMsg(MessageQueueDto messageQueueDto) {
        if (messageQueueDto == null) {
            return;
        }
        callbackMsg(recodeMsgCallbackMap.get(messageQueueDto.msgType), messageQueueDto);
    }

    /**
     * 回传列表消息
     *
     * @param list 记录回调列表
     */
    private synchronized void callbackMsg(List<CallbackRecodeDto> list, MessageQueueDto messageQueueDto) {
        if (list != null && list.size() > 0 && messageQueueDto != null) {
            CallbackRecodeDto callbackRecodeDto;
            for (CallbackRecodeDto recodeDto : list) {
                callbackRecodeDto = recodeDto;
                if (callbackRecodeDto.msgType == messageQueueDto.msgType) {
                    callbackMsg(callbackRecodeDto.flyMessgeCallback, messageQueueDto);
                }
            }
        }
    }

    /**
     * 回传单条消息
     *
     * @param callback 回调
     */
    private synchronized void callbackMsg(final FlyMessgeCallback callback, final MessageQueueDto messageQueueDto) {
        if (messageQueueDto == null) {
            return;
        }
        try {
            callback.msg(messageQueueDto.msgType, messageQueueDto.msgs);
            //            //如果要移除的话则在队列当中移除
            //            if(messageQueueDto.isFinishRemove && messageQueueList.contains(messageQueueDto)){
            //                msgQueListOptions(false,true,false,messageQueueDto,messageQueueDto.msgType);
            //            }
        } catch (Exception e) {
            if (JtlwCheckVariateUtil.getInstance().isEmpty(e) && JtlwCheckVariateUtil.getInstance().isEmpty(e.getMessage()) &&
                    e.getMessage().contains("Only the original thread that created a view hierarchy can touch its views.")) {
                AtlwThreadUtil.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.msg(messageQueueDto.msgType, messageQueueDto.msgs);
                            //                          //如果要移除的话则在队列当中移除
                            //                         if(messageQueueDto.isFinishRemove && messageQueueList.contains(messageQueueDto)){
                            //                             msgQueListOptions(false,true,false,messageQueueDto,messageQueueDto.msgType);
                            //                         }
                        } catch (Exception e) {
                            AtlwLogUtil.logUtils.logE(TAG, "callback msg fail");
                        }
                    }
                });
            } else {
                AtlwLogUtil.logUtils.logE(TAG, "callback msg fail");
            }
        }
    }

    /**
     * 消息集合操作类
     *
     * @param isAdd           是否是添加进入集合
     * @param isRemove        是否是从集合当中删除
     * @param isCallback      是否是要进行线程回调
     * @param optionMsgQueDto 添加或删除时要被操作的实体类
     * @param msgType         要移除的消息类型
     */
    private synchronized void msgQueListOptions(boolean isAdd, boolean isRemove, boolean isCallback, MessageQueueDto optionMsgQueDto,
            Integer msgType) {
        //        //新增数据
        //        if(isAdd){
        //            if(optionMsgQueDto != null){
        //                messageQueueList.add(optionMsgQueDto);
        //            }
        //            return;
        //        }
        //        //移除数据
        //        if(isRemove){
        //            if(msgType != null){
        //                MessageQueueDto messageQueueDto;
        //                for(int i = 0 ; i < messageQueueList.size() ; i++){
        //                    messageQueueDto = messageQueueList.get(i);
        //                    if(messageQueueDto != null && msgType.compareTo(messageQueueDto.msgType) == 0){
        //                        messageQueueList.remove(messageQueueDto);
        //                    }
        //                }
        //            }
        //            return;
        //        }
        //        //开始进行数据回调
        //        if(isCallback){
        //            try {
        //                MessageQueueDto messageQueueDto;
        //                Iterator<MessageQueueDto> iterator = messageQueueList.iterator();
        //                while (iterator.hasNext()){
        //                    messageQueueDto = iterator.next();
        //                    if(messageQueueDto != null) {
        //                        callbackMsg(messageQueueDto);
        //                    }
        //                }
        //            }catch (Exception e){
        //                LogUtils.logE(TAG,"data callback error");
        //            }
        //        }
    }

    /**
     * 消息回调
     */
    public interface FlyMessgeCallback {
        void msg(int msgType, Object... msgs);
    }

    /**
     * 回调实体类
     */
    private static class CallbackRecodeDto {
        int msgType;//消息类型
        FlyMessgeCallback flyMessgeCallback;//消息回调

        public CallbackRecodeDto(int msgType, FlyMessgeCallback flyMessgeCallback) {
            this.msgType = msgType;
            this.flyMessgeCallback = flyMessgeCallback;
        }
    }

    /**
     * 消息队列实体类
     */
    private static class MessageQueueDto {
        int msgType;
        Object[] msgs;
        boolean isFinishRemove;//是否结束移除
    }
}
