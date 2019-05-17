package com.wake.fs.example1;

import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author hui.zhou 2019/5/17
 */
public class EslCoreEventListener implements IEslEventListener {
    private static final Logger logger = LoggerFactory.getLogger(EslCoreEventListener.class);

    private TopicSender topicSender;

    public EslCoreEventListener(TopicSender topicSender) {
        this.topicSender = topicSender;
    }

    @Override
    public void backgroundJobResultReceived(EslEvent event) {
        System.out.println("Background job result received [{}]"
                + event);
    }

    @Override
    public void eventReceived(EslEvent event) {
        //fs事件接收
        String eventName = event.getEventName();
        //System.out.println("=======eventReceived======"+event.getEventHeaders());
        FSEventType fSEventType = FSEventType.valueOf(eventName);
        switch (fSEventType) {
            case CUSTOM://订阅事件
                dealCustomEvent(event);
                break;
            case CHANNEL_CREATE://通道创建事件
                dealChannelCreateEvent(event);
                break;
            case CHANNEL_HANGUP_COMPLETE://通道挂断事件
                dealChannelHangupEvent(event);
                break;
            case HEARTBEAT://心跳包
                dealBeatEvent(event);
                break;
            case SESSION_HEARTBEAT://计费心跳包心跳包
                dealSessionHertBeatEvent(event);
                break;
            default:
                break;
        }
    }

    private void dealSessionHertBeatEvent(EslEvent event) {
        logger.info(event.toString());
    }

    private void dealBeatEvent(EslEvent event) {
        logger.info(event.toString());
    }

    private void dealChannelHangupEvent(EslEvent event) {
        logger.info(event.toString());
    }

    private void dealChannelCreateEvent(EslEvent event) {
        logger.info(event.toString());
    }

    private void dealCustomEvent(EslEvent event) {
        logger.info(event.toString());
    }
}