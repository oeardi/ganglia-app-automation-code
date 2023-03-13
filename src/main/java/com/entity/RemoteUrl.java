package com.entity;

import lombok.Data;

/**
 * @author 冷枫红舞
 */
@Data
public class RemoteUrl {
    private String server = "http://localhost:";
    private String port = "4777";
    private String path = "/wd/hub";
}
