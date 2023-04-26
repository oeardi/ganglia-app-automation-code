package com.entity;

import lombok.Data;

/**
 * 公共配置类
 *
 * @Author: 狄枫 Casta
 * @Date: 2023/4/24 17:54
 */
@Data
public class CommonConfigEntity {
    private Integer findElementLoopCount;
    private Integer operateElementLoopCount;
    private Integer sleepTime;

}
