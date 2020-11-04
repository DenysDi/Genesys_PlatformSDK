package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.IWSHandler;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.exceptions.WSException;

import static Protocols.Logging.logger;

public class CS_CFGSubscription {
    public CS_CFGSubscription() {
        new Logging();
        Endpoint endpoint0 = new Endpoint("192.168.66.188", 2020);
        Endpoint endpoint1 = new Endpoint("192.168.66.156", 2020);
        ConfServerProtocol csp = new ConfServerProtocol();
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());
        MessageHandler mh = logger::debug;
                //System.out::println;
            csp.setMessageHandler(mh);
        WarmStandby ws = new WarmStandby(csp, endpoint0, endpoint1);
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
        IConfService confService = ConfServiceFactory.createConfService(csp);
            confService.setUserMessageHandler(mh);
        NotificationQuery nq = new NotificationQuery();
            nq.setTenantDbid(1);
        try {
            ws.open();
            confService.subscribe(nq);
        } catch (InterruptedException | WSException | ConfigException e) {
            logger.error(e);
        }
    }
}
