package com.zhongan.icare.bpm.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.icare.bpm.bean.qdo.ExtraProcessDefinitionModelQDO;
import com.zhongan.icare.bpm.dao.ExtraProcessDefinitionModelDAO;
import com.zhongan.icare.bpm.dao.dataObject.ExtraProcessDefinitionModelDO;
import com.zhongan.icare.share.bpm.bean.qdto.ExtraProcessDefinitionModelQDTO;
import com.zhongan.icare.share.bpm.dto.ExtraProcessDefinitionModelDTO;
import com.zhongan.icare.share.bpm.service.IExtraProcessDefinitionModelService;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
class ExtraProcessDefinitionModelServiceImpl implements IExtraProcessDefinitionModelService {
    @Autowired
    ExtraProcessDefinitionModelDAO dao;

    @Override
    @Transactional
    public long create(@RequestBody ExtraProcessDefinitionModelDTO dto) {
        Preconditions.checkArgument(dto != null,"dto不能为空.");
        ExtraProcessDefinitionModelDO dataobject=to(dto);
        Long id=dataobject.getId();
        if(id==null)
        {
            id=SequenceFactory.nextId(ExtraProcessDefinitionModelDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insertSelective(dataobject);
        return id;
    }

    @Override
    @Transactional
    public int delete(@PathVariable("id") long id) {
        Preconditions.checkArgument(id>0,"Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        return cnt;
    }

    @Override
    @Transactional
    public int update(@RequestBody ExtraProcessDefinitionModelDTO dto) {
        Preconditions.checkArgument(dto != null&&dto.getId()!=null,"Id不能为空.");
        ExtraProcessDefinitionModelDO dataobject=to(dto);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    @Override
    public List<ExtraProcessDefinitionModelDTO> list(@RequestBody ExtraProcessDefinitionModelQDTO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ExtraProcessDefinitionModelQDO qdo = buildExtraProcessDefinitionModelQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        List<ExtraProcessDefinitionModelDO> dataobjects =  dao.selectByCond(qdo);
        return to(dataobjects);
    }

    @Override
    public int count(@RequestBody ExtraProcessDefinitionModelQDTO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ExtraProcessDefinitionModelQDO qdo = buildExtraProcessDefinitionModelQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(qdo);
        return cnt;
    }

    @Override
    public ExtraProcessDefinitionModelDTO get(@PathVariable("id") long id) {
        Preconditions.checkArgument(id >0,"id必须大于0");
        ExtraProcessDefinitionModelDO dataobject =  dao.selectByPrimaryKey(id);
        return to(dataobject);
    }

    private ExtraProcessDefinitionModelDTO to(ExtraProcessDefinitionModelDO d) {
        if(null == d){
            return null;
        }
        ExtraProcessDefinitionModelDTO t  = new ExtraProcessDefinitionModelDTO();
        t.setId(d.getId());
        t.setModelId(d.getModelId());
        t.setIconUrl(d.getIconUrl());
        if(StringUtils.isNotEmpty(d.getIsDeleted())){
            t.setIsDeleted(com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(d.getIsDeleted(),com.zhongan.health.common.share.enm.YesOrNo.class));
        }
        t.setCreator(d.getCreator());
        t.setModifier(d.getModifier());
        t.setGmtCreated(d.getGmtCreated());
        t.setGmtModified(d.getGmtModified());
        return t;
    }

    private ExtraProcessDefinitionModelDO to(ExtraProcessDefinitionModelDTO t) {
        ExtraProcessDefinitionModelDO d = new ExtraProcessDefinitionModelDO();
        populate(d,t);
        return d;
    }

    private void populate(ExtraProcessDefinitionModelDO d, ExtraProcessDefinitionModelDTO t) {
        if(null == d){
            d = new ExtraProcessDefinitionModelDO();
        }
        d.setId(t.getId());
        d.setModelId(t.getModelId());
        d.setIconUrl(t.getIconUrl());
        if (t.getIsDeleted() != null){
            d.setIsDeleted(t.getIsDeleted().getValue());
        }
        d.setCreator(t.getCreator());
        d.setModifier(t.getModifier());
        d.setGmtCreated(t.getGmtCreated());
        d.setGmtModified(t.getGmtModified());
    }

    private List<ExtraProcessDefinitionModelDTO> to(List<ExtraProcessDefinitionModelDO> dataobjects) {
        if(CollectionUtils.isEmpty(dataobjects)){
            return Collections.emptyList();
        }
        List<ExtraProcessDefinitionModelDTO> dtos=Lists.newArrayListWithCapacity(dataobjects.size());
        for(ExtraProcessDefinitionModelDO dataobject:dataobjects){dtos.add(to(dataobject));}
        return dtos;
    }

    private ExtraProcessDefinitionModelQDO buildExtraProcessDefinitionModelQDO(ExtraProcessDefinitionModelQDTO qso) {
        if(null == qso){
            return null;
        }
        ExtraProcessDefinitionModelQDO result = new ExtraProcessDefinitionModelQDO();
        populate(result,qso);
        return result;
    }
}