package com.reussy.development.plugin.integration;

public interface IPluginIntegration {

    /**
     * @return true If this plugin was hooked successfully, otherwise false.
     */
    boolean isRunning();

    /**
     * @return true If the plugin is present, otherwise false.
     */
    boolean isPresent();

    /**
     * @return true If the plugin is enabled, otherwise false.
     */
    boolean isEnabled();

    /**
     * Enable and instance the plugin.
     */
    boolean enable();

    /**
     * Disable the instance of the plugin.
     */
    void disable();

}
