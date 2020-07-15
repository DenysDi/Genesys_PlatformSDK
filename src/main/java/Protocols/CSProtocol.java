package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

public class CSProtocol {
    public static void main (String [] args) throws ConfigException, WSException, InterruptedException {
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());

        ConfServerProtocol confServer = new ConfServerProtocol();
            confServer.setUserName("default");
            confServer.setUserPassword("password");
            confServer.setClientName("default");
        Endpoint[] endpoints = new Endpoint[2];
            endpoints[0] = new Endpoint("192.168.66.188", 2020);
            endpoints[1] = new Endpoint("192.168.66.188", 2021);
        WarmStandby ws = new WarmStandby(confServer, endpoints);
        IConfService confService = ConfServiceFactory.createConfService(confServer);    //	initialization of COM ABlock functionality
            ws.open();

        CfgPersonQuery CFgP = new CfgPersonQuery(confService);
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
        System.out.println(pass1);
    }
}
