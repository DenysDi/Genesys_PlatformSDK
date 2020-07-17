package Protocols;

import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;

import static Protocols.Logging.logger;


public class TServProtocol {
    public static void main (String [] args) throws ProtocolException, WSException, InterruptedException {

        new Logging();

        /*ConnectionSettings cs = new ConnectionSettings();
            cs.setApplicationName("PSDK");
            cs.setEndpoint(new Endpoint("DD-WIN12R2-FR", 7014));
            cs.setSwitchoverTimeout(1000);

        TServerProtocol tsp001 = new TServerProtocol();
            tsp001.connectionContext(cs);*/



        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        TServerProtocol tsp0 = new TServerProtocol();
            tsp0.setClientName						("iWDManager0");
        MessageHandler mh0 = x -> logger.info(x);
            tsp0.setMessageHandler(mh0);

        TServerProtocol tsp1 = new TServerProtocol();
            tsp1.setClientName						("iWDManager1");
        MessageHandler mh1 = message -> logger.info(message);
            tsp1.setMessageHandler(mh1);

        TServerProtocol tsp2 = new TServerProtocol();
            tsp2.setClientName						("iWDManager2");
        MessageHandler mh2 = message -> logger.info(message);
            tsp2.setMessageHandler(mh2);

        RequestAgentLogin Rlogin0 = RequestAgentLogin.create();
            Rlogin0.setAgentID("wdeadmin");
            Rlogin0.setReasons(null);
            Rlogin0.setThisDN("8000");
            Rlogin0.setThisQueue("8888");

        RequestRegisterAddress RegDn0 = RequestRegisterAddress.create();
            RegDn0.setThisDN								("8000");

        RequestAgentLogin Rlogin1 = RequestAgentLogin.create();
            Rlogin1.setAgentID("agent1");
            Rlogin1.setReasons(null);
            Rlogin1.setThisDN("8001");
            Rlogin1.setThisQueue("8888");

        RequestRegisterAddress RegDn1 = RequestRegisterAddress.create();
            RegDn1.setThisDN								("8001");

        RequestAgentLogin Rlogin2 = RequestAgentLogin.create();
            Rlogin2.setAgentID("agent2");
            Rlogin2.setReasons(null);
            Rlogin2.setThisDN("8002");
            Rlogin2.setThisQueue("8888");

        RequestRegisterAddress RegDn2 = RequestRegisterAddress.create();
            RegDn2.setThisDN								("8002");

        WarmStandby ws = new WarmStandby(tsp0);
        WSConfig  cfg = new WSConfig()
                .setEndpoints(new Endpoint("DD-WIN12R2-FR", 7014), new Endpoint("DD-WIN12R2-FR", 7014))
                .setBackupDelay(2000)
                .setReconnectionRandomDelayRange(5000)
                .setRetryDelay(100, 500, 5000)
                .setTimeout(10000);

            ws.setConfig(cfg);
            ws.autoRestore();
            ws.open();
            if (ws.isOpened()){
                System.out.println("\n\n***_ws0 is opened!_***\n\n");
                tsp0.request(RegDn0);
                tsp0.request(Rlogin0);
                System.out.println("\n\n***_All requests sent!_***\n\n");
            }else{
                System.out.println("\n\n***_ws0 is NOT opened!_***\n\n");
            }

        WSConfig  cfg1 = new WSConfig()
                .setEndpoints(new Endpoint("DD-WIN12R2-FR", 7014), new Endpoint("DD-WIN12R2-FR", 7014))
                .setBackupDelay(2000)
                .setReconnectionRandomDelayRange(5000)
                .setRetryDelay(100, 500, 5000)
                .setTimeout(10000);
            ws.setConfig(cfg1);
            ws.open();
            if (ws.isOpened()){
                System.out.println("\n\n***_ws1 is opened!_***\n\n");
                tsp1.request(RegDn1);
                tsp1.request(Rlogin1);
                System.out.println("\n\n***_All requests sent!_***\n\n");
            }else{
                System.out.println("\n\n***_ws1 is NOT opened!_***\n\n");
            }

        WSConfig  cfg2 = new WSConfig()
                .setEndpoints(new Endpoint("DD-WIN12R2-FR", 7014), new Endpoint("DD-WIN12R2-FR", 7014))
                .setBackupDelay(2000)
                .setReconnectionRandomDelayRange(5000)
                .setRetryDelay(100, 500, 5000)
                .setTimeout(10000);
            ws.setConfig(cfg2);
            ws.open();
            if (ws.isOpened()){
                logger.info("\n\n***_ws2 is opened!_***\n\n");
                tsp2.request(RegDn2);
                tsp2.request(Rlogin2);
                logger.info("\n\n***_All requests sent!_***\n\n");
            }else{
                logger.info("\n\n***_ws2 is NOT opened!_***\n\n");
            }

            //ws.close();
    }
}
