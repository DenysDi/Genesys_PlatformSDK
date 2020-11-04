package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFolderQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import java.util.Collection;

import static Protocols.Logging.logger;

public class CS_TestACE {

    public static void main (String [] args) {

        new Logging();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());
        //WarmStandby ws = new WarmStandby(confServer, endpoints);
        IConfService confService = ConfServiceFactory.createConfService(csp);    //	initialization of COM ABlock functionality
        try {
            csp.open();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //CfgACE cfgACE = new CfgACE(confService, );
            //RP.setDBID(103);
        //CfgACL cfgACL = new CfgACL(confService, RP);
            //cfgACE.getProperty("object_dbid");
            //cfgACL.setProperty("user_dbid", 103);
            //cfgACL.setProperty("object_type", 9);

            /*Collection<CfgApplication> application = confService.retrieveMultipleObjects(CfgApplication.class, query);
            System.out.println("\n--->\n" + application + "\n<---\n");*/

        //Collection<CfgACE> collection = confService.retrieveMultipleObjects(CfgACL.class, cfgACL.getACEs());

        //System.out.println(collection);

        CfgFolderQuery cfgFQ = new CfgFolderQuery(confService);
        cfgFQ.setObjectType(9);
        try {
            Collection<CfgFolder> msg = confService.retrieveMultipleObjects(CfgFolder.class, cfgFQ);
            logger.debug("\n\n--->CFG_Folder Collection: " + msg + "\nEnd of collection\n<---\n\n");
        } catch (ConfigException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                csp.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //(CfgACE) confService.retrieveMultipleObjects(CfgFolder.class, cfgFQ);
    }
}
