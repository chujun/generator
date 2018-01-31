package com.zhongan.icare.share.bpm.service;

import com.zhongan.icare.share.bpm.bean.qso.ProcessCarbonCopyQSO;
import com.zhongan.icare.share.bpm.dto.ProcessCarbonCopyDTO;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "icare-account")
@RequestMapping(path = "/int/v1/process/sort")
public interface IProcessCarbonCopyService {
    @RequestMapping(path = "create",method={RequestMethod.POST})
    long create(@RequestBody ProcessCarbonCopyDTO dto);

    @RequestMapping(path = "delete/{id}",method={RequestMethod.DELETE})
    int delete(@PathVariable("id") long id);

    @RequestMapping(path = "update",method={RequestMethod.POST})
    int update(@RequestBody ProcessCarbonCopyDTO dto);

    @RequestMapping(path = "list",method={RequestMethod.POST})
    List<ProcessCarbonCopyDTO> list(@RequestBody ProcessCarbonCopyQSO qso);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody ProcessCarbonCopyQSO qso);

    @RequestMapping(path = "get/{id}",method={RequestMethod.GET})
    ProcessCarbonCopyDTO get(@PathVariable("id") long id);
}