package com.zhongan.icare.bpm.dao;

import com.zhongan.icare.bpm.bean.qdo.ProcessDefinitionSortQDO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessDefinitionSortDO;
import java.util.List;

public interface ProcessDefinitionSortDAO {
    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int insert(ProcessDefinitionSortDO record);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int insertSelective(ProcessDefinitionSortDO record);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    ProcessDefinitionSortDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ProcessDefinitionSortDO record);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ProcessDefinitionSortDO record);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    List<ProcessDefinitionSortDO> selectByCond(ProcessDefinitionSortQDO cond);

    /**
     * This method corresponds to the database table act_my_process_sort
     *
     * @mbggenerated
     */
    int countByCond(ProcessDefinitionSortQDO cond);
}