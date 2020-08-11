package Protocols;

import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;

import java.net.URI;
import java.net.URISyntaxException;

import static Protocols.Logging.logger;

public class TServer {


    public static void main (String [] args) {

        CompletionHandler loginResponseHandler = new  CompletionHandler() {

            @Override
            public void completed(Object o, Object o2) {

            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }

        };

        new Logging();

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
        tserverConfig.setUseAddp(true);
        tserverConfig.setAddpServerTimeout(10);
        tserverConfig.setAddpClientTimeout(10);
        tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        TServerProtocol tsp0 = null;
        try {
            tsp0 = new TServerProtocol(new Endpoint(new URI("tcp://192.168.66.188:7014")));
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        tsp0.setClientName("iWDManager");
        MessageHandler mh0 = x -> logger.debug(x);
        tsp0.setMessageHandler(mh0);
        try {
            tsp0.beginOpen();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        String thisDN = "8000";

        RequestRegisterAddress rra = RequestRegisterAddress.create();
        rra.setThisDN(thisDN);

        logger.info("\n\nRequestRegisterAddress is: \n" + rra + "\n<---end of message--->\n\n");

        try {
            loginResponseHandler = (CompletionHandler) tsp0.beginRequest(rra);
            logger.info("\n--->\nResp is: \n" + loginResponseHandler + "\n<---end of message--->\n\n");
        } catch (ProtocolException e) {
            logger.error(e);
        }
    }
}
