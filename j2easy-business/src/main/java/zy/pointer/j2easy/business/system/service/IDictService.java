package zy.pointer.j2easy.business.system.service;

import zy.pointer.j2easy.business.system.entity.Dict;
import zy.pointer.j2easy.framework.business.BusinessService;

import java.util.List;

public interface IDictService extends BusinessService<Dict> {

    /**
     * 检测 uniq 是否被其他所有字典占用
     * @param uniq  唯一值
     * @param id    字典主键值,如果传入ID则是判断除了指定字典以外是否存在,不传则全局判断是否存在.
     *              不传入ID的情况适用于新增,传入ID的情况适用于更新
     * @return
     */
    boolean checkUniqueExists( String uniq , Long id );

    /**
     * 检查 val 是否在同步元素中被使用
     * @param pId   父节点字典的主键ID
     * @param val   检查值
     * @param id    字典主键值,如果传入ID则是判断除了指定字典以外是否存在,不传则全局判断是否存在.
     *      *       不传入ID的情况适用于新增,传入ID的情况适用于更新
     * @return
     */
    boolean checkValExistsInSiblings( Long pId , String val , Long id );

}
