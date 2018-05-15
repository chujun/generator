package com.zhongan.icare.share.bpm.dto;

import com.zhongan.health.common.share.bean.BaseDTO;

@lombok.Getter
@lombok.Setter
public class ProcessPermissionDTO extends BaseDTO {
    private static final long serialVersionUID = 1525853333629L;

    /**
     * 流程定义模型Id
     *
     * @mbggenerated
     */
    private String modelId;

    /**
     * 流程定义模型名称
     *
     * @mbggenerated
     */
    private String modelName;

    /**
     * 流程定义模型key
     *
     * @mbggenerated
     */
    private String modelKey;

    /**
     * 所属公司id
     *
     * @mbggenerated
     */
    private String tenantId;

    /**
     * 是否无限制,0:有限制,1:无限制
     *
     * @mbggenerated
     */
    private Integer noLimit;

    /**
     * 公司id
     *
     * @mbggenerated
     */
    private String companyId;

    /**
     * 部门id
     *
     * @mbggenerated
     */
    private String departmentId;

    /**
     * 用户id
     *
     * @mbggenerated
     */
    private String custId;
}