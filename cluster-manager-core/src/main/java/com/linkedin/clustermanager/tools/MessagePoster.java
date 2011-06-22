package com.linkedin.clustermanager.tools;

import java.io.StringWriter;

import org.I0Itec.zkclient.ZkClient;

import com.linkedin.clustermanager.core.CMConstants;
import com.linkedin.clustermanager.impl.zk.ZNRecordSerializer;
import com.linkedin.clustermanager.model.Message;
import com.linkedin.clustermanager.model.ZNRecord;
import com.linkedin.clustermanager.util.CMUtil;

public class MessagePoster
{
    public void post(Message message)
    {

    }

    public static void main(String[] args)
    {
        String instanceName = "localhost_8900";
        String serverstring = "kgopalak-mn:2181";
        String msgSrc = "cm-instance-0";
        String msgId = "TestMessageId-2";
        String clusterName = "test-cluster";

        ZkClient client = new ZkClient(serverstring);
        client.setZkSerializer(new ZNRecordSerializer());
        Message message = new Message();
        message.setId(msgId);
        message.setMsgId(msgId);
        message.setSrcName(msgSrc);
        message.setTgtName(instanceName);
        message.setMsgState("new");
        message.setFromState("Slave");
        message.setToState("Master");
        message.setStateUnitKey("EspressoDB.partition-0." + instanceName);
        String path = CMUtil.getMessagePath(clusterName, instanceName) + "/"
                + message.getId();
        client.delete(path);
        ZNRecord record = client.readData(CMUtil.getLiveInstancePath(
                clusterName, instanceName));
        message.setTgtSessionId(record.getSimpleField(
                CMConstants.ZNAttribute.SESSION_ID.toString()).toString());
        client.createPersistent(path, message);
    }
}
