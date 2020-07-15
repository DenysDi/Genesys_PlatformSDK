package Protocols;

import com.genesyslab.platform.commons.threading.AsyncInvoker;

import javax.swing.*;

public class SwingInvoker implements AsyncInvoker {
    @Override
    public void invoke(Runnable target) {
        SwingUtilities.invokeLater(target);
    }

    @Override
    public void dispose() {}
}
