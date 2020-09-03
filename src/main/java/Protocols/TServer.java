package Protocols;

import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConfiguration;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConnectionFailureContext;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyConnectionFailureHandler;
import com.genesyslab.platform.applicationblocks.warmstandby.WarmStandbyService;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.voice.protocol.ConnectionSettings;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;

import java.net.URI;
import java.net.URISyntaxException;

import static Protocols.Logging.logger;

public class TServer {


    public static void main (String [] args) throws InterruptedException, URISyntaxException {

        Endpoint endpoint = new Endpoint(new URI("tcp://192.168.66.188:7014"));
        Endpoint backup_endpoint = new Endpoint(new URI("tcp://192.168.66.188:7025"));
        TServerProtocol tsp0 = new TServerProtocol(endpoint);
        WarmStandbyService ws = new WarmStandbyService(tsp0);
        MessageHandler mh0 = logger::debug;

        new Logging();

        String sUserPass = "4(,\\{BeGHLMQrTVz";
        logger.info("\n\n\t--->" + sUserPass + "<---\t\n\n");

        ConnectionSettings cs = new ConnectionSettings();
            cs.setApplicationName("TServ_810");
            cs.setBackupReconnectInterval(15000L);
            cs.setEndpoint(endpoint);
            cs.setPassword("");
            cs.setSwitchoverTimeout(5000L);

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        WarmStandbyConfiguration wsConfig = new WarmStandbyConfiguration(endpoint, backup_endpoint);
            wsConfig.setTimeout(3000);
            wsConfig.setAttempts((short) 3);
            wsConfig.setSwitchovers((short) 3);

        WarmStandbyConnectionFailureHandler wsHandler = new WarmStandbyConnectionFailureHandler() {
            @Override
            public boolean handleRegistrationFailure(WarmStandbyConnectionFailureContext warmStandbyConnectionFailureContext) {
                return false;
            }

            @Override
            public boolean handleConnectionFailure(WarmStandbyConnectionFailureContext warmStandbyConnectionFailureContext) {
                return false;
            }
        };

            ws.applyConfiguration(wsConfig);
            ws.setConnectionFailureHandler(wsHandler);
            ws.start();
            tsp0.setClientName("iWDManager");
            tsp0.setMessageHandler(mh0);

        try {
            tsp0.open();
        } catch (ProtocolException e) {
            logger.error(e);
        }

        String thisDN = "8000";

        RequestRegisterAddress rra = RequestRegisterAddress.create();
            rra.setThisDN(thisDN);

        logger.info("\n\nRequestRegisterAddress is: \n" + rra + "\n<---end of message--->\n\n");

        try {
            tsp0.send(rra);
            //logger.info("\n--->\nResp is: \n" + loginResponseHandler + "\n<---end of message--->\n\n");
        } catch (ProtocolException e) {
            logger.error(e);
        }
    }
}
