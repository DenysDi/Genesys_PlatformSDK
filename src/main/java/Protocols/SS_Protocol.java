package Protocols;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.*;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestGetStatisticEx;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SS_Protocol {
    public static void main(String[] args) {
        StatServerProtocol ssp;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date;
        try {
            StatServerProtocol StatServer = new StatServerProtocol(new Endpoint(new URI("tcp://WIN-F3JRIRUVQ4F:7010")));
                StatServer.setClientName("default");
                StatServer.open();

            RequestGetStatisticEx request = RequestGetStatisticEx.create();
            StatisticObject object = StatisticObject.create();
                object.setObjectId("Place1001");
                object.setObjectType(StatisticObjectType.RegularDN);
                object.setTenantName("Environment");
                object.setTenantPassword("");

                request.setStatisticObject(object);
            //ActionM

            DnActionMask mainMask = ActionsMask.createDNActionsMask();
                mainMask.setBit(DnActions.WaitForNextCall);
                mainMask.setBit(DnActions.CallDialing);
                mainMask.setBit(DnActions.CallRinging);
                mainMask.setBit(DnActions.NotReadyForNextCall);
                mainMask.setBit(DnActions.CallOnHold);
                mainMask.setBit(DnActions.CallUnknown);
                mainMask.setBit(DnActions.CallConsult);
                mainMask.setBit(DnActions.CallInternal);
                mainMask.setBit(DnActions.CallOutbound);
                mainMask.setBit(DnActions.CallInbound);
                mainMask.setBit(DnActions.LoggedOut);

            DnActionMask relMask = ActionsMask.createDNActionsMask();
            StatisticMetricEx metric = StatisticMetricEx.create();
                metric.setCategory(StatisticCategory.CurrentState);
                metric.setMainMask(mainMask);
                metric.setRelativeMask(relMask);
                metric.setSubject(StatisticSubject.DNStatus);

                request.setStatisticMetricEx(metric);
                request.setReferenceId(12);

            Message response = StatServer.request(request);
            System.out.println(response);

            //Message response = ssp.receive          ();
            //System.out.println                      (response);
            StatServer.close();

 			/*ActionsMask mainMask = ActionsMask.createRPActionsMask();
			//  mainMask.setBit(DnActions.NotReadyForNextCall);
			mainMask.setBit(RoutePointActions.AgentReady);

			//  mainMask.setBit(RoutePointActions.CallReleased);
			//  mainMask.setBit(RoutePointActions.CallMissed);
			//  mainMask.setBit(RoutePointActions.ACWCompleted);
			//  mainMask.setBit(RoutePointActions.AgentReady);

				mainMask.setBit(DnActions.CallObservedUnknown);
				mainMask.setBit(DnActions.CallObservedInbound);
				mainMask.setBit(DnActions.CallObservedOutbound);
				mainMask.setBit(DnActions.CallObservedConsult);
				mainMask.setBit(DnActions.CallObservedInternal);

			RequestGetStatisticEx req = RequestGetStatisticEx.create();
			StatisticMetricEx stmetric = StatisticMetricEx.create();
			stmetric.setCategory(StatisticCategory.CurrentNumber);
			stmetric.setSubject(StatisticSubject.DNAction);
			stmetric.setMainMask(mainMask);
			stmetric.setRelativeMask(ActionsMask.createDNActionsMask());

			//	stmetric.setSubject(StatisticSubject.AgentStatus);
			//	stmetric.setCategory(StatisticCategory.CurrentState);

			stmetric.setTimeProfile("Default");
			req.setStatisticMetricEx(stmetric);

			RequestOpenStatisticEx req1 = RequestOpenStatisticEx.create();

			StatisticObject statobj = StatisticObject.create();
			statobj.setObjectId("1001@MediaSwitch"); //Employee ID of agent
			statobj.setObjectType(StatisticObjectType.Queue);
			statobj.setTenantName("Environment");
			statobj.setTenantPassword("");

				req.StatisticObject = StatisticObject.Create();
				req.StatisticObject.ObjectId = "6001@KievSwitch";
				req.StatisticObject.ObjectType = StatisticObjectType.RegularDN;
				req.StatisticObject.TenantName = "Resources";
				req.StatisticObject.TenantPassword = "";

			StatisticMetricEx metric = StatisticMetricEx.create();
			metric.setFilter("chat");
			metric.setIntervalType(StatisticInterval.GrowingWindow);
			metric.setTimeProfile("Default");
			metric.setCategory(StatisticCategory.CurrentNumber);

			metric.setSubject(StatisticSubject.DNAction);
			metric.setMainMask(mainMask);
			metric.setRelativeMask(ActionsMask.createDNActionsMask());

			try {
				// req1.ReferenceId = 5052;
				req1.setStatisticObject(statobj);
				Notification not = Notification.create();
				not.setMode(NotificationMode.Immediate);
				req1.setNotification(not);
				req1.setStatisticMetricEx(metric);
			} catch (Exception e) {}

			// req.ReferenceId = 1236;
			// StatServer.Send(req1);
			Message response9 = StatServer.request(req1);
			EventStatisticOpened res6;

			try {
				res6 = ((EventStatisticOpened) response9);
				date = new Date();
				System.out.println("Statisctic value is" + res6.toString() + "\n time is " + dateFormat.format(date));
			} catch (Exception e) {
				System.out.println("Statistic failed to open" + "\n time is " + dateFormat.format(new Date()));
			}

			StatServer.close();

		*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
