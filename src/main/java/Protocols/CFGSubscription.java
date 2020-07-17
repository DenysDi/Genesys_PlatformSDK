package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

public class CFGSubscription {
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
            confService.setUserMessageHandler(mh);
        NotificationQuery nq = new NotificationQuery();
            nq.setTenantDbid(101);
        try {
            ws.open();
            confService.subscribe(nq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (WSException e) {
            e.printStackTrace();
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }
}
