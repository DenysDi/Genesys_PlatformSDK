package Protocols;

import com.genesyslab.platform.commons.protocol.*;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocol;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocolListener;
import com.genesyslab.platform.openmedia.protocol.interactionserver.events.EventAck;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.RequestRegisterClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestAgentLogin;

import static Protocols.Logging.logger;

public class IXN_SCL {
    public static void main(final String[] args) {
        new Logging();
        final int PORT = 11111;
        final InteractionServerProtocolListener server = new InteractionServerProtocolListener(new WildcardEndpoint(PORT));
        server.setClientRequestHandler(new ClientRequestHandler() {
            public void processRequest(final RequestContext requestContext) {
                Message msg = requestContext.getRequestMessage();
                Message response = null;
                Object refId = null;
                if (msg instanceof Referenceable) {
                    refId = ((Referenceable) msg).retreiveReference();
                }
                if (msg != null) {
                    if (msg.messageId() == RequestRegisterClient.ID) {
                        RequestRegisterClient request = (RequestRegisterClient) msg;
                        EventAck event = EventAck.create();
                        event.setProxyClientId(1);
                        response = event;
                    } else if (msg.messageId() == EventAck.ID) {
                        logger.debug("\n--->" + msg + "<---\n");
                    }
                }
                if (response != null) {
                    if (refId != null && response instanceof Referenceable) {
                        ((Referenceable) response).updateReference(refId);
                    }
                    try {
                        requestContext.respond(response);
                    } catch (final ProtocolException e) {
                        logger.error(e);
                    }
                }
            }
        });
        InteractionServerProtocol client = new InteractionServerProtocol(new Endpoint("iWDManager","DD-WIN12R2-ES", 7004));
        try {
            server.open();
            client.open();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int prxid = 5;
        // create "some request":
        RequestAgentLogin ral = RequestAgentLogin.create();
            ral.setAgentId("wdeadmin");
            ral.setReason(null);
            ral.setTenantId(101);
            ral.setProxyClientId(prxid);
            ral.setPlaceId("wdeadmin");
        //ixn.request(ral);
        try {
            Message response = client.request(ral);
            logger.info(response);
            client.close();
            server.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
    }
}
