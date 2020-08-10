package com.example.communitymanagement.dao;

import com.example.communitymanagement.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<SysUser,Long> {
    /**
     * 根据用户名获取用户信息(此处用于登录后台管理系统，检查人员无法登录)
     * @param username 用户名
     * @return 用户实体类
     */
    @Query(value = "select * from sys_user where username = ?1 and (user_role = 'ROLE_SUPERADMIN' or user_role = 'ROLE_ADMIN')",nativeQuery=true)
    SysUser checkinBack(String username);

    /**
     * 根据用户名获取用户信息(此处用于小程序前台登录，其他人员无法登录)
     * @param username 用户名
     * @return 用户实体类
     */
    @Query(value = "select * from sys_user where username = ?1 and user_role = 'ROLE_CHECK'",nativeQuery=true)
    SysUser checkinFront(String username);

    /**
     * 根据角色查询系统用户信息
     * @param userRole 角色
     * @param req 分页
     * @return 该角色本页信息
     */
    Page<SysUser> findByUserRole(String userRole, Pageable req);

    /**
     * 根据账号查找用户
     * @param username 账号
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 根据关键词搜索系统管理员信息
     */
    @Query(value = "SELECT * FROM sys_user WHERE user_role = 'ROLE_ADMIN' and (username like %?1% or nickname like %?2%)",
            countQuery = "SELECT count(*) FROM sys_user WHERE user_role = 'ROLE_ADMIN' and (username like %?1% or nickname like %?2%)",
            nativeQuery = true)
    Page<SysUser> findAdminBySearch(String username, String nickname, Pageable req);

    /**
     * 根据关键词搜检查人员信息
     */
    @Query(value = "SELECT * FROM sys_user WHERE user_role = 'ROLE_CHECK' and (username like %?1% or nickname like %?2%)",
            countQuery = "SELECT count(*) FROM sys_user WHERE user_role = 'ROLE_CHECK' and (username like %?1% or nickname like %?2%)",
            nativeQuery = true)
    Page<SysUser> findCheckBySearch(String username, String nickname, Pageable req);

    /**
     * 更改用户密码
     * @param newPassword 新密码
     * @param userId 用户id
     */
    @Modifying
    @Query(value = "update sys_user set passwd = ?1 where user_id = ?2 and user_role = ?3",nativeQuery=true)
    void updateUserPasswordByUserId(String newPassword, Long userId,String userRole);

    /**
     * 更改用户昵称
     * @param newNickname 新昵称
     * @param userId 用户id
     */
    @Modifying
    @Query(value = "update sys_user set nickname = ?1 where user_id = ?2 and user_role = ?3",nativeQuery=true)
    void updateUserNicknameByUserId(String newNickname, Long userId,String userRole);

    /**
     * 更改用户状态
     * @param userStatus 是否禁用状态
     * @param userId 用户id
     */
    @Modifying
    @Query(value = "update sys_user set user_status = ?1 where user_id = ?2 and user_role = ?3",nativeQuery=true)
    void updateUserStatusByUserId(int userStatus, Long userId,String userRole);

    /**
     * 删除指定id的信息
     * @param userId id
     * @param userRole 角色
     */
    @Modifying
    void deleteByUserIdAndUserRole(Long userId,String userRole);

}
