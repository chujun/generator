package com.zhongan.icare.share.bpm.service;

import com.zhongan.icare.share.bpm.bean.qso.ExtraProcessDefinitionModelQSO;
import com.zhongan.icare.share.bpm.dto.ExtraProcessDefinitionModelDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "icare-bpm")
@RequestMapping(path = "/int/v1/extraProcessDefinition")
public interface IExtraProcessDefinitionModelService {
    @RequestMapping(path = "create",method={RequestMethod.POST})
    long create(@RequestBody ExtraProcessDefinitionModelDTO dto);

    @RequestMapping(path = "delete/{id}",method={RequestMethod.DELETE})
    int delete(@PathVariable("id") long id);

    @RequestMapping(path = "update",method={RequestMethod.POST})
    int update(@RequestBody ExtraProcessDefinitionModelDTO dto);

    @RequestMapping(path = "list",method={RequestMethod.POST})
    List<ExtraProcessDefinitionModelDTO> list(@RequestBody ExtraProcessDefinitionModelQSO qso);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody ExtraProcessDefinitionModelQSO qso);

    @RequestMapping(path = "get/{id}",method={RequestMethod.GET})
    ExtraProcessDefinitionModelDTO get(@PathVariable("id") long id);
}