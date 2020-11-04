package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.standby.IWSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.exceptions.WSException;

import static Protocols.Logging.logger;

public class CS_Protocol {
    public static void main (String [] args) throws ConfigException, WSException, InterruptedException {

        new Logging();

        ConfServerProtocol confServer = new ConfServerProtocol();
            confServer.setUserName("default");
            confServer.setUserPassword("password");
            confServer.setClientName("default");
        Endpoint[] endpoints = new Endpoint[2];
            endpoints[0] = new Endpoint("192.168.66.188", 2020);
            endpoints[1] = new Endpoint("192.168.66.156", 2020);
        WarmStandby ws = new WarmStandby(confServer, endpoints);

        IWSHandler iwsHandler = new IWSHandler() {
            @Override
            public void onChannelOpened(WSOpenedEvent wsOpenedEvent) {
                logger.info("\n\n\"onChannelOpened\" event is detected: " + wsOpenedEvent + " !\n\n");
            }

            @Override
            public void onChannelDisconnected(WSDisconnectedEvent wsDisconnectedEvent) {
                logger.debug("\n\n\"onChannelOpened\" event is detected: " + wsDisconnectedEvent + " !\n\n");
            }

            @Override
            public void onEndpointTriedUnsuccessfully(WSTriedUnsuccessfullyEvent wsTriedUnsuccessfullyEvent) {
                logger.error("\n\n\"onChannelOpened\" event is detected: " + wsTriedUnsuccessfullyEvent + " !\n\n");
            }

            @Override
            public void onAllEndpointsTriedUnsuccessfully(WSAllTriedUnsuccessfullyEvent wsAllTriedUnsuccessfullyEvent) {
                logger.error("\n\n\"onChannelOpened\" event is detected: " + wsAllTriedUnsuccessfullyEvent + " !\n\n");
            }
        };
        ws.setHandler(iwsHandler);

        IConfService confService = ConfServiceFactory.createConfService(confServer);    //	initialization of COM ABlock functionality
            ws.open();

        CfgPersonQuery CFgP = new CfgPersonQuery(confService);
            CFgP.setUserName                    ("default");
        CfgPerson RP = confService.retrieveObject(CFgP);
        String pass = RP.getPassword();
        logger.info(pass);
        if (pass == null) {
            CfgPersonQuery CFgP1 = new CfgPersonQuery(confService);
                CFgP1.setUserName                    ("default");
            CfgPerson RP1 = confService.retrieveObject(CFgP1);
                RP1.setPassword("password");
                RP1.save();
        }
        CfgPersonQuery CFgP2 = new CfgPersonQuery(confService);
            CFgP2.setUserName                    ("default");
        CfgPerson RP2 = confService.retrieveObject(CFgP2);
        String pass1 = RP2.getPassword();
        logger.info(pass1);
    }
}
