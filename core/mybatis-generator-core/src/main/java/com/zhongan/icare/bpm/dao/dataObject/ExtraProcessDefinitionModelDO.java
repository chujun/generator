package com.zhongan.icare.bpm.dao.dataObject;

import com.zhongan.health.common.share.bean.BaseDataObject;
import com.zhongan.health.common.utils.bean.annotation.Name;

@lombok.Getter
@lombok.Setter
@Name("act_my_extra_proc_def")
public class ExtraProcessDefinitionModelDO extends BaseDataObject {
    private static final long serialVersionUID = 1525246576539L;

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