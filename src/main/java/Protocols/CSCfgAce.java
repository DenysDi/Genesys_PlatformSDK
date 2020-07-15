package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestReadObjects2;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.exceptions.WSException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CSCfgAce {
    private static final Logger logger = LogManager.getLogger(CSCfgAce.class);
    public static void main (String [] args) throws ConfigException, WSException, InterruptedException, ProtocolException {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());
        BasicConfigurator.configure();

        ConfServerProtocol confServerProtocol = new ConfServerProtocol(new Endpoint("192.168.66.188", 2020));
            confServerProtocol.setClientApplicationType(CfgAppType.CFGSCE.asInteger());
            confServerProtocol.setClientName("default");
            confServerProtocol.setUserName("default");
            confServerProtocol.setUserPassword("password");

        MessageHandler tserverMessageHandler = message -> logger.info("\n--->\n" + message + "\n<---\n");

            confServerProtocol.setMessageHandler(tserverMessageHandler);
            confServerProtocol.open();

        int refid = 322223;

        /*CfgObjectType objectType = CfgObjectType.CFGEnumerator.CfgPermissions;
        int intPerson = objectType.asInteger();
        RequestReadObjects2 requestXpathQuery =
                RequestReadObjects2.create(
                        intPerson,
                        "CfgPermissions");*/

        RequestReadObjects2 rro = RequestReadObjects2.create();
            rro.setObjectType(4);
            rro.setReferenceId(refid);
            rro.setXPathExpression("CfgPermissions");

        logger.info("\n--->\nRequestReadObjects2:\n" + rro + "\n<---\n");
            confServerProtocol.send(rro);
        //confServerProtocol.receive();

        //Thread.sleep(50000);
            //confServerProtocol.close();
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
