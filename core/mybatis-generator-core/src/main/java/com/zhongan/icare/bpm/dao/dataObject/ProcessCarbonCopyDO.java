package com.zhongan.icare.bpm.dao.dataObject;

import com.zhongan.health.common.share.bean.BaseDataObject;
import com.zhongan.health.common.utils.bean.annotation.Name;
import java.util.Date;

@lombok.Getter
@lombok.Setter
@Name("act_my_carbon_copy")
public class ProcessCarbonCopyDO extends BaseDataObject {
    private static final long serialVersionUID = 1517369267073L;

    /**
     * 抄送状态,0:未读,1:已读
     *
     * @mbggenerated
     */
    private Integer state;

    /**
     * 流程实例id
     *
     * @mbggenerated
     */
    private String procInstId;

    /**
     * 业务key
     *
     * @mbggenerated
     */
    private String businessKey;

    /**
     * 抄送人ID
     *
     * @mbggenerated
     */
    private String custId;

    /**
     * 抄送人名称
     *
     * @mbggenerated
     */
    private String custName;

    /**
     * 流程所在公司ID
     *
     * @mbggenerated
     */
    private String tenantId;

    /**
     * 流程定义ID
     *
     * @mbggenerated
     */
    private String procDefId;

    /**
     * 流程定义key
     *
     * @mbggenerated
     */
    private String procDefKey;

    /**
     * 流程定义名称
     *
     * @mbggenerated
     */
    private String procDefName;

    /**
     * 流程定义分类ID
     *
     * @mbggenerated
     */
    private String sortId;

    /**
     * 发起人ID
     *
     * @mbggenerated
     */
    private String startCustId;

    /**
     * 发起人名称
     *
     * @mbggenerated
     */
    private String startCustName;

    /**
     * 流程开始时间
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * 流程结束时间
     *
     * @mbggenerated
     */
    private Date endTime;

    /**
     * 流程是否结束,0:未结束,1:结束
     *
     * @mbggenerated
     */
    private Integer procIsEnd;
}