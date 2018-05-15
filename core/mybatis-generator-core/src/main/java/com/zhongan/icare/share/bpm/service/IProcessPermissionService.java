package com.zhongan.icare.share.bpm.service;

import com.zhongan.icare.share.bpm.bean.qdto.ProcessPermissionQDTO;
import com.zhongan.icare.share.bpm.dto.ProcessPermissionDTO;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "icare-bpm")
@RequestMapping(path = "/int/v1/processPermission")
public interface IProcessPermissionService {
    @RequestMapping(path = "create",method={RequestMethod.POST})
    long create(@RequestBody ProcessPermissionDTO dto);

    @RequestMapping(path = "delete/{id}",method={RequestMethod.DELETE})
    int delete(@PathVariable("id") long id);

    @RequestMapping(path = "update",method={RequestMethod.POST})
    int update(@RequestBody ProcessPermissionDTO dto);

    @RequestMapping(path = "list",method={RequestMethod.POST})
    List<ProcessPermissionDTO> list(@RequestBody ProcessPermissionQDTO qso);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody ProcessPermissionQDTO qso);

    @RequestMapping(path = "get/{id}",method={RequestMethod.GET})
    ProcessPermissionDTO get(@PathVariable("id") long id);
}