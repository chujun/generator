package com.zhongan.icare.share.bpm.dto;

import com.zhongan.health.common.share.bean.BaseDTO;

@lombok.Getter
@lombok.Setter
public class CommentDTO extends BaseDTO {
    private static final long serialVersionUID = 1516692364261L;

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