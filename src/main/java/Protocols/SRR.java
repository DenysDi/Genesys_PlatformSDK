package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.connection.configuration.ClientADDPOptions;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.CallbackType;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.OutboundDesktopBinding;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.ScheduledRecordReschedule;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genesyslab.platform.voice.protocol.tserver.requests.special.RequestDistributeUserEvent;
import org.apache.log4j.BasicConfigurator;

import java.util.Date;

public class SRR {
    public static void main (String []args) throws InterruptedException, ProtocolException, OutboundDesktopBinding.KVBindingException {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());
        BasicConfigurator.configure();

        PropertyConfiguration tserverConfig = new PropertyConfiguration();
            tserverConfig.setUseAddp(true);
            tserverConfig.setAddpServerTimeout(10);
            tserverConfig.setAddpClientTimeout(10);
            tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.Both);

        Endpoint endpoint = new Endpoint("192.168.66.188", 7015);

        TServerProtocol tsp0 = new TServerProtocol();
            tsp0.setClientName("iWDManager");
            tsp0.setEndpoint(endpoint);
        MessageHandler mh0 = System.out::println;
            tsp0.setMessageHandler(mh0);

        tsp0.open();

        ScheduledRecordReschedule srr = new ScheduledRecordReschedule();
            srr.setApplicationId(322223);
            srr.setCampaignName("Airlines");
            srr.setCallbackType(CallbackType.Campaign);
            srr.setDateTime(new Date());
            srr.setRecordHandle(32222355);

        KeyValueCollection kvc = OutboundDesktopBinding.marshal(srr);

        CommonProperties cp = CommonProperties.create();
            cp.setUserData(kvc);

        RequestDistributeUserEvent rdue = RequestDistributeUserEvent.create();
            rdue.setReferenceID(322223);
            rdue.setUserEvent(cp);
            rdue.setCommunicationDN("9001");

            tsp0.request(rdue);

    }

    /**************************************************************************************************
     * Developed by Denys Diachenko, denys.diachenko@genesys.com                                      *
     * This file is part of FSinPSDK.                                                                 *
     *                                                                                                *
     * FSinPSDK. files can not be copied and/or distribute without the express                        *
     * permission of Denys Diachenko.                                                                 *
     * Last modified 2/12/19 5:01 PM.                                                                 *
     * Copyright (c) 2019. - All Rights Reserved.                                                     *
     **************************************************************************************************/

    public static class CFGSubscription {
        public static void main (String [] args) throws ConfigException, WSException, InterruptedException {
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

            NotificationQuery nq = new NotificationQuery();
                nq.setTenantDbid(101);
                ws.open();
                confService.subscribe(nq);
            //MessageHandler mh = message -> System.out.println(message);
                confService.setUserMessageHandler(mh);

        }
    }
}
