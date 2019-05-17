package com.wake.fs.example1;

import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hui.zhou 2019/5/17
 */
public class ESLClient {
    private static final Logger logger = LoggerFactory.getLogger(ESLClient.class);

    //========esl ip地址 端口 密码===========
    private String host;
    private int port;
    private String password;
    private Client client;
    private boolean isConnect;
    private TopicSender topicSender;//此类只是用来发送mq信息用来其他程序消费

    public ESLClient(String host, int port, String password) {
        seting(host, port, password);

    }

    private void seting(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }


    public void connect() {
        try {
            client = new Client();
            client.connect(host, port, password, 1);
            addListener();
            isConnect = true;
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();

        }

    }

    private void addListener() {
        EslCoreEventListener myIEslEventListener = new EslCoreEventListener(topicSender);
        client.addEventListener(myIEslEventListener);
        client.setEventSubscriptions("plain", "all");
        //不区分大小写，对事件进行过滤，只关注需要的事件
        for (FSEventType fSEventType : FSEventType.values()) {
            client.addEventFilter("Event-Name", fSEventType.name());
        }
    }


    public void close() {
        client.close();
        isConnect = false;
    }


    public boolean canSend() {
        return client.canSend();
    }


    public boolean isConnect() {
        return isConnect;
    }


    public List<String> comand(String command) {
        List<String> strList = null;
        try {
            EslMessage elme = client.sendSyncApiCommand(command, "");
            strList = elme.getBodyLines();
            for (String string : strList) {
                logger.info("  " + string);
            }
        } catch (java.lang.IllegalStateException e) {
            e.printStackTrace();
            strList = new ArrayList<String>();
            strList.add(e.getMessage());
            //清除该缓存
            ESLClientMap.remove(this.host);
        }


        return strList;
    }


    public String comandString(String command) {
        List<String> strList = comand(command);
        StringBuffer sb = new StringBuffer();
        for (String str : strList) {
            sb.append("<font color='red'>" + str + "</font></br>");
        }
        return sb.toString();
    }


    public void setTopicSender(TopicSender topicSender) {
        this.topicSender = topicSender;
    }


    public String getHost() {
        return host;
    }


    public int getPort() {
        return port;
    }


    public static void main(String[] args) throws Exception {
        ESLClient e = new ESLClient("172.19.60.174", 8021, "ClueCon");
        e.connect();

        logger.info(e.isConnect() + "");
    }
}
