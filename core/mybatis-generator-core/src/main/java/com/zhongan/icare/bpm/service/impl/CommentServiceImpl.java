package com.zhongan.icare.bpm.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.icare.bpm.dao.CommentDAO;
import com.zhongan.icare.bpm.dao.dataObject.CommentDO;
import com.zhongan.icare.share.bpm.dto.CommentDTO;
import com.zhongan.icare.share.bpm.service.ICommentService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
class CommentServiceImpl implements ICommentService {
    @Autowired
    CommentDAO dao;

    public static CommentDTO to(CommentDO d) {
        CommentDTO t  = new CommentDTO();
        t.setId(d.getId());
        t.setMessage(d.getMessage());
        t.setTaskId(d.getTaskId());
        t.setProcInstId(d.getProcInstId());
        t.setProcDefKey(d.getProcDefKey());
        t.setAction(d.getAction());
         if(StringUtils.isNotEmpty(d.getIsDeleted()))
        t.setIsDeleted(com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(d.getIsDeleted(),com.zhongan.health.common.share.enm.YesOrNo.class));
        t.setModifier(d.getModifier());
        t.setCreator(d.getCreator());
        t.setGmtCreated(d.getGmtCreated());
        t.setGmtModified(d.getGmtModified());
        t.setAttachmentUrlJson(d.getAttachmentUrlJson());
        return t;
    }

    public static CommentDO to(CommentDTO t) {
        CommentDO d  = new CommentDO();
        d.setId(t.getId());
        d.setMessage(t.getMessage());
        d.setTaskId(t.getTaskId());
        d.setProcInstId(t.getProcInstId());
        d.setProcDefKey(t.getProcDefKey());
        d.setAction(t.getAction());
        if (t.getIsDeleted() != null)
        d.setIsDeleted(t.getIsDeleted().getValue());
        d.setModifier(t.getModifier());
        d.setCreator(t.getCreator());
        d.setGmtCreated(t.getGmtCreated());
        d.setGmtModified(t.getGmtModified());
        d.setAttachmentUrlJson(t.getAttachmentUrlJson());
        return d;
    }

    public static List<CommentDTO> to(List<CommentDO> dataobjects) {
        if(dataobjects==null) return null;
        List<CommentDTO> dtos=Lists.newArrayListWithCapacity(dataobjects.size());
        for(CommentDO dataobject:dataobjects){dtos.add(to(dataobject));}
        return dtos;
    }

    public long create(@RequestBody CommentDTO dto) {
        Preconditions.checkArgument(dto != null,"dto不能为空.");
        CommentDO dataobject=to(dto);
        Long id=dataobject.getId();
        if(id==null)
        {
            id=SequenceFactory.nextId(CommentDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insert(dataobject);
        return id;
    }

    public int delete(@PathVariable("id") long id) {
        Preconditions.checkArgument(id>0,"Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        return cnt;
    }

    public int update(@RequestBody CommentDTO dto) {
        Preconditions.checkArgument(dto != null&&dto.getId()!=null,"Id不能为空.");
        CommentDO dataobject=to(dto);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    public List<CommentDTO> list(@RequestBody CommentDTO dto) {
        Preconditions.checkArgument(dto != null,"查询条件不能为空.");
        CommentDO dataobject=to(dto);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        List<CommentDO> dataobjects =  dao.selectByCond(dataobject);
        return to(dataobjects);
    }

    public int count(@RequestBody CommentDTO dto) {
        Preconditions.checkArgument(dto != null,"查询条件不能为空.");
        CommentDO dataobject=to(dto);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(dataobject);
        return cnt;
    }

    public CommentDTO get(@PathVariable("id") long id) {
        Preconditions.checkArgument(id >0,"id必须大于0");
        CommentDO dataobject =  dao.selectByPrimaryKey(id);
        return to(dataobject);
    }
}