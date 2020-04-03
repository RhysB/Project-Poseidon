package com.projectposeidon.johnymuffin;

public class ConnectionPause {
    private String pluginName;
    private String connectionPauseName;
    private LoginProcessHandler loginProcessHandler;

    public ConnectionPause(String PluginName, String connectionPauseName, LoginProcessHandler loginProcessHandler) {
        this.pluginName = PluginName;
        this.connectionPauseName = connectionPauseName;
        this.loginProcessHandler = loginProcessHandler;
    }

    /**
     * This method is still undecided, please don't use it in production.
     */
    public void removeConnectionPause() {
        loginProcessHandler.removeConnectionPause(this);
    }

    public String getPluginName() {
        return this.pluginName;
    }

    public String getConnectionPauseName() {
        return this.connectionPauseName;
    }


}
