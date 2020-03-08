package com.ws.security.service;


import com.ws.security.entity.SysRequestPathPermissionRelation;

import java.util.List;


/**
 * @Description:    //TODO 路径权限关联表(SysRequestPathPermissionRelation)表服务接口
 * @Author:         john
 * @CreateDate:     2020/3/8 12:48
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 12:48
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public interface SysRequestPathPermissionRelationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRequestPathPermissionRelation queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysRequestPathPermissionRelation> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param sysRequestPathPermissionRelation 实例对象
     * @return 实例对象
     */
    SysRequestPathPermissionRelation insert(SysRequestPathPermissionRelation sysRequestPathPermissionRelation);

    /**
     * 修改数据
     *
     * @param sysRequestPathPermissionRelation 实例对象
     * @return 实例对象
     */
    SysRequestPathPermissionRelation update(SysRequestPathPermissionRelation sysRequestPathPermissionRelation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
