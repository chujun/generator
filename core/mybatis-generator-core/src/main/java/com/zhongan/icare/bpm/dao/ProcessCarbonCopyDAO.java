package com.zhongan.icare.bpm.dao;

import com.zhongan.icare.bpm.bean.qdo.ProcessCarbonCopyQDO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessCarbonCopyDO;
import java.util.List;

public interface ProcessCarbonCopyDAO {
    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int insert(ProcessCarbonCopyDO record);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int insertSelective(ProcessCarbonCopyDO record);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    ProcessCarbonCopyDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ProcessCarbonCopyDO record);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ProcessCarbonCopyDO record);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    List<ProcessCarbonCopyDO> selectByCond(ProcessCarbonCopyQDO cond);

    /**
     * This method corresponds to the database table act_my_carbon_copy
     *
     * @mbggenerated
     */
    int countByCond(ProcessCarbonCopyQDO cond);
}