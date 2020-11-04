package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.standby.WarmStandby;

import java.util.Collection;

import static Protocols.Logging.logger;

public class CS_CFGIsServ {
    public static void main (String [] args) {

        new Logging();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);

        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

        MessageHandler mh = System.out::println;

            csp.setMessageHandler(mh);

        WarmStandby ws = new WarmStandby(csp, endpoint);

        IConfService confService = ConfServiceFactory.createConfService(csp);

        MessageHandler mh1 = System.out::println;

            confService.setUserMessageHandler(mh1);
            ws.openAsync();
        if (ws.isOpened()) {
            logger.info("\n---> Opening connection... <---\n");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.debug("\n--->\n" + "1st attempt" + "\n<---\n");
            CfgApplicationQuery query = new CfgApplicationQuery();
            //query.setTenantDbid(1);
            query.setIsServer(CfgFlag.valueOf(1));
            Collection<CfgApplication> application = null;

            try {
                application = confService.retrieveMultipleObjects(CfgApplication.class, query);
            } catch (ConfigException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.info("\n--->\n" + application + "\n<---\n");
        }else if (ws.isOpened()){

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.debug("\n--->\n" + "2nd attempt" + "\n<---\n");
            CfgApplicationQuery query1 = new CfgApplicationQuery();
            //query1.setTenantDbid(1);
            query1.setIsServer(CfgFlag.CFGTrue);
            Collection<CfgApplication> application1 = null;

            try {
                application1 = confService.retrieveMultipleObjects(CfgApplication.class, query1);
            } catch (ConfigException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.debug("\n--->\n" + application1 + "\n<---\n");
        }else {
            logger.debug("\n--->\n" + "Time is up!!!" + "\n<---\n");
            ws.closeAsync();
        }
    }
}

