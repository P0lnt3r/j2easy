package zy.pointer.j2easy.business.system.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.business.system.mapper.DictMapper;
import zy.pointer.j2easy.business.system.service.IDictService;
import zy.pointer.j2easy.framework.business.AbsBusinessService;

@Transactional
@Primary
@Service
public class DictServiceImpl extends AbsBusinessService<DictMapper , Dict> implements IDictService {



}
