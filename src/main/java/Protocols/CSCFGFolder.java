package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collection;


public class CSCFGFolder {

    private static final Logger logger = LogManager.getLogger(CSCFGFolder.class);

    public static void main (String [] args) throws ConfigException, InterruptedException {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());
        BasicConfigurator.configure();

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
        } catch (WSException e) {
            logger.error("\n\n--->WSException: ", e);
        }
        CfgApplicationQuery aq = new CfgApplicationQuery(confService);
            aq.setIsServer(CfgFlag.CFGFalse);

        Collection<ICfgObject> list = confService.retrieveMultipleObjects(ICfgObject.class, aq);
        System.out.println("\n--->\nCollection<ICfgObject> list:\n" + list + "\n<---\n");
            ws.close();
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
