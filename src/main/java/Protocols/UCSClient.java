package Protocols;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocol;
import com.genesyslab.platform.standby.WSConfig;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Protocols.Logging.logger;

public class UCSClient {
    public static void main (String []args){

        new Logging();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date;
        Date date1;
        Date date2;

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
        try {
            date = new Date();
            logger.info("\n--->\nStart testing\nconnection should be opened in a sec\n" + dateFormat.format(date) + "\n<---\n");
            ws.open();
            if (ws.isOpened()){
                date1 = new Date();
                logger.info("\n--->\nStart testing\nconnection is opened in\n" + dateFormat.format(date1) + "\n<---\n");
            } else {
                date2 = new Date();
                logger.info("\n--->\nStart testing\nconnection is not opened\n" + dateFormat.format(date2) + "\n<---\n");
            }
        } catch (InterruptedException | WSException e) {
            logger.error(e);
        }
    }
}
