package com.zhongan.icare.bpm.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.icare.bpm.bean.qdo.ProcessCarbonCopyQDO;
import com.zhongan.icare.bpm.dao.ProcessCarbonCopyDAO;
import com.zhongan.icare.bpm.dao.dataObject.ProcessCarbonCopyDO;
import com.zhongan.icare.share.bpm.bean.qso.ProcessCarbonCopyQSO;
import com.zhongan.icare.share.bpm.dto.ProcessCarbonCopyDTO;
import com.zhongan.icare.share.bpm.service.IProcessCarbonCopyService;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
class ProcessCarbonCopyServiceImpl implements IProcessCarbonCopyService {
    @Autowired
    ProcessCarbonCopyDAO dao;

    @Override
    public long create(@RequestBody ProcessCarbonCopyDTO dto) {
        Preconditions.checkArgument(dto != null,"dto不能为空.");
        ProcessCarbonCopyDO dataobject=to(dto);
        Long id=dataobject.getId();
        if(id==null)
        {
            id=SequenceFactory.nextId(ProcessCarbonCopyDO.class);
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
    public int update(@RequestBody ProcessCarbonCopyDTO dto) {
        Preconditions.checkArgument(dto != null&&dto.getId()!=null,"Id不能为空.");
        ProcessCarbonCopyDO dataobject=to(dto);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    @Override
    public List<ProcessCarbonCopyDTO> list(@RequestBody ProcessCarbonCopyQSO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessCarbonCopyQDO qdo = buildProcessCarbonCopyQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        List<ProcessCarbonCopyDO> dataobjects =  dao.selectByCond(qdo);
        return to(dataobjects);
    }

    @Override
    public int count(@RequestBody ProcessCarbonCopyQSO qso) {
        Preconditions.checkArgument(qso != null,"查询条件不能为空.");
        ProcessCarbonCopyQDO qdo = buildProcessCarbonCopyQDO(qso);
        qdo.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(qdo);
        return cnt;
    }

    @Override
    public ProcessCarbonCopyDTO get(@PathVariable("id") long id) {
        Preconditions.checkArgument(id >0,"id必须大于0");
        ProcessCarbonCopyDO dataobject =  dao.selectByPrimaryKey(id);
        return to(dataobject);
    }

    private ProcessCarbonCopyDTO to(ProcessCarbonCopyDO d) {
        if(null == d){
            return null;
        }
        ProcessCarbonCopyDTO t  = new ProcessCarbonCopyDTO();
        t.setId(d.getId());
        t.setState(d.getState());
        t.setProcInstId(d.getProcInstId());
        t.setBusinessKey(d.getBusinessKey());
        t.setCustId(d.getCustId());
        t.setCustName(d.getCustName());
        t.setTenantId(d.getTenantId());
        t.setProcDefId(d.getProcDefId());
        t.setProcDefKey(d.getProcDefKey());
        t.setProcDefName(d.getProcDefName());
        t.setSortId(d.getSortId());
        t.setStartCustId(d.getStartCustId());
        t.setStartCustName(d.getStartCustName());
        t.setStartTime(d.getStartTime());
        t.setEndTime(d.getEndTime());
        t.setProcIsEnd(d.getProcIsEnd());
        if(StringUtils.isNotEmpty(d.getIsDeleted())){
            t.setIsDeleted(com.zhongan.health.common.utils.bean.enm.EnumUtils.byValue(d.getIsDeleted(),com.zhongan.health.common.share.enm.YesOrNo.class));
        }
        t.setModifier(d.getModifier());
        t.setCreator(d.getCreator());
        t.setGmtCreated(d.getGmtCreated());
        t.setGmtModified(d.getGmtModified());
        return t;
    }

    private ProcessCarbonCopyDO to(ProcessCarbonCopyDTO t) {
        ProcessCarbonCopyDO d = new ProcessCarbonCopyDO();
        populate(d,t);
        return d;
    }

    private void populate(ProcessCarbonCopyDO d, ProcessCarbonCopyDTO t) {
        if(null == d){
            d = new ProcessCarbonCopyDO();
        }
        d.setId(t.getId());
        d.setState(t.getState());
        d.setProcInstId(t.getProcInstId());
        d.setBusinessKey(t.getBusinessKey());
        d.setCustId(t.getCustId());
        d.setCustName(t.getCustName());
        d.setTenantId(t.getTenantId());
        d.setProcDefId(t.getProcDefId());
        d.setProcDefKey(t.getProcDefKey());
        d.setProcDefName(t.getProcDefName());
        d.setSortId(t.getSortId());
        d.setStartCustId(t.getStartCustId());
        d.setStartCustName(t.getStartCustName());
        d.setStartTime(t.getStartTime());
        d.setEndTime(t.getEndTime());
        d.setProcIsEnd(t.getProcIsEnd());
        if (t.getIsDeleted() != null){
            d.setIsDeleted(t.getIsDeleted().getValue());
        }
        d.setModifier(t.getModifier());
        d.setCreator(t.getCreator());
        d.setGmtCreated(t.getGmtCreated());
        d.setGmtModified(t.getGmtModified());
    }

    private List<ProcessCarbonCopyDTO> to(List<ProcessCarbonCopyDO> dataobjects) {
        if(CollectionUtils.isEmpty(dataobjects)){
            return Collections.emptyList();
        }
        List<ProcessCarbonCopyDTO> dtos=Lists.newArrayListWithCapacity(dataobjects.size());
        for(ProcessCarbonCopyDO dataobject:dataobjects){dtos.add(to(dataobject));}
        return dtos;
    }

    private ProcessCarbonCopyQDO buildProcessCarbonCopyQDO(ProcessCarbonCopyQSO qso) {
        if(null == qso){
            return null;
        }
        ProcessCarbonCopyQDO result = new ProcessCarbonCopyQDO();
        populate(result,qso);
        return result;
    }
}