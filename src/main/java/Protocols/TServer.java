package Protocols;

import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.voice.protocol.TServerProtocol;

import static Protocols.Logging.logger;

public class TServer {
    public static void main (String [] args) {

        new Logging();

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        TServerProtocol tsp0 = new TServerProtocol();
            tsp0.setClientName("iWDManager");
        MessageHandler mh0 = x -> logger.debug(x);
            tsp0.setMessageHandler(mh0);
    }
}
