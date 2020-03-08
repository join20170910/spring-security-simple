package com.ws.security.dao;

import com.ws.security.entity.SysRequestPath;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * @Description:    //TODO 请求路径(SysRequestPath)表数据库访问层
 * @Author:         john
 * @CreateDate:     2020/3/8 13:04
 * @UpdateUser:     john
 * @UpdateDate:     2020/3/8 13:04
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public interface SysRequestPathDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRequestPath queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysRequestPath> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysRequestPath 实例对象
     * @return 对象列表
     */
    List<SysRequestPath> queryAll(SysRequestPath sysRequestPath);

    /**
     * 新增数据
     *
     * @param sysRequestPath 实例对象
     * @return 影响行数
     */
    int insert(SysRequestPath sysRequestPath);

    /**
     * 修改数据
     *
     * @param sysRequestPath 实例对象
     * @return 影响行数
     */
    int update(SysRequestPath sysRequestPath);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}
