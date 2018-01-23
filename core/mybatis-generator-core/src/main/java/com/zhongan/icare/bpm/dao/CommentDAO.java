package com.zhongan.icare.bpm.dao;

import com.zhongan.icare.bpm.dao.dataObject.CommentDO;
import java.util.List;

public interface CommentDAO {
    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int insert(CommentDO record);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int insertSelective(CommentDO record);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    CommentDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CommentDO record);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CommentDO record);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    List<CommentDO> selectByCond(CommentDO cond);

    /**
     * This method corresponds to the database table act_wo_comment
     *
     * @mbggenerated
     */
    int countByCond(CommentDO cond);
}