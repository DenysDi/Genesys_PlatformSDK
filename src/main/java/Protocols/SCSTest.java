package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.management.protocol.SolutionControlServerProtocol;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.ControlObjectType;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.events.EventInfo;
import com.genesyslab.platform.management.protocol.solutioncontrolserver.requests.applications.RequestStartApplication;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;
import org.apache.log4j.BasicConfigurator;

import static Protocols.Logging.logger;

public class SCSTest {
    public static void main (String [] args) throws WSException, InterruptedException, ConfigException, ProtocolException {

        new Logging();

        int refid = 322223;

        ConfServerProtocol confServer = new ConfServerProtocol();
            confServer.setUserName("default");
            confServer.setUserPassword("password");
            confServer.setClientName("default");
        Endpoint[] endpoints = new Endpoint[2];
            endpoints[0] = new Endpoint("192.168.66.188", 2020);
            endpoints[1] = new Endpoint("192.168.66.188", 2021);
        WarmStandby ws = new WarmStandby(confServer, endpoints);
        IConfService confService = ConfServiceFactory.createConfService(confServer);    //	initialization of COM ABlock functionality
            ws.open();

        SolutionControlServerProtocol scsProtocol = new SolutionControlServerProtocol(new Endpoint("192.168.66.188", 7001));
            scsProtocol.setClientId(322223);
            scsProtocol.setClientName("SCSClient");
            scsProtocol.setClientType(ControlObjectType.Unknown);
            scsProtocol.setUserName("SCSUserClient");

        MessageHandler scsrMessageHandler = new MessageHandler() {
            public void onMessage(Message message) {
                switch(message.messageId()){
                    case EventInfo.ID:
                        OnEventInfo(message);
                        break;
                    //Other events.
                }
            }
        };
            scsProtocol.setMessageHandler(scsrMessageHandler);

        try {
            scsProtocol.open();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CfgApplicationQuery cfgAppQ = new CfgApplicationQuery();
            cfgAppQ.setName("Stat_Server_851");

        CfgApplication cfgApp = confService.retrieveObject(cfgAppQ);
        Integer appIdIs = cfgApp.getDBID();

        RequestStartApplication rsa = RequestStartApplication.create();
            rsa.setReferenceId(refid);
            rsa.setControlObjectId(appIdIs);
            scsProtocol.request(rsa);
        Thread.sleep(30000);
    }

    private static void OnEventInfo(Message message) {
        logger.debug("Event info: \n" + message);
    }
}
