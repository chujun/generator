package com.zhongan.icare.share.bpm.service;

import com.zhongan.icare.share.bpm.bean.qdto.ExtraProcessDefinitionModelQDTO;
import com.zhongan.icare.share.bpm.dto.ExtraProcessDefinitionModelDTO;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    List<ExtraProcessDefinitionModelDTO> list(@RequestBody ExtraProcessDefinitionModelQDTO qso);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody ExtraProcessDefinitionModelQDTO qso);

    @RequestMapping(path = "get/{id}",method={RequestMethod.GET})
    ExtraProcessDefinitionModelDTO get(@PathVariable("id") long id);
}