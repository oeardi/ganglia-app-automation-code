package com.entity;

import lombok.Data;

/**
 * @author 冷枫红舞
 */
@Data
public class RemoteUrlEntity {
    private String server = "http://127.0.0.1:";
    private String port = "4777";

    /**
     * https://github.com/appium/appium-inspector#important-migration-notes
     * Note The default remote server path has changed from /wd/hub to / to reflect Appium 2.0's default server path.
     * If you're using Appium Inspector with an Appium 1.x server,
     * you'll likely need to update the path information in the New Session form back to /wd/hub.
     */
    private String path = "/";  // 测试环境的 server 版本是 2.0.1

}
