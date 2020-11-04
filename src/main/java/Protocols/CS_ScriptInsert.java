package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgScript;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.genesyslab.platform.configuration.protocol.types.CfgScriptType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import static Protocols.Logging.logger;

public class CS_ScriptInsert {

    public static void main (String [] args) throws ConfigException, WSException, InterruptedException {

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
        IConfService service = ConfServiceFactory.createConfService(csp);
        MessageHandler mh1 = System.out::println;
            service.setUserMessageHandler(mh1);
            ws.open();
        logger.info("\n\n---> Opening connection... <---\n\n");

        String scriptName = "TestScript_3false";

        logger.info("Script " + scriptName + " does not exists. Creating a new script.");

        CfgScript newScript = new CfgScript(service);
            newScript.setName(scriptName);
            newScript.setTenantDBID(1); // DBID for Environment is 1 always.
            newScript.setType(CfgScriptType.CFGEnhancedRouting);
            newScript.setFolderId(114);

        // Create "Options" sections:
        KeyValueCollection sectionsList;
        KeyValueCollection sectionOptions;
            sectionsList = new KeyValueCollection();
            sectionOptions = new KeyValueCollection();

        String urlWithParam = "uri" + "?" + "uriParameters";
        String blueUrl = "blueUri" + "_blue/src_gen/";
        String greenUrl = "greenUri" + "_green/src_gen/";

        logger.info("URI : " + urlWithParam);
        logger.info("blueUrl : " + blueUrl);
        logger.info("greenUrl : " + greenUrl);

            sectionOptions.addString("url", urlWithParam);

            boolean bool = false;

        if(bool) {
            sectionOptions.addString("alternate-url", greenUrl);
        } else {
            sectionOptions.addString("alternate-url", blueUrl);
        }

            sectionsList.addList("Application", sectionOptions);
            sectionOptions = new KeyValueCollection();
            //sectionsList.addList("ApplicationParms", sectionOptions);scxml-doc-request-headers

        if(bool) {
            sectionOptions.addString("activeApplication", "greenUrl");
        } else {
            sectionOptions.addString("activeApplication", "blueUrl");
        }

            sectionOptions.addString("blueURL", blueUrl);
            sectionOptions.addString("greenURL", greenUrl);
            sectionOptions.addString("lobName", "lobName");
            sectionsList.addList("scxml-doc-request-headers", sectionOptions);

            newScript.setUserProperties(sectionsList);
            newScript.save();

    }
}
