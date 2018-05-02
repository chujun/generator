package com.zhongan.icare.bpm.dao;

import com.zhongan.icare.bpm.bean.qdo.ExtraProcessDefinitionModelQDO;
import com.zhongan.icare.bpm.dao.dataObject.ExtraProcessDefinitionModelDO;
import java.util.List;

public interface ExtraProcessDefinitionModelDAO {
    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int insert(ExtraProcessDefinitionModelDO record);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int insertSelective(ExtraProcessDefinitionModelDO record);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    ExtraProcessDefinitionModelDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ExtraProcessDefinitionModelDO record);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ExtraProcessDefinitionModelDO record);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    List<ExtraProcessDefinitionModelDO> selectByCond(ExtraProcessDefinitionModelQDO cond);

    /**
     * This method corresponds to the database table act_my_extra_proc_def
     *
     * @mbggenerated
     */
    int countByCond(ExtraProcessDefinitionModelQDO cond);
}