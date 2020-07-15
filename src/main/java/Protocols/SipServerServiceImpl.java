package Protocols;

public class SipServerServiceImpl {/*

    protected String nameTServerPrimary;
    protected String hostTServerPrimary;
    protected int portTServerPrimary;

    protected String nameTServerBackup;
    protected String hostTServerBackup;
    protected int portTServerBackup;

    protected boolean addpSipserverUseAddp;
    protected int addpSipserverServerTimeout;
    protected int addpSipserverClientTimeout;
    protected String addpSipserverTraceMode;

    protected int warmStandbySipserverTimeout;
    protected int warmStandbySipserverAttempts;

    private TServerProtocol mainProtocol;
    private Endpoint mainEndpoint;

    private Endpoint backupEndpoint;

    private String dn;

    // WarmStandby Application Block
    private WarmStandbyService warmStandby;

    private void createProtocol(String country) {

        try {

            nameTServerPrimary = PropertiesConf.getSipServerNamePrimary(country);
            hostTServerPrimary = PropertiesConf.getSipServerHostPrimary(country);
            portTServerPrimary = Integer.parseInt(PropertiesConf.getSipServerPortPrimary(country));

            nameTServerBackup = PropertiesConf.getSipServerNameBackup(country);
            hostTServerBackup = PropertiesConf.getSipServerHostBackup(country);
            portTServerBackup = Integer.parseInt(PropertiesConf.getSipServerPortBackup(country));

            addpSipserverUseAddp = Boolean.valueOf(PropertiesConf.getAddpSipserverUseAddp(country));
            addpSipserverServerTimeout = Integer.parseInt(PropertiesConf.getAddpSipserverServerTimeout(country));
            addpSipserverClientTimeout = Integer.parseInt(PropertiesConf.getAddpSipserverClientTimeout(country));
            addpSipserverTraceMode = PropertiesConf.getAddpSipserverTraceMode(country);

            warmStandbySipserverTimeout = Integer.parseInt(PropertiesConf.getWarmStandbySipserverTimeout(country));
            warmStandbySipserverAttempts = Integer.parseInt(PropertiesConf.getWarmStandbySipserverAttempts(country));

        } catch (Exception e) {
            LOG.error("Error al cargar los datos de conexion al SIP Server: {}", e.getMessage(), e);
            throw new SipServerServiceException("IOException");
        }

        // Advanced Disconnection Detection Protocol (ADDP) configuration
        PropertyConfiguration tserverConfig = new PropertyConfiguration();
        tserverConfig.setUseAddp(addpSipserverUseAddp);
        tserverConfig.setAddpServerTimeout(addpSipserverServerTimeout);
        tserverConfig.setAddpClientTimeout(addpSipserverClientTimeout);

        tserverConfig.setAddpTraceMode(ClientADDPOptions.AddpTraceMode.parse(addpSipserverTraceMode));

        LOG.debug("ADDP configured.");

        this.mainEndpoint = new Endpoint(nameTServerPrimary, hostTServerPrimary, portTServerPrimary, tserverConfig);

        // WarmStandby Application Block
        // If you are connecting to a single server, use the WarmStandby
        // Application Block to retry
        // the first connection or to reconnect after that server has been
        // unavailable.
        // In this case, configure the WarmStandbyService to use the same
        // Endpoint as primary and backup.
        this.backupEndpoint = new Endpoint(nameTServerBackup, hostTServerBackup, portTServerBackup, tserverConfig);

        this.mainProtocol = new TServerProtocol(mainEndpoint);

        LOG.debug("TServerProtocol created.");

        WarmStandbyConfiguration warmStandbyConfig = new WarmStandbyConfiguration(mainEndpoint, backupEndpoint);
        warmStandbyConfig.setTimeout(warmStandbySipserverTimeout);
        warmStandbyConfig.setAttempts((short) warmStandbySipserverAttempts);

        LOG.debug("WarmStandbyConfiguration created.");

        warmStandby = new WarmStandbyService(mainProtocol);
        warmStandby.applyConfiguration(warmStandbyConfig);
        warmStandby.start();

        LOG.debug("WarmStandbyService created.");

    }

    private void createProtocolBackup(String country) throws SipServerServiceException {

        try {

            nameTServerPrimary = PropertiesConf.getSipServerNamePrimary(country);
            hostTServerPrimary = PropertiesConf.getSipServerHostPrimary(country);
            portTServerPrimary = Integer.parseInt(PropertiesConf.getSipServerPortPrimary(country));

            nameTServerBackup = PropertiesConf.getSipServerNameBackup(country);
            hostTServerBackup = PropertiesConf.getSipServerHostBackup(country);
            portTServerBackup = Integer.parseInt(PropertiesConf.getSipServerPortBackup(country));

            addpSipserverUseAddp = Boolean.valueOf(PropertiesConf.getAddpSipserverUseAddp(country));
            addpSipserverServerTimeout = Integer.parseInt(PropertiesConf.getAddpSipserverServerTimeout(country));
            addpSipserverClientTimeout = Integer.parseInt(PropertiesConf.getAddpSipserverClientTimeout(country));
            addpSipserverTraceMode = PropertiesConf.getAddpSipserverTraceMode(country);

            warmStandbySipserverTimeout = Integer.parseInt(PropertiesConf.getWarmStandbySipserverTimeout(country));
            warmStandbySipserverAttempts = Integer.parseInt(PropertiesConf.getWarmStandbySipserverAttempts(country));

        } catch (Exception e) {
            LOG.error("Error al cargar los datos de conexion al SIP Server: {}", e.getMessage(), e);
            throw new SipServerServiceException("IOException");
        }

        // Advanced Disconnection Detection Protocol (ADDP) configuration
        PropertyConfiguration tserverConfig = new PropertyConfiguration();
        tserverConfig.setUseAddp(addpSipserverUseAddp);
        tserverConfig.setAddpServerTimeout(addpSipserverServerTimeout);
        tserverConfig.setAddpClientTimeout(addpSipserverClientTimeout);

        tserverConfig.setAddpTraceMode(AddpTraceMode.parse(addpSipserverTraceMode));

        LOG.debug("ADDP configured.");

        this.mainEndpoint = new Endpoint(nameTServerPrimary, hostTServerPrimary, portTServerPrimary, tserverConfig);

        // WarmStandby Application Block
        // If you are connecting to a single server, use the WarmStandby
        // Application Block to retry
        // the first connection or to reconnect after that server has been
        // unavailable.
        // In this case, configure the WarmStandbyService to use the same
        // Endpoint as primary and backup.
        this.backupEndpoint = new Endpoint(nameTServerBackup, hostTServerBackup, portTServerBackup, tserverConfig);

        this.mainProtocol = new TServerProtocol(backupEndpoint);

        LOG.debug("TServerProtocol created.");

        WarmStandbyConfiguration warmStandbyConfig = new WarmStandbyConfiguration(backupEndpoint, mainEndpoint);
        warmStandbyConfig.setTimeout(warmStandbySipserverTimeout);
        warmStandbyConfig.setAttempts((short) warmStandbySipserverAttempts);

        LOG.debug("WarmStandbyConfiguration created.");

        warmStandby = new WarmStandbyService(mainProtocol);
        warmStandby.applyConfiguration(warmStandbyConfig);
        warmStandby.start();

        LOG.debug("WarmStandbyService created.");

    }

    private void connect(String country) throws SipServerServiceException {
        boolean isErrorConnectingPrimary = false;

        try {
            LOG.debug("Connecting to Primary SIP Server...");
            createProtocol(country);
            getMainProtocol().open();
            LOG.debug("Primary SIP Server connected");
        }
        catch(Exception ex){
            isErrorConnectingPrimary = true;
            LOG.warn("Error connecting to Primary SIP Server: {}", ex.getMessage(), ex);
        }

        if (isErrorConnectingPrimary){
            try{
                this.mainProtocol.close();
                warmStandby.stop();
            }
            catch(Exception ex){
                LOG.warn("Error closing Primary SIP Server: {}", ex.getMessage(), ex);
            }

            try{
                LOG.debug("Connecting to Backup SIP Server...");
                createProtocolBackup(country);
                getMainProtocol().open();
                LOG.debug("Backup SIP Server connected");

            } catch (Exception ex) {
                LOG.error("Error connecting to Backup SIP Server: {}", ex.getMessage(), ex);
                throw new SipServerServiceException("CONNECT_SERVER_ERROR");
            }
        }

    }

    public Message singleStepTransfer(String ccIdentifier, String applicationUser, String ccUserId, String thisDN,
                                      String connID, String otherDN, String country) throws SipServerServiceException {
        Message message = null;
        try {
            connect(country);
            requestRegisterAddress(thisDN);
            message = requestSingleStepTransfer(connID, thisDN, otherDN);
        } catch (Exception ex) {
            LOG.error("Error en singleStepTransfer: {}", ex.getMessage(), ex);
            new SipServerServiceException(ex.getMessage());
        } finally {
            disconnect();
        }

        return message;

    }

    private Message requestSingleStepTransfer(String connid, String thisDn, String otherDn)
            throws ProtocolException, IllegalStateException, SipServerServiceException {

        LOG.info("Transfering connid {} from thisDn={} to otherDn={}", connid, thisDn, otherDn);
        Message message = null;
        try {
            // connect();
            RequestSingleStepTransfer transfer = RequestSingleStepTransfer.create();

            transfer.setConnID(new ConnectionId(connid));
            transfer.setThisDN(thisDn);
            transfer.setOtherDN(otherDn);

            message = getMainProtocol().request(transfer);
        } catch (Exception ex) {
            LOG.error("error: ", ex.getMessage(), ex);
        } finally {
            // disconnect();
        }
        return message;

    }

    public Message muteOn(String ccIdentifier, String applicationUser, String ccUserId, String thisDN,
                          String connID, String country) throws SipServerServiceException {
        Message message = null;
        try {
            connect(country);
            requestRegisterAddress(thisDN);
            message = requestMuteOn(connID, thisDN);
        } catch (Exception ex) {
            LOG.error("Error en muteOn: {}", ex.getMessage(), ex);
            new SipServerServiceException(ex.getMessage());
        } finally {
            disconnect();
        }

        return message;

    }

    private Message requestMuteOn(String connid, String thisDn)
            throws ProtocolException, IllegalStateException, SipServerServiceException {

        LOG.info("setting mute on for connid {} on thisDn={}", connid, thisDn);
        Message message = null;
        try {
            // connect();
            RequestSetMuteOn mute = RequestSetMuteOn.create();

            mute.setConnID(new ConnectionId(connid));
            mute.setThisDN(thisDn);

            message = getMainProtocol().request(mute);
        } catch (Exception ex) {
            LOG.error("error: ", ex.getMessage(), ex);
        } finally {
            // disconnect();
        }
        return message;

    }

    public Message muteOff(String ccIdentifier, String applicationUser, String ccUserId, String thisDN,
                           String connID, String country) throws SipServerServiceException {
        Message message = null;
        try {
            connect(country);
            requestRegisterAddress(thisDN);
            message = requestMuteOff(connID, thisDN);
        } catch (Exception ex) {
            LOG.error("Error en muteOff: {}", ex.getMessage(), ex);
            new SipServerServiceException(ex.getMessage());
        } finally {
            disconnect();
        }

        return message;

    }

    private Message requestMuteOff(String connid, String thisDn)
            throws ProtocolException, IllegalStateException, SipServerServiceException {

        LOG.info("setting mute off for connid {} on thisDn={}", connid, thisDn);
        Message message = null;
        try {
            // connect();
            RequestSetMuteOff mute = RequestSetMuteOff.create();

            mute.setConnID(new ConnectionId(connid));
            mute.setThisDN(thisDn);

            message = getMainProtocol().request(mute);
        } catch (Exception ex) {
            LOG.error("error: ", ex.getMessage(), ex);
        } finally {
            // disconnect();
        }
        return message;

    }

    private void requestRegisterAddress(String dn)
            throws ProtocolException, IllegalStateException, SipServerServiceException {

        LOG.debug("Registering dn {}", dn);
        RequestRegisterAddress address = RequestRegisterAddress.create();
        address.setAddressType(AddressType.DN);
        address.setRegisterMode(RegisterMode.ModeShare);
        address.setControlMode(ControlMode.RegisterDefault);
        address.setThisDN(dn);
        getMainProtocol().request(address);
        LOG.debug("dn {} registered", dn);
    }

    private void disconnect() throws SipServerServiceException {
        try {
            LOG.debug("Disconnecting to SIP Server...");
            if (getMainProtocol() != null) {
                getMainProtocol().close();
            }
            LOG.debug("SIP Server disconnected");
        } catch (Exception e) {
            LOG.error("Error al desconectar del SIP Server: {}", e.getMessage(), e);
            throw new SipServerServiceException("DISCONNECT_SERVER_ERROR");
        }
    }

    public TServerProtocol getMainProtocol() {
        return this.mainProtocol;
    }

    public Endpoint getMainEndpoint() {
        return this.mainEndpoint;
    }

    public void setMainEndpoint(Endpoint mainEndpoint) {
        this.mainEndpoint = mainEndpoint;
    }

    public String getDn() {
        return this.dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public WarmStandbyService getWarmStandby() {
        return warmStandby;
    }

    public void setWarmStandby(WarmStandbyService warmStandby) {
        this.warmStandby = warmStandby;
    }
*/}
