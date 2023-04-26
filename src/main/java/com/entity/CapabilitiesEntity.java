package com.entity;

import lombok.Data;

/**
 * @author 冷枫红舞
 */
@Data
public class CapabilitiesEntity {
    private String deviceName;
    private String platformName;
    private String appPackage;
    private String appActivity;
    private String automationName;
    private String platformVersion;
    private String newCommandTimeout;
    private String language;
    private Boolean autoLaunch;
    private Boolean autoWebview;
    private Boolean noReset;
    private Boolean fullReset;
    private Boolean dontStopAppOnReset;
    private Boolean skipDeviceInitialization;
    private Boolean skipServerInstallation;
    private Boolean unicodeKeyboard;
    private Boolean resetKeyboard;
}
