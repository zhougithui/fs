package com.wake.fs;

import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FsClient {
    private static final Logger log = LoggerFactory.getLogger(FsClient.class);

    /**
     * host/port/password配置在D:\VS\freeswitch-1.6.19\Win32\Debug\conf\
     * autoload_configs目录event_socket.conf.xml文件中
     */
    private static String host = "172.19.60.174";
    private static int port = 8021;
    private static String password = "ClueCon";

    public static Client inBand() {

        final Client client = new Client();
        try {
            client.connect(host, port, password, 20);
        } catch (InboundConnectionFailure e) {
            log.error("Connect failed", e);
            return null;
        }

        // 注册事件处理程序
        client.addEventListener(new IEslEventListener() {

            @Override
            public void eventReceived(EslEvent event) {
                /// System.out.println("Event received [{}]" + event.getEventHeaders());
                if (event.getEventName().equals("CHANNEL_ANSWER")) {
                    // 呼叫应答事件
                    System.err.println("CHANNEL_ANSWER");
                }
                // 一个呼叫两个端点之间的桥接事件
                if (event.getEventName().equals("CHANNEL_BRIDGE")) {
                    System.err.println("CHANNEL_BRIDGE");
                }

                // 销毁事件
                if (event.getEventName().equals("CHANNEL_DESTROY")) {
                    System.err.println("CHANNEL_DESTROY");
                }

                // 挂机完成事件
                if (event.getEventName().equals("CHANNEL_HANGUP_COMPLETE")) {
                    System.err.println("CHANNEL_HANGUP_COMPLETE");
                }
            }

            @Override
            public void backgroundJobResultReceived(EslEvent event) {
                /// String uuid = event.getEventHeaders().get("Job-UUID");
                log.info("Background job result received+:" + event.getEventName() + "/" + event.getEventHeaders());
                // +"/"+JoinString(event.getEventHeaders())+"/"+JoinString(event.getEventBodyLines()));
            }
        });
        client.setEventSubscriptions("plain", "all");
        return client;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = inBand();
        if (client != null) {
            // 呼叫1002-播放语音
            client.sendSyncApiCommand("originate", "{ignore_early_media=true}user/1002 &playback(text/end.wav)");
            // 呼叫手机-执行lua脚本
            // client.sendSyncApiCommand("originate", "{ignore_early_media=true}sofia/gateway/fs_sg/18621730742 &lua(welcome.lua)");
            // 建立1002和1000的通话
            ///client.sendSyncApiCommand("originate", "user/1002 &bridge(user/1000)");
            client.close();
        }
    }

}
