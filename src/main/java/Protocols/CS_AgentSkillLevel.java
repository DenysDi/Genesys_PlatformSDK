package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkill;
import com.genesyslab.platform.applicationblocks.com.objects.CfgSkillLevel;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.applicationblocks.com.queries.CfgSkillQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import static Protocols.Logging.logger;

public class CS_AgentSkillLevel {
    public static void main (String [] args) {

        new Logging();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

        MessageHandler mh = message -> System.out.println("\n--->Received via mh:\n" + message + "\nEnd of message\n<---");
            csp.setMessageHandler(mh);
        WarmStandby ws = new WarmStandby(csp, endpoint);
        IConfService confService = ConfServiceFactory.createConfService(csp);
        try {
            ws.open();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (WSException e) {
            e.printStackTrace();
        }

        CfgPersonQuery cfgPQ = new CfgPersonQuery(confService);
            cfgPQ.setDbid(106);
        //CfgPerson cfgP = confService.retrieveObject(cfgPQ);
        //System.out.println("\n--->\nPerson info before: " + cfgP + "\n<---\n" );

        CfgSkillQuery cfgSkillQuery = new CfgSkillQuery(confService);
            cfgSkillQuery.setDbid(102);
        //query for skill with unique identifier 103
        CfgPersonQuery cfgPersonQuery = new CfgPersonQuery(confService);
            cfgPersonQuery.setDbid(106);
        //get person
        CfgPerson cfgPerson = null;
        CfgSkill cfgSkill = null;
        try {
            cfgPerson = cfgPersonQuery.executeSingleResult();
            cfgSkill = cfgSkillQuery.executeSingleResult();
        } catch (ConfigException e) {
            e.printStackTrace();
        }
        //get skill

        CfgSkillLevel cfgSkillLevel = new CfgSkillLevel(confService, cfgPerson);
            cfgSkillLevel.setSkill(cfgSkill);
            cfgSkillLevel.setSkillDBID(cfgSkill.getDBID());
            cfgSkillLevel.setLevel(0);
            cfgPerson.getAgentInfo().getSkillLevels().add(cfgSkillLevel);
        try {
            cfgPerson.save();
        } catch (ConfigException e) {
            e.printStackTrace();
        }

        CfgPersonQuery cfgPQ1 = new CfgPersonQuery(confService);
            cfgPQ1.setDbid(106);
        //CfgPerson cfgP1 = confService.retrieveObject(cfgPQ1);
        //System.out.println("\n--->\nPerson info after: " + cfgP1 + "\n<---\n" );

        CfgPersonQuery cfgPQ2 = new CfgPersonQuery(confService);
            cfgPQ2.setDbid(106);
        CfgPerson cfgP2 = null;
        try {
            cfgP2 = confService.retrieveObject(cfgPQ2);
        } catch (ConfigException e) {
            e.printStackTrace();
        }
        logger.debug("\n--->\nPerson:\n" + cfgP2 + "\n<---\n" );
        try {
            ws.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
