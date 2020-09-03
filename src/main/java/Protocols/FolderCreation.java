package Protocols;

import com.genesyslab.platform.applicationblocks.com.ConfServiceFactory;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgFolder;
import com.genesyslab.platform.applicationblocks.com.objects.CfgOwnerID;
import com.genesyslab.platform.applicationblocks.com.queries.CfgFolderQuery;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.MessageHandler;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.configuration.protocol.ConfServerProtocol;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.standby.WarmStandby;
import com.genesyslab.platform.standby.exceptions.WSException;

public class FolderCreation {
    public static void main (String [] args) throws InterruptedException, ProtocolException, ConfigException, WSException {

        new Logging();

        ConfServerProtocol csp = new ConfServerProtocol();
            csp.setUserName("default");
            csp.setUserPassword("password");
            csp.setClientName("default");

        Endpoint[] endpoints = new Endpoint[2];
            endpoints[0] = new Endpoint("192.168.66.188", 2020);
            endpoints[1] = new Endpoint("192.168.66.188", 2021);

        MessageHandler mh = System.out::println;
            csp.setMessageHandler(mh);

        WarmStandby ws = new WarmStandby(csp, endpoints);

        IConfService confService = ConfServiceFactory.createConfService(csp);    //	initialization of COM ABlock functionality

            ws.open();

        CfgOwnerID ownerId = null;
        Integer folderId = null; //get parent folder

        CfgFolderQuery cfgFolderQueryParent = new CfgFolderQuery(confService);
            cfgFolderQueryParent.setName("TestFolder1");//parent folder query

        CfgFolder cfgFolderParent = cfgFolderQueryParent.executeSingleResult();
            folderId = cfgFolderParent.getObjectDbid();
            ownerId = new CfgOwnerID(confService, null);
            ownerId.setDBID(cfgFolderParent.getOwnerID().getDBID());
            ownerId.setType(cfgFolderParent.getOwnerID().getType());

        CfgFolder folder = new CfgFolder(confService);//new folder creation
            folder.setName("TestFolder2");
            folder.setFolderId(folderId);
            folder.setOwnerID(ownerId);
            folder.setType(CfgObjectType.CFGScript);
            folder.save();
    }
}
