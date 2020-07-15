package Protocols;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.contacts.protocol.UniversalContactServerProtocol;
import com.genesyslab.platform.contacts.protocol.contactserver.Operators;
import com.genesyslab.platform.contacts.protocol.contactserver.SearchCriteriaCollection;
import com.genesyslab.platform.contacts.protocol.contactserver.SimpleSearchCriteria;
import com.genesyslab.platform.contacts.protocol.contactserver.requests.RequestInteractionListGet;
import com.genesyslab.platform.contacts.protocol.contactserver.requests.RequestInteractionListGetNextPage;

import java.net.URI;
import java.net.URISyntaxException;

public class UCSProtocol {
    public static void main(String[] args) throws InterruptedException, ProtocolException, URISyntaxException {
        UniversalContactServerProtocol ucs = new UniversalContactServerProtocol(new Endpoint(new URI("tcp://DD-WIN12R2-ES:7009")));
            ucs.setClientName               ("PSDK");
            ucs.open();

        SimpleSearchCriteria Thread = new SimpleSearchCriteria();
            Thread.setAttrName              ("StartDate");
            Thread.setAttrValue             ("2018-01-01T00:00:00.000Z");
            Thread.setOperator              (Operators.Greater);

        SimpleSearchCriteria Thread1 = new SimpleSearchCriteria();
            Thread1.setAttrName                 ("StartDate");
            Thread1.setAttrValue                ("2019-12-12T00:00:00.000Z");
            Thread1.setOperator                 (Operators.Lesser);

        SearchCriteriaCollection sc = new SearchCriteriaCollection();
            sc.add                          (Thread);
            sc.add                          (Thread1);

        /*InteractionAttributes IA = new InteractionAttributes();
            IA.setId                        ("00010aDST1PD000N");

        RequestUpdateInteraction rup = new RequestUpdateInteraction();
            rup.setInteractionAttributes(IA);

        RequestDeleteInteraction rdel = new RequestDeleteInteraction();         //Can delete interactions from main UCS DB only
            rdel.setInteractionId           ("00010aDST1PD000N");*/

        RequestInteractionListGet RILG = new RequestInteractionListGet();
            RILG.setTenantId                (1);
            RILG.setPageMaxSize             (2);
            RILG.setSearchCriteria          (sc);
            RILG.setReferenceId(322223);
            //RILG.setDataSource              (DataSourceType.Archive);


        /*RequestInteractionListGet r1 = new RequestInteractionListGet();
        r1.setDataSource                (DataSourceType.Archive);
        r1.set;

        RequestGetInteractionContent rgic = new RequestGetInteractionContent();
        rgic.setDataSource              (DataSourceType.Archive);
        rgic.setIncludeBinaryContent    (true);
        rgic.setIncludeAttachments      (true);
        rgic.setInteractionId           ("00005aDQ3AH1000K");*/

            ucs.send                      (RILG);
            //ucs.send                            (rdel);
        Message resp = ucs.request    (RILG);
        //Message resp1 = ucs.request         (rdel);
        System.out.println            ("1st resp \n" + resp);
        //System.out.println                  (resp1);

        java.lang.Thread.sleep(1000);
        RequestInteractionListGetNextPage nextPage1 = new RequestInteractionListGetNextPage();
            nextPage1.setReferenceId(322223);
            nextPage1.setScrollId("46");

            ucs.send(nextPage1);

        Message resp1 = ucs.request    (nextPage1);
        System.out.println            ("2nd resp \n" + resp1);



        java.lang.Thread.sleep(1000);
        ucs.send(nextPage1);

        Message resp2 = ucs.request    (nextPage1);
        System.out.println            ("3rd resp \n" + resp2);

        /*RequestInteractionListGetNextPage nextPage2 = new RequestInteractionListGetNextPage();
            nextPage2.setReferenceId(322223);
            nextPage2.setScrollId("44");

            ucs.send(nextPage2);

        Message resp2 = ucs.request    (nextPage2);
        System.out.println            ("3rd resp \n" + resp2);*/

        ucs.close();
    }
}
