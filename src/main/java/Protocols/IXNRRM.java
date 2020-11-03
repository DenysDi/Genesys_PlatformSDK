package Protocols;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocol;
import com.genesyslab.platform.openmedia.protocol.interactionserver.InteractionClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.ReasonInfo;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.RequestRegisterClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestAddMedia;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestAgentLogin;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestRemoveMedia;

import java.net.URI;

public class IXNRRM {
    public static void main (String [] args) throws Exception {

        int prxid = 5;
        String media = "chat";

        new Logging();

        InteractionServerProtocol ixn = new InteractionServerProtocol(new Endpoint(new URI("tcp://DD-WIN12R2-ES:7004")));
            ixn.setClientName("WDE_851_wdeadmin");
            ixn.setClientType(InteractionClient.AgentApplication);
            ixn.setProxyId(prxid);
            ixn.open();

        RequestRegisterClient rrc = RequestRegisterClient.create();
            rrc.setClientName("WDE_851_wdeadmin");
            rrc.setClientType(InteractionClient.Proxy);
            rrc.setMediaType(media);
            rrc.setProxyClientId(prxid);

            ixn.request(rrc);

        RequestAgentLogin ral = RequestAgentLogin.create();
            ral.setAgentId("wdeadmin");
            ral.setReason(null);
            ral.setTenantId(101);
            ral.setProxyClientId(prxid);
            ral.setPlaceId("wdeadmin");
            ral.setTenantId(1);

            //ixn.request(ral);

        RequestAddMedia ram = RequestAddMedia.create();
            ram.setMediaTypeName("media");
            ram.setProxyClientId(prxid);
            ram.setReason(ReasonInfo.create("0","0"));

            //ixn.request(ram);

        RequestRemoveMedia rrm = RequestRemoveMedia.create();
            rrm.setProxyClientId(prxid);
            rrm.setMediaTypeName(media);

            ixn.request(rrm);


            //ixn.request(ral);
            //ixn.setProxyId(prxid);
            //ixn.request(rrm);
            //ixn.close();
    }
}
