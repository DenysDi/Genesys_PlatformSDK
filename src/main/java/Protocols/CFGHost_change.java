package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgConnInfo;
import com.genesyslab.platform.applicationblocks.com.objects.CfgHost;
import com.genesyslab.platform.applicationblocks.com.objects.CfgServer;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgHostQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import java.util.Collection;

import static Protocols.Logging.logger;

public class CFGHost_change {

    private boolean verify (String num) {
        return num.matches("[0]{1}-[0-9]{2}-[0-9]{7}");
    }


    public static void main (String []args) {

        new Logging();
        String environmentName = "Environment";
            logger.info("Initializing Genesys Environment for " + environmentName);
        WarmStandby ws = null;
        try {
            Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
            ConfServerProtocol csp = new ConfServerProtocol(endpoint);
                csp.setUserName("default");
                csp.setUserPassword("password");
                csp.setClientName("default");
                csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

            MessageHandler mh = System.out::println;
                csp.setMessageHandler(mh);
            ws = new WarmStandby(csp, endpoint);
            IConfService confService = ConfServiceFactory.createConfService(csp);
                confService.setUserMessageHandler(mh);
            NotificationQuery nq = new NotificationQuery();
                nq.setTenantDbid(1);
                ws.open();
                confService.subscribe(nq);

            String applicationName = "ChatServer_853";
            String oldHostName = "DD-WIN12R2-ES";
            CfgHost oldHost = confService.retrieveObject(CfgHost.class, new CfgHostQuery(oldHostName));
            String newHostName = "DD-WIN12R2-FR";

                logger.info("Getting applicationName " + applicationName);

            CfgApplicationQuery query = new CfgApplicationQuery();
                query.setName(applicationName);
            CfgApplication application = confService.retrieveObject(query);

                logger.info("Got application: " + application + "\n<---\n");

            CfgServer applicationServerInfo = application.getServerInfo();
            CfgHost applicationHost = applicationServerInfo.getHost();
            Collection<CfgConnInfo> old_connInfo = application.getAppServers();
                logger.info("\n--->\nApplication connInfo: \n" + old_connInfo + "\n<---\n");

            CfgApplicationQuery cfgAQ = new CfgApplicationQuery(confService);
                cfgAQ.setServerDbid(167);
            Collection<CfgApplication> appcoll = confService.retrieveMultipleObjects(CfgApplication.class, cfgAQ);
            logger.info("\n--->\nCfgApplication collection: \n" + appcoll + "\n<---\n");

            /*Collection<CfgConnInfo> new_connInfo = old_connInfo;

            CfgConnInfo connInfo = new CfgConnInfo(confService, application);
            connInfo.setAppServerDBID(oldHost.getDBID());*/

                logger.info("Application " + application.getName() + " runs on CfgHost named " + applicationHost.getName() + " with DBID " + applicationHost.getDBID());

            //CfgHost oldHost = confService.retrieveObject(CfgHost.class, new CfgHostQuery(oldHostName));

                logger.info("Old host " + oldHostName + " is CfgHost named " + oldHost.getName() + " with DBID " + oldHost.getDBID());

            CfgHostQuery HostQuery = new CfgHostQuery();
                HostQuery.setName(newHostName);
            CfgHost newHost = confService.retrieveObject(HostQuery);

                logger.info("New host " + newHostName + " is CfgHost named " + newHost.getName() + " with DBID " + newHost.getDBID());

            CfgServer _applicationServerInfo = applicationServerInfo;
                _applicationServerInfo.setHost(newHost);
                logger.info("\n\n_applicationServerInfo is: \n" + _applicationServerInfo + "\n<---\n");
                application.setServerInfo(_applicationServerInfo);
                /*new_connInfo.removeAll(new_connInfo);
                application.setAppServers(new_connInfo);
                logger.info("\n--->\nCfgServer is: " + new_connInfo + "\n<---\n");*/
                application.save();

        } catch (ConfigException | InterruptedException | WSException e) {
                logger.error(e);
            try {
                ws.close();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
}
