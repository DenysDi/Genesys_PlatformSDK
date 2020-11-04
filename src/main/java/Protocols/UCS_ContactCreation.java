package Protocols;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocol;
import com.genesyslab.platform.contacts.protocol.contactserver.AttributesList;
import com.genesyslab.platform.contacts.protocol.contactserver.requests.RequestInsert;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import static Protocols.Logging.logger;

public class UCS_ContactCreation {

    public static void main (String []args){
        new Logging();

        UniversalContactServerProtocol ucsp = new UniversalContactServerProtocol();
            ucsp.setClientName("PSDK");

        MessageHandler mh = System.out::println;
            ucsp.setMessageHandler(mh);

        WSConfig wscfg = new WSConfig();
            wscfg.setBackupDelay(2000);
            wscfg.setEndpoints(new Endpoint("DD-WIN12R2-ES", 7009));
            wscfg.setReconnectionRandomDelayRange(3000);
            wscfg.setRetryDelay(1000, 2000);
            wscfg.setTimeout(1000);

        WarmStandby ws = new WarmStandby(ucsp, wscfg.getEndpoints());
            ws.setConfig(wscfg);

            try{
                ws.open();
            } catch (InterruptedException | WSException e) {
                logger.error(e);
            }

        KeyValueCollection kvc = new KeyValueCollection();
            kvc.addObject("Name", "Mark");
            kvc.addObject("Phone", "3222235588");

        AttributesList name = new AttributesList();
            name.add(name.get(1));

        /*ContactAttributeList cal = new ContactAttributeList();
            cal.add(1,kvc);
            cal.add(2,phone);

        Contact contact = new Contact();
            contact.setContactAttributesList(cal);*/

        RequestInsert ri = new RequestInsert();
            ri.setTenantId(1);
            ri.setAttributes(name);

        try {
            ucsp.request(ri);
        } catch (ProtocolException e) {
            logger.error(e);
        }
    }
}
