package com.zhongan.icare.share.bpm.dto;

import com.zhongan.health.common.share.bean.BaseDTO;

@lombok.Getter
@lombok.Setter
public class ExtraProcessDefinitionModelDTO extends BaseDTO {
    private static final long serialVersionUID = 1525247856929L;

    /**
     * 流程定义模型Id
     *
     * @mbggenerated
     */
    private String modelId;

    /**
     * 图标url
     *
     * @mbggenerated
     */
    private String iconUrl;
}