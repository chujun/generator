package com.zhongan.icare.bpm.dao;

import com.zhongan.icare.bpm.bean.qdo.ProcessPermissionQDO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessPermissionDO;
import java.util.List;

public interface ProcessPermissionDAO {
    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int insert(ProcessPermissionDO record);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int insertSelective(ProcessPermissionDO record);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    ProcessPermissionDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ProcessPermissionDO record);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ProcessPermissionDO record);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    List<ProcessPermissionDO> selectByCond(ProcessPermissionQDO cond);

    /**
     * This method corresponds to the database table act_my_proc_perm
     *
     * @mbggenerated
     */
    int countByCond(ProcessPermissionQDO cond);
}