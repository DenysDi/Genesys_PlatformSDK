package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import java.util.Collection;

import static Protocols.Logging.logger;

public class CSCFGFolder {
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
        try {
            ws.open();
        } catch (WSException | InterruptedException e) {
            logger.error("\n\n--->WSException: ", e);
        }
        CfgApplicationQuery aq = new CfgApplicationQuery(confService);
            aq.setIsServer(CfgFlag.CFGFalse);

        Collection<ICfgObject> list = null;
        try {
            list = confService.retrieveMultipleObjects(ICfgObject.class, aq);
        } catch (ConfigException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("\n--->\nCollection<ICfgObject> list:\n" + list + "\n<---\n");
        try {
            ws.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //confService.re
        /*KeyValueCollection filterKey = new KeyValueCollection();
            filterKey.addObject("folder_name", "Transactions");

        CfgObjectType objectType = CfgObjectType.CFGTransaction;

        int intFolder = objectType.asInteger();

        RequestReadObjects requestReadObjects = RequestReadObjects.create(intFolder, filterKey);
        csp.send(requestReadObjects);

        CfgTransactionQuery tq = new CfgTransactionQuery(confService);
            tq.setTenantDbid(1);

        Collection<ICfgObject> list = confService.retrieveMultipleObjects(ICfgObject.class, tq);
        System.out.println("\n--->\nCollection<ICfgObject> list:\n" + list + "\n<---\n");*/

    }
}
