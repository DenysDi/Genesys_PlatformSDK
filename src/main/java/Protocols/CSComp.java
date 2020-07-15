package Protocols;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import org.apache.log4j.BasicConfigurator;

public class CSComp {
    public static void main (String [] args) {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());
        BasicConfigurator.configure();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
                csp.setUserName("default");
                csp.setUserPassword("password");
                csp.setClientName("default");
                csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

        //WarmStandby ws = new WarmStandby(csp, endpoint);
        /*IConfService confService = ConfServiceFactory.createConfService(csp);    //	initialization of COM ABlock functionality
            csp.open();

        CfgAgentInfo CFGsq = new CfgAgentInfo(confService, confService.retrieveObject(CfgObjectType.CFGPerson, args.length));
        CFGsq.setUserName("wdetest");
        CfgSkillLevel CFGs = confService.retrieveObject(CFGsq);
        System.out.println("\n--->\nReturnedCfgPerson is: " + RP + "\n<---\n");
        CfgAgentInfo CFGai = new CfgAgentInfo(confService, );
        CFGai

        /*CfgCampaignGroupQuery cfgCGQ = new CfgCampaignGroupQuery(confService);
        //cfgCGQ.setName("TestComp@IQ>=50");   //    Name of CampaignGroup that do you want to change
        //cfgCGQ.executeSingleResult();
        cfgCGQ.setCampaignDbid(101);
        cfgCGQ.setDbid(102);
        CfgCampaignGroup ReturnedCampaignGroup = confService.retrieveObject(cfgCGQ);
        System.out.println("\n--->\nReturnedCampaignGroup is: " + ReturnedCampaignGroup + "\n<---\n");
        ReturnedCampaignGroup.setGroupDBID(112);
        ReturnedCampaignGroup.save();


        CfgCampaignGroupQuery cfgCGQ1 = new CfgCampaignGroupQuery(confService);
        cfgCGQ1.setName("TestComp@IQ>=50");   //    Name of CampaignGroup that do you want to change
        CfgCampaignGroup ReturnedCampaignGroup1 = confService.retrieveObject(cfgCGQ1);
        System.out.println("\n--->\nReturnedCampaignGroup is: " + ReturnedCampaignGroup1 + "\n<---\n");

        /*CfgPersonQuery CFgP = new CfgPersonQuery(confService);
                CFgP.setUserName                    ("default");
        CfgPerson RP = confService.retrieveObject(CFgP);
        String pass = RP.getPassword();
            System.out.println(pass);
            if (pass == null) {
            CfgPersonQuery CFgP1 = new CfgPersonQuery(confService);
            CFgP1.setUserName                    ("default");
            CfgPerson RP1 = confService.retrieveObject(CFgP1);
            RP1.setPassword("password");
            RP1.save();
        }
        CfgPersonQuery CFgP2 = new CfgPersonQuery(confService);
                CFgP2.setUserName                    ("default");
        CfgPerson RP2 = confService.retrieveObject(CFgP2);
        String pass1 = RP2.getPassword();
            System.out.println(pass1);*/
    }
}
