package com.zhongan.icare.bpm.dao.dataObject;

import com.zhongan.health.common.share.bean.BaseDataObject;
import com.zhongan.health.common.utils.bean.annotation.Name;

@lombok.Getter
@lombok.Setter
@Name("act_my_proc_perm")
public class ProcessPermissionDO extends BaseDataObject {
    private static final long serialVersionUID = 1525853333575L;

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