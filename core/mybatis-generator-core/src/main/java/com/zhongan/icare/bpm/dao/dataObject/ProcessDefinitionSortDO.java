package com.zhongan.icare.bpm.dao.dataObject;

import com.zhongan.health.common.share.bean.BaseDataObject;
import com.zhongan.health.common.utils.bean.annotation.Name;

@lombok.Getter
@lombok.Setter
@Name("act_my_process_sort")
public class ProcessDefinitionSortDO extends BaseDataObject {
    private static final long serialVersionUID = 1516786396182L;

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
     * 是否在oa显示,默认显示,0:不显示,1:显示
     *
     * @mbggenerated
     */
    private Integer oaShow;
}