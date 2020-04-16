package zy.pointer.j2easy.api.bm.cms.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zy.pointer.j2easy.api.bm.cms.dto.DiscussDTO;
import zy.pointer.j2easy.api.bm.cms.dto.DiscussQueryDTO;
import zy.pointer.j2easy.api.bm.cms.vo.DiscussVO;
import zy.pointer.j2easy.business.cms.entity.Discuss;
import zy.pointer.j2easy.business.cms.service.IDiscussService;
import zy.pointer.j2easy.framework.web.model.vo.PageVo;

@RestController
@RequestMapping("/api/bm/cms/discuss")
public class DiscussController {

    @Autowired
    IDiscussService discussService;

    @GetMapping("/queryQuestions")
    public PageVo<DiscussVO , Discuss> queryQuestions( DiscussQueryDTO DTO ){
        return new PageVo<DiscussVO , Discuss>().from(
                discussService.selectByMapForPage_extras( DTO.convert() , BeanUtil.beanToMap( DTO )) ,
                DiscussVO.class
        );
    }

    @GetMapping("/queryReplies")
    public PageVo<DiscussVO , Discuss> queryReplies( DiscussQueryDTO DTO ){
        return new PageVo<DiscussVO , Discuss>().from(
                discussService.selectByMapForPage_replies( DTO.convert() , BeanUtil.beanToMap( DTO )) ,
                DiscussVO.class
        );
    }

    @PostMapping("/update")
    @ApiOperation("讨论更新")
    public int update( DiscussDTO DTO ){
        return discussService.updateById( DTO.convert() ) ? 1 : 0;
    }


}
