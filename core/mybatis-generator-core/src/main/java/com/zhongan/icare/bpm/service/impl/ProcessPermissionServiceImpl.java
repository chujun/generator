package com.zhongan.icare.bpm.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.icare.bpm.bean.qdo.ProcessPermissionQDO;
import com.zhongan.icare.bpm.dao.ProcessPermissionDAO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessPermissionDO;
import com.zhongan.icare.share.bpm.bean.qdto.ProcessPermissionQDTO;
import com.zhongan.icare.share.bpm.dto.ProcessPermissionDTO;
import com.zhongan.icare.share.bpm.service.IProcessPermissionService;
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
class ProcessPermissionServiceImpl implements IProcessPermissionService {
    @Autowired
    ProcessPermissionDAO dao;

    @Override
    @Transactional
    public long create(@RequestBody ProcessPermissionDTO dto) {
        Preconditions.checkArgument(dto != null,"dto不能为空.");
        ProcessPermissionDO dataobject=to(dto);
        Long id=dataobject.getId();
        if(id==null)
        {
            id=SequenceFactory.nextId(ProcessPermissionDO.class);
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
    public int update(@RequestBody ProcessPermissionDTO dto) {
        Preconditions.checkArgument(dto != null&&dto.getId()!=null,"Id不能为空.");
        ProcessPermissionDO dataobject=to(dto);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    @Override
    public List<ProcessPermissionDTO> list(@RequestBody ProcessPermissionQDTO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessPermissionQDO qdo = buildProcessPermissionQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        List<ProcessPermissionDO> dataobjects =  dao.selectByCond(qdo);
        return to(dataobjects);
    }

    @Override
    public int count(@RequestBody ProcessPermissionQDTO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessPermissionQDO qdo = buildProcessPermissionQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(qdo);
        return cnt;
    }

    @Override
    public ProcessPermissionDTO get(@PathVariable("id") long id) {
        Preconditions.checkArgument(id >0,"id必须大于0");
        ProcessPermissionDO dataobject =  dao.selectByPrimaryKey(id);
        return to(dataobject);
    }

    private ProcessPermissionDTO to(ProcessPermissionDO d) {
        if(null == d){
            return null;
        }
        ProcessPermissionDTO t  = new ProcessPermissionDTO();
        t.setId(d.getId());
        t.setModelId(d.getModelId());
        t.setModelName(d.getModelName());
        t.setModelKey(d.getModelKey());
        t.setTenantId(d.getTenantId());
        t.setNoLimit(d.getNoLimit());
        t.setCompanyId(d.getCompanyId());
        t.setDepartmentId(d.getDepartmentId());
        t.setCustId(d.getCustId());
        if(StringUtils.isNotEmpty(d.getIsDeleted())){
            t.setIsDeleted(com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(d.getIsDeleted(),com.zhongan.health.common.share.enm.YesOrNo.class));
        }
        t.setCreator(d.getCreator());
        t.setModifier(d.getModifier());
        t.setGmtCreated(d.getGmtCreated());
        t.setGmtModified(d.getGmtModified());
        return t;
    }

    private ProcessPermissionDO to(ProcessPermissionDTO t) {
        ProcessPermissionDO d = new ProcessPermissionDO();
        populate(d,t);
        return d;
    }

    private void populate(ProcessPermissionDO d, ProcessPermissionDTO t) {
        if(null == d){
            d = new ProcessPermissionDO();
        }
        d.setId(t.getId());
        d.setModelId(t.getModelId());
        d.setModelName(t.getModelName());
        d.setModelKey(t.getModelKey());
        d.setTenantId(t.getTenantId());
        d.setNoLimit(t.getNoLimit());
        d.setCompanyId(t.getCompanyId());
        d.setDepartmentId(t.getDepartmentId());
        d.setCustId(t.getCustId());
        if (t.getIsDeleted() != null){
            d.setIsDeleted(t.getIsDeleted().getValue());
        }
        d.setCreator(t.getCreator());
        d.setModifier(t.getModifier());
        d.setGmtCreated(t.getGmtCreated());
        d.setGmtModified(t.getGmtModified());
    }

    private List<ProcessPermissionDTO> to(List<ProcessPermissionDO> dataobjects) {
        if(CollectionUtils.isEmpty(dataobjects)){
            return Collections.emptyList();
        }
        List<ProcessPermissionDTO> dtos=Lists.newArrayListWithCapacity(dataobjects.size());
        for(ProcessPermissionDO dataobject:dataobjects){dtos.add(to(dataobject));}
        return dtos;
    }

    private ProcessPermissionQDO buildProcessPermissionQDO(ProcessPermissionQDTO qso) {
        if(null == qso){
            return null;
        }
        ProcessPermissionQDO result = new ProcessPermissionQDO();
        populate(result,qso);
        return result;
    }
}