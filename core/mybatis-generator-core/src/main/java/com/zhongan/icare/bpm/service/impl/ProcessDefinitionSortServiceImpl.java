package com.zhongan.icare.bpm.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.icare.bpm.bean.qdo.ProcessDefinitionSortQDO;
import com.zhongan.icare.bpm.dao.ProcessDefinitionSortDAO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessDefinitionSortDO;
import com.zhongan.icare.share.bpm.bean.qso.ProcessDefinitionSortQSO;
import com.zhongan.icare.share.bpm.dto.ProcessDefinitionSortDTO;
import com.zhongan.icare.share.bpm.service.IProcessDefinitionSortService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
class ProcessDefinitionSortServiceImpl implements IProcessDefinitionSortService {
    @Autowired
    ProcessDefinitionSortDAO dao;

    @Override
    public long create(@RequestBody ProcessDefinitionSortDTO dto) {
        Preconditions.checkArgument(dto != null,"dto不能为空.");
        ProcessDefinitionSortDO dataobject=to(dto);
        Long id=dataobject.getId();
        if(id==null)
        {
            id=SequenceFactory.nextId(ProcessDefinitionSortDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insertSelective(dataobject);
        return id;
    }

    @Override
    public int delete(@PathVariable("id") long id) {
        Preconditions.checkArgument(id>0,"Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        return cnt;
    }

    @Override
    public int update(@RequestBody ProcessDefinitionSortDTO dto) {
        Preconditions.checkArgument(dto != null&&dto.getId()!=null,"Id不能为空.");
        ProcessDefinitionSortDO dataobject=to(dto);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    @Override
    public List<ProcessDefinitionSortDTO> list(@RequestBody ProcessDefinitionSortQSO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessDefinitionSortQDO qdo = buildProcessDefinitionSortQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        List<ProcessDefinitionSortDO> dataobjects =  dao.selectByCond(qdo);
        return to(dataobjects);
    }

    @Override
    public int count(@RequestBody ProcessDefinitionSortQSO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessDefinitionSortQDO qdo = buildProcessDefinitionSortQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(qdo);
        return cnt;
    }

    @Override
    public ProcessDefinitionSortDTO get(@PathVariable("id") long id) {
        Preconditions.checkArgument(id >0,"id必须大于0");
        ProcessDefinitionSortDO dataobject =  dao.selectByPrimaryKey(id);
        return to(dataobject);
    }

    private ProcessDefinitionSortDTO to(ProcessDefinitionSortDO d) {
        ProcessDefinitionSortDTO t  = new ProcessDefinitionSortDTO();
        t.setId(d.getId());
        t.setName(d.getName());
        t.setParentSortId(d.getParentSortId());
        t.setLevel(d.getLevel());
        t.setTenantId(d.getTenantId());
        t.setDescription(d.getDescription());
        t.setOaShow(d.getOaShow());
        if(StringUtils.isNotEmpty(d.getIsDeleted())){
            t.setIsDeleted(com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(d.getIsDeleted(),com.zhongan.health.common.share.enm.YesOrNo.class));
        }
        t.setModifier(d.getModifier());
        t.setCreator(d.getCreator());
        t.setGmtCreated(d.getGmtCreated());
        t.setGmtModified(d.getGmtModified());
        return t;
    }

    private ProcessDefinitionSortDO to(ProcessDefinitionSortDTO t) {
        ProcessDefinitionSortDO d = new ProcessDefinitionSortDO();
        populate(d,t);
        return d;
    }

    private void populate(ProcessDefinitionSortDO d, ProcessDefinitionSortDTO t) {
        if(null == d){
            d = new ProcessDefinitionSortDO();
        }
        d.setId(t.getId());
        d.setName(t.getName());
        d.setParentSortId(t.getParentSortId());
        d.setLevel(t.getLevel());
        d.setTenantId(t.getTenantId());
        d.setDescription(t.getDescription());
        d.setOaShow(t.getOaShow());
        if (t.getIsDeleted() != null){
            d.setIsDeleted(t.getIsDeleted().getValue());
        }
        d.setModifier(t.getModifier());
        d.setCreator(t.getCreator());
        d.setGmtCreated(t.getGmtCreated());
        d.setGmtModified(t.getGmtModified());
    }

    private List<ProcessDefinitionSortDTO> to(List<ProcessDefinitionSortDO> dataobjects) {
        if(null == dataobjects){
            return null;
        }
        List<ProcessDefinitionSortDTO> dtos=Lists.newArrayListWithCapacity(dataobjects.size());
        for(ProcessDefinitionSortDO dataobject:dataobjects){dtos.add(to(dataobject));}
        return dtos;
    }

    private ProcessDefinitionSortQDO buildProcessDefinitionSortQDO(ProcessDefinitionSortQSO qso) {
        if(null == qso){
            return null;
        }
        ProcessDefinitionSortQDO result = new ProcessDefinitionSortQDO();
        populate(result,qso);
        return result;
    }
}