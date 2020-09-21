package Protocols;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.Log4JLoggerFactoryImpl;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Logging {

    public static final Logger logger = LogManager.getLogger(Logging.class);

    public Logging() {
        PsdkCustomization.setOption(PsdkCustomization.PsdkOption.PsdkLoggerTraceMessages, null, "true");
        Log.setLoggerFactory(new Log4JLoggerFactoryImpl());
        BasicConfigurator.configure();
    }
}
