package Protocols;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.CallbackType;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.OutboundDesktopBinding;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.ScheduledRecordReschedule;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;

import java.util.Date;

public class SRR {
    public static void main (String []args) {

        new Logging();

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        Endpoint endpoint = new Endpoint("192.168.66.188", 7015);

        TServerProtocol tsp0 = new TServerProtocol();
            tsp0.setClientName("iWDManager");
            tsp0.setEndpoint(endpoint);
        MessageHandler mh0 = System.out::println;
            tsp0.setMessageHandler(mh0);

        try {
            tsp0.open();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ScheduledRecordReschedule srr = new ScheduledRecordReschedule();
            srr.setApplicationId(322223);
            srr.setCampaignName("Airlines");
            srr.setCallbackType(CallbackType.Campaign);
            srr.setDateTime(new Date());
            srr.setRecordHandle(32222355);

        KeyValueCollection kvc = null;
        try {
            kvc = OutboundDesktopBinding.marshal(srr);
        } catch (OutboundDesktopBinding.KVBindingException e) {
            e.printStackTrace();
        }

        CommonProperties cp = CommonProperties.create();
            cp.setUserData(kvc);

        RequestDistributeUserEvent rdue = RequestDistributeUserEvent.create();
            rdue.setReferenceID(322223);
            rdue.setUserEvent(cp);
            rdue.setCommunicationDN("9001");

        try {
            tsp0.request(rdue);
            tsp0.close();
        } catch (ProtocolException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
