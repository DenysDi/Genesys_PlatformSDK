package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import java.util.EventObject;

import static Protocols.Logging.logger;

public class ConfDelayReq {
    public static void main (String []args) throws InterruptedException {

        new Logging();

        String environmentName = "Environment";

        logger.info("Initializing Genesys Environment for " + environmentName);

        PropertyConfiguration confservConfig = new PropertyConfiguration();
            confservConfig.setUseAddp(true);
            confservConfig.setAddpServerTimeout(10);
            confservConfig.setAddpClientTimeout(10);
            confservConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020, confservConfig);

        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

        MessageHandler conf_mh = logger::debug;

            csp.setMessageHandler(conf_mh);

        IConfService confService = ConfServiceFactory.createConfService(csp);

            csp.addChannelListener(new ChannelListener() {
                @Override
                public void onChannelOpened(EventObject eventObject) {
                    logger.info("\n---> Channel Opened!!! <---\n");
                }

                @Override
                public void onChannelClosed(ChannelClosedEvent channelClosedEvent) {
                    logger.info("\n---> Channel Closed!!! <---\n");
                }

                @Override
                public void onChannelError(ChannelErrorEvent channelErrorEvent) {
                    logger.error("\n---> Channel Error!!!\n" + channelErrorEvent + "\n<---\n");
                }
            });

        try {
            csp.open();
        } catch (InterruptedException | ProtocolException e) {
            logger.error(e.getMessage());
        }

        String applicationName0 = "SNMP_MA_851";
        logger.info("\n--->\nGetting 1st application: " + applicationName0 + "\n<---\n");
        CfgApplicationQuery query0 = new CfgApplicationQuery();
            query0.setName(applicationName0);
        CfgApplication application0 = null;

        try {
            application0 = confService.retrieveObject(query0);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
        }

        logger.info("\n--->\nGot an application: " + application0 + "\n<---\n");

        try {
            logger.info("\n--->\nThread.sleep started!!!\n<---\n");
            Thread.sleep(3000000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }

        String applicationName1 = "ChatServer_853";
        logger.info("\n--->\nGetting 2nd application: " + applicationName1 + "\n<---\n");
        CfgApplicationQuery query1 = new CfgApplicationQuery();
            query1.setName(applicationName1);
        CfgApplication application1 = null;

        try {
            application1 = confService.retrieveObject(query1);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
        }

        logger.info("\n--->\nGot an application: " + application1 + "\n<---\n");

        try {
            csp.close();
        } catch (ProtocolException e) {
            logger.error(e.getMessage());
        }
    }
}
