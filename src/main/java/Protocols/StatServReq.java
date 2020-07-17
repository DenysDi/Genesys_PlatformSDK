package Protocols;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.*;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatisticEx;

import static Protocols.Logging.logger;

public class StatServReq {
    public static void main(String []args) throws ProtocolException {

        new Logging();

        StatServerProtocol ssp = new StatServerProtocol(new Endpoint("192.168.66.99",7006));
            ssp.setClientName("PSDKStatTest001");

        MessageHandler ssMessageHandler = new MessageHandler() {
            @Override
            public void onMessage(Message message) {
                logger.debug("*****************\n" + "--->Received via MH:\n" + message + "\n<---\n");
            }
        };
        ssp.setMessageHandler(ssMessageHandler);

        try {
            ssp.open();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        StatisticObject object = StatisticObject.create();
            object.setObjectType(StatisticObjectType.GroupQueues);
            object.setTenantName("Environment");
            object.setObjectId("DN Group");
            object.setTenantPassword("");

        Notification notification = Notification.create();
            notification.setMode(NotificationMode.Periodical);
            notification.setInsensitivity(5);
            notification.setFrequency(10);

        DnActionMask mainMask = ActionsMask.createDNActionsMask();
            mainMask.setBit(DnActions.CallAnswered);

        /*RoutePointActionsMask mainMask = RoutePointActionsMask.createRPActionsMask();
            mainMask.setBit(RoutePointActions.CallAnswered);*/

        DnActionMask relMask = ActionsMask.createDNActionsMask();

        StatisticMetricEx metricEx = StatisticMetricEx.create();
            metricEx.setMainMask(mainMask);
            metricEx.setRelativeMask(mainMask);
            metricEx.setIntervalType(StatisticInterval.GrowingWindow);
            metricEx.setTimeProfile("900:900"); //not sure for this value
            metricEx.setDistinguishByConnId(true);
            metricEx.setSubject(StatisticSubject.DNStatus);
            metricEx.setCategory(StatisticCategory.TotalAdjustedTime);

            KeyValueCollection extension = new KeyValueCollection();
            extension.addObject("MediaType", "chat");

        RequestOpenStatisticEx request = RequestOpenStatisticEx.create();
            request.setReferenceId(322223);
            request.setStatisticObject(object);
            request.setStatisticMetricEx(metricEx);
            request.setNotification(notification);
            request.setExtensions(extension);

        logger.info("--->Sending:\n" + request + "\n<---\n");
        Message response = ssp.request(request);
        logger.info("\n*****************\n" + "\n--->\nReceived in response:\n" + response + "\n<---\n");

        try {
            ssp.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
