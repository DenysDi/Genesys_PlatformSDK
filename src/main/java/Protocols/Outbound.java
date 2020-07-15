package Protocols;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.outbound.protocol.OutboundServerProtocol;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.ScheduledRecordReschedule;
import com.genesyslab.platform.outbound.protocol.outboundserver.requests.RequestGetCampaignStatus;

import java.time.Instant;
import java.util.Date;

import static Protocols.Logging.logger;
import static com.genesyslab.platform.outbound.protocol.outbounddesktop.CallbackType.Campaign;

public class Outbound {

    public static void main(String[] args) throws InterruptedException, ProtocolException, ClassNotFoundException {

        new Logging();

        OutboundServerProtocol outboundServerProtocol = new OutboundServerProtocol(new Endpoint("OCS", "10.160.68.83", 6990));
            outboundServerProtocol.setClientName("PSDK");
            outboundServerProtocol.setUserName("default");
            outboundServerProtocol.setUserPassword("password");
        MessageHandler ssMessageHandler = message -> System.out.println("*****************\n" + "--->Received via MH:\n" + message + "\n<---\n" + "*****************\n");
            outboundServerProtocol.setMessageHandler(ssMessageHandler);
            outboundServerProtocol.open();

        RequestGetCampaignStatus rgcs = RequestGetCampaignStatus.create();
            rgcs.setCampaignId(102);
            //rgcs.setGroupId(140);

        Message str = outboundServerProtocol.request(rgcs);
        //outboundServerProtocol.(srr);
        //String msg = cga.getCampaignDescription();
        logger.debug("*****************\n" + "--->CampaignStatus is:\n" + str + "\n<---\n");

        ScheduledRecordReschedule srr = new ScheduledRecordReschedule();
            //srr.setApplicationId();
            srr.setCallbackType(Campaign);
            srr.setDateTime(Date.from(Instant.now()));
            srr.setCampaignName("Airlines Campaign");
            srr.setRecordHandle(252);
    }
}
