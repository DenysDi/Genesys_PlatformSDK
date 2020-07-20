package Protocols;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.outbound.protocol.OutboundServerProtocol;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.CallbackType;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.OutboundDesktopBinding;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.ScheduledRecordReschedule;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;

import java.util.Date;

import static Protocols.Logging.logger;

public class SRRTest {
    public void main (String []args) throws InterruptedException, ProtocolException, OutboundDesktopBinding.KVBindingException {

        new Logging();

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        Endpoint endpoint = new Endpoint("192.168.66.188", 7015);
        Endpoint endpoint1 = new Endpoint("192.168.66.188", 7015);

        OutboundServerProtocol outboundServerProtocol = new OutboundServerProtocol(endpoint1);

        TServerProtocol tsp0 = new TServerProtocol();
            tsp0.setClientName("iWDManager");
            tsp0.setEndpoint(endpoint);
        MessageHandler mh0 = System.out::println;
            tsp0.setMessageHandler(mh0);
            tsp0.open();

        ScheduledRecordReschedule srr = new ScheduledRecordReschedule();
            srr.setApplicationId(322223);
            srr.setCampaignName("Airlines");
            srr.setCallbackType(CallbackType.Campaign);
            srr.setDateTime(new Date());
            srr.setRecordHandle(32222355);

        KeyValueCollection kvc = OutboundDesktopBinding.marshal(srr);

        CommonProperties cp = CommonProperties.create();
            cp.setUserData(kvc);

        RequestDistributeUserEvent rdue = RequestDistributeUserEvent.create();
            rdue.setReferenceID(322223);
            rdue.setUserEvent(cp);
            rdue.setCommunicationDN("9001");

            tsp0.request(rdue);
            tsp0.close();

        Message MessageRetrun = tsp0.request(rdue);
        logger.info(MessageRetrun);
    }
}
