package com.wake.fs.example1;

/**
 *
 * fs通道事件类型
 */
public enum FSEventType {

    //跟呼叫相关的通道事件
    CHANNEL_CREATE,
    //通道创建事件
    CHANNEL_ANSWER,
    //通道应答事件
    CHANNEL_BRIDGE,
    //通道桥接事件
    CHANNEL_HANGUP,
    //通道挂断事件
    CHANNEL_HANGUP_COMPLETE,
    CUSTOM,
    //CHANNEL_DESTROY,

    //CHANNEL_ORIGINATE,
    //CHANNEL_OUTGOING,

    //心跳包
    HEARTBEAT,
    //计费心跳包心跳包
    SESSION_HEARTBEAT;

}