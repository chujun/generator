package com.zhongan.icare.share.bpm.service;

import com.zhongan.icare.bpm.bean.qso.CommentQSO;
import com.zhongan.icare.share.bpm.dto.CommentDTO;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "icare-account")
@RequestMapping(path = "/int/v1/comment")
public interface ICommentService {
    @RequestMapping(path = "create",method={RequestMethod.POST})
    long create(@RequestBody CommentDTO dto);

    @RequestMapping(path = "delete/{id}",method={RequestMethod.DELETE})
    int delete(@PathVariable("id") long id);

    @RequestMapping(path = "update",method={RequestMethod.POST})
    int update(@RequestBody CommentDTO dto);

    @RequestMapping(path = "list",method={RequestMethod.POST})
    List<CommentDTO> list(@RequestBody CommentQSO qso);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody CommentQSO qso);

    @RequestMapping(path = "get/{id}",method={RequestMethod.GET})
    CommentDTO get(@PathVariable("id") long id);
}