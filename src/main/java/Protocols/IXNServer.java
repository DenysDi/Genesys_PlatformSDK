package Protocols;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.openmedia.protocol.InteractionServerProtocol;
import com.genesyslab.platform.openmedia.protocol.interactionserver.InteractionClient;
import com.genesyslab.platform.openmedia.protocol.interactionserver.ReasonInfo;
import com.genesyslab.platform.openmedia.protocol.interactionserver.events.EventSnapshotInteractions;
import com.genesyslab.platform.openmedia.protocol.interactionserver.events.EventSnapshotTaken;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestAgentLogin;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.agentmanagement.RequestAgentLogout;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.interactionmanagement.RequestGetSnapshotInteractions;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.interactionmanagement.RequestTakeSnapshot;

import java.net.URI;

/**************************************************************************************************
 * Developed by Denys Diachenko, denys.diachenko@genesys.com                                      *
 * This file is part of FSinPSDK.                                                                 *
 *                                                                                                *
 * FSinPSDK. files can not be copied and/or distribute without the express                        *
 * permission of Denys Diachenko.                                                                 *
 * Last modified 11/12/18 6:31 PM.                                                                *
 * Copyright (c) 2018. - All Rights Reserved.                                                     *
 **************************************************************************************************/

public class IXNServer {
    public static void main (String [] args) throws Exception {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());

        int count = 1;

        InteractionServerProtocol ixn = new InteractionServerProtocol(new Endpoint(new URI("tcp://DD-WIN12R2-ES:7004")));
            ixn.setClientName("PSDK5");
            ixn.setClientType(InteractionClient.RoutingEngine);
            ixn.open();

        RequestAgentLogin ral = RequestAgentLogin.create();
            ral.setAgentId("wdeadmin");
            ral.setReason(ReasonInfo.create());
            ral.setTenantId(1);

        int prxid = 322223;

            ral.setProxyClientId(prxid);
            ral.setPlaceId("wdeadmin");
            ixn.request(ral);

        RequestAgentLogout rlogout = RequestAgentLogout.create();

        RequestTakeSnapshot RTS = RequestTakeSnapshot.create();
            RTS.setProxyClientId(prxid);
            RTS.setCondition("[media_type] = 'chat'");
            RTS.setLock(true);

        Message rep = ixn.request(RTS);

        System.out.println("Request Take Snapshot - " + rep);

        EventSnapshotTaken EST = (EventSnapshotTaken) rep;

        System.out.println("Event Snapshot Taken - " + EST);

        Integer a = EST.getNumberOfInteractions();

        System.out.println("Number Of Interactions - " + a);

        while (a>=count) {
            RequestGetSnapshotInteractions RGSI = RequestGetSnapshotInteractions.create();
                RGSI.setProxyClientId(prxid);
                RGSI.setSnapshotId(EST.getSnapshotId());
                RGSI.setStartFrom(0);
                RGSI.setNumberOfInteractions(1);
                RGSI.setReferenceId(EST.getReferenceId());

            Message repl = ixn.request(RGSI);

            System.out.println("Request Get Snapshot Interactions - " + repl);

            EventSnapshotInteractions ESI = (EventSnapshotInteractions) repl;

            System.out.println("Event Snapshot Interactions - " + ESI);
            System.out.println("Count - " + count);

            count++;
        }
            rlogout.setProxyClientId(prxid);
            ixn.request(rlogout);
            ixn.close();
    }
}
