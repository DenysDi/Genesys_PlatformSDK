package Protocols;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocol;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocolListener;
import com.genesyslab.platform.openmedia.protocol.interactionserver.InteractionClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.events.EventAck;
import com.genesyslab.platform.openmedia.protocol.interactionserver.events.EventSnapshotInteractions;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.RequestRegisterClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestStartPlaceAgentStateReportingAll;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ReqRegCli {

    private static final Logger logger = LogManager.getLogger(ReqRegCli.class);

    public static void main(String[] args) {

        new Logging();

        Endpoint endpoint = new Endpoint("IXN_853", "DD-WIN12R2-ES", 7004);

        InteractionServerProtocol ixn = new InteractionServerProtocol(endpoint);
            ixn.setClientName("PSDK-RE");
            ixn.setClientType(InteractionClient.ReportingEngine);

        final InteractionServerProtocolListener server = new InteractionServerProtocolListener(endpoint);
            server.setClientRequestHandler(requestContext -> {
                System.out.println("\n\n--->Server Context is: " + requestContext + "<---\n\n");
                Message msg = requestContext.getRequestMessage();
                Message response = null;
                Object refId = null;
                if (msg instanceof Referenceable) {
                    refId = ((Referenceable) msg).retreiveReference();
                }
                if (msg != null) if (msg.messageId() == RequestRegisterClient.ID) {
                    RequestRegisterClient request = (RequestRegisterClient) msg;
                    EventAck event = EventAck.create();
                    event.setProxyClientId(1);
                    response = event;
                } else if (msg.messageId() != EventSnapshotInteractions.ID) {
                    // handle some request
                    // ...
                    // create response
                }
                if (response != null) {
                    if (refId != null && response instanceof Referenceable) {
                        ((Referenceable) response).updateReference(refId);
                    }
                    try {
                        requestContext.respond(response);
                    } catch (final ProtocolException e) {
                        e.printStackTrace();
                    }
                }
            });

        KeyValueCollection kvc = new KeyValueCollection();
            kvc.addObject("Tenant","101");

        try {
            ixn.open();

            RequestStartPlaceAgentStateReportingAll rs = RequestStartPlaceAgentStateReportingAll.create(kvc);

            Message response = ixn.request(rs);
            logger.info("Response: " + response.cloneMessage());
        } catch (Exception exception) {
            logger.info("Exception while sending request to interaction server: " + exception.getMessage());
        }
    }
}
