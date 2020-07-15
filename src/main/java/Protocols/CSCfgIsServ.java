package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgFlag;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;
import org.apache.log4j.BasicConfigurator;

import java.util.Collection;

public class CSCfgIsServ {
    public static void main (String [] args) throws ConfigException, WSException, InterruptedException, ProtocolException {
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
            ws.openAsync();
        System.out.println("\n---> Opening connection... <---\n");
        Thread.sleep(3000);
        if (ws.isOpened()) {
            System.out.println("\n--->\n" + "1st attempt" + "\n<---\n");
            CfgApplicationQuery query = new CfgApplicationQuery();
                //query.setTenantDbid(1);
                query.setIsServer(CfgFlag.valueOf(1));
            Collection<CfgApplication> application = confService.retrieveMultipleObjects(CfgApplication.class, query);
            System.out.println("\n--->\n" + application + "\n<---\n");
        }else if (ws.isOpened()){
            Thread.sleep(3000);
            System.out.println("\n--->\n" + "2nd attempt" + "\n<---\n");
            CfgApplicationQuery query1 = new CfgApplicationQuery();
                //query1.setTenantDbid(1);
                query1.setIsServer(CfgFlag.CFGTrue);

            Collection<CfgApplication> application1 = confService.retrieveMultipleObjects(CfgApplication.class, query1);


            System.out.println("\n--->\n" + application1 + "\n<---\n");
        }else {
            System.out.println("\n--->\n" + "Time is up!!!" + "\n<---\n");
            ws.closeAsync();
        }
    }
}
