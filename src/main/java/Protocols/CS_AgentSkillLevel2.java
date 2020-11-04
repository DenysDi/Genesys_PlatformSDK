package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

public class CS_AgentSkillLevel2 {
    public static void main (String []args) {

        new Logging();

        Endpoint endpoint = new Endpoint("192.168.66.188", 2020);
        ConfServerProtocol csp = new ConfServerProtocol(endpoint);
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");
            csp.setClientApplicationType(CfgAppType.CFGSCE.asInteger());

        MessageHandler mh = message -> System.out.println("\n--->Received via mh:\n" + message + "\nEnd of message\n<---");
            csp.setMessageHandler(mh);

        IConfService confService = ConfServiceFactory.createConfService(csp);

        WarmStandby ws = new WarmStandby(csp, endpoint);
        try {
            ws.open();
        } catch (InterruptedException | WSException e) {
            e.printStackTrace();
        }

        /*ConfObjectDelta agentDelta = new ConfObjectDelta(metadata, CfgObjectType.CFGPerson);

        ConfObject obj = (ConfObject) agentDelta.getOrCreatePropertyValue("deltaHost");
            obj.setPropertyValue("DBID", objDbid);              // - required
            obj.setPropertyValue("name", "new-host-name");      // - to rename the host object (if needed)
            obj.setPropertyValue("IPaddress", "21.21.21.21");   // - to change address (if needed)

        RequestUpdateObject reqUpdate = RequestUpdateObject.create();
            reqUpdate.setObjectDelta(agentDelta);

        Message resp = confService.retrieveObject();

        if (resp instanceof EventObjectUpdated) {
            //The object has been updated
        } else if (resp instanceof EventError) {
        logger.error("Error on object update: "
                    + "CfgUtilities.getErrorCode(((EventError) resp).getErrorCode())"
                    + "tDescription:  + ((EventError) resp).getDescription()");
        }*/
        try{
            ws.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
