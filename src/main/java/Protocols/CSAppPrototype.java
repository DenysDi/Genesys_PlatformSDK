package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgAppPrototype;
import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectState;
import org.apache.log4j.BasicConfigurator;

public class CSAppPrototype {
    public static void main (String [] args) throws InterruptedException, ProtocolException, ConfigException {

        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());BasicConfigurator.configure();

        ConfServerProtocol csp = new ConfServerProtocol(new Endpoint("192.168.66.188", 2020));
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");

        MessageHandler mh = System.out::println;
            csp.setMessageHandler(mh);

        IConfService confService = ConfServiceFactory.createConfService(csp);    //	initialization of COM ABlock functionality

            csp.open();

        KeyValueCollection settings = new KeyValueCollection();
            settings.addObject("allow-duplicated-kvp", "true");
            settings.addObject("async-idle-alert", 86400);
            settings.addObject("async-idle-close", 3600);
            settings.addObject("async-idle-notices", "major");
            settings.addObject("attach-session-statistics", "none");
            settings.addObject("connection-down-report-scs", "true");
            settings.addObject("flex-disconnect-timeout", 45);

        KeyValueCollection log = new KeyValueCollection();
            log.addObject("all", "stdout");
            log.addObject("buffering", "false");
            log.addObject("expire", 10);
            log.addObject("messagefile", "ChatServer.lms");
            log.addObject("segment", "10MB");
            log.addObject("standard", "stdout");
            log.addObject("time-format", "ISO8601");
            log.addObject("trace", "stdout");
            log.addObject("verbose", "all");

        KeyValueCollection log_filter = new KeyValueCollection();
            log_filter.addObject("default-filter-type", "copy");
            log_filter.addObject("string-max-print-size", 128);

        KeyValueCollection options = new KeyValueCollection();
            options.addObject("settings", settings);
            options.addObject("log", log);
            options.addObject("log_filter", log_filter);

        CfgAppPrototype appP = new CfgAppPrototype(confService);
            appP.setDBID(0);
            appP.setName("PSDK_App");
            appP.setOptions(options);
            appP.setState(CfgObjectState.CFGEnabled);
            appP.setType(CfgAppType.CFGCustomServer);
            appP.setVersion("8.5.3");
            appP.save();

            csp.close();
    }
}
