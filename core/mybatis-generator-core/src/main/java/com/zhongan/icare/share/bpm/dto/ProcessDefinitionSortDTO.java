package com.zhongan.icare.share.bpm.dto;

import com.zhongan.health.common.share.bean.BaseDTO;

@lombok.Getter
@lombok.Setter
public class ProcessDefinitionSortDTO extends BaseDTO {
    private static final long serialVersionUID = 1516708234126L;

    /**
     * 分类名称
     *
     * @mbggenerated
     */
    private String name;

    /**
     * 父级分类ID
     *
     * @mbggenerated
     */
    private Long parentSortId;

    /**
     * 分类级别,从0开始,1,2递增
     *
     * @mbggenerated
     */
    private Integer level;

    /**
     * 公司id
     *
     * @mbggenerated
     */
    private String tenantId;

    /**
     * 描述
     *
     * @mbggenerated
     */
    private String description;

    /**
     * 是否在oa显示
     *
     * @mbggenerated
     */
    private Integer oaShow;
}