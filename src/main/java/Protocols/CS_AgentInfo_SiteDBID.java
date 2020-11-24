package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFolderQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import java.util.Collection;

import static Protocols.Logging.logger;

public class CS_AgentInfo_SiteDBID {
    public static void main (String [] args) throws InterruptedException {
        new Logging();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
        MessageHandler mh = logger::debug;

        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());
            csp.setMessageHandler(mh);

        WarmStandby ws = new WarmStandby(csp, endpoint);
        MessageHandler mh1 = logger::debug;
        IConfService confService = ConfServiceFactory.createConfService(csp);
            confService.setUserMessageHandler(mh1);

        try {
            ws.open();
        } catch (InterruptedException | WSException e) {
            logger.error(e.getMessage());
        }

        CfgFolderQuery cfgFQ = new CfgFolderQuery(confService);
            cfgFQ.setDbid(495);

            logger.debug("\n--->\nCfgPersonQuery:\n" + /*cfgpq +*/ "\n<---\n");
        try {
            //CfgPerson persons = confService.retrieveObject(CfgPerson.class, cfgFQ);
            Collection <CfgPerson> persons = confService.retrieveMultipleObjects(CfgPerson.class, cfgFQ);
            for (CfgPerson person:persons) {
                persons.add(person);
            }
            
            /*ICollection<CfgAgentGroup> someGroup = confService.RetrieveMultipleObjects<CfgAgentGroup>(AgGquery);
	            foreach (CfgAgentGroup AgG in someGroup)
                {
                    DNsOut = "";
                    foreach (CfgDN DN in AgG.GroupInfo.RouteDNs)
                    {
                        DNsOut += DN.Name + " ";
                        if (DN.Number == DNsToSearch) GroupReturned = AgG.GroupInfo.Name + " ";
                    };
            
                    AgGOut +="(" + AgG.GroupInfo.Name + " DNs there:" + DNsOut+" )";
            
                };*/
            logger.info("\n--->\nPersons Collection:\n" + persons + "\n<---\n");
        } catch (ConfigException e) {
            logger.error(e.getMessage());
        }
    }
}
