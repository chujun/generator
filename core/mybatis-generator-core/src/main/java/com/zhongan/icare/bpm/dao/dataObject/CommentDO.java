package com.zhongan.icare.bpm.dao.dataObject;

import com.zhongan.health.common.share.bean.BaseDataObject;

@lombok.Getter
@lombok.Setter
public class CommentDO extends BaseDataObject {
    private static final long serialVersionUID = 1516693062539L;

    /**
     * 评论信息
     *
     * @mbggenerated
     */
    private String message;

    /**
     * 任务id
     *
     * @mbggenerated
     */
    private String taskId;

    /**
     * 流程实例id
     *
     * @mbggenerated
     */
    private String procInstId;

    /**
     * 流程定义key
     *
     * @mbggenerated
     */
    private String procDefKey;

    /**
     * 业务动作，例如通过，驳回,commit,reject
     *
     * @mbggenerated
     */
    private String action;

    /**
     * 附件urljson
     *
     * @mbggenerated
     */
    private String attachmentUrlJson;
}