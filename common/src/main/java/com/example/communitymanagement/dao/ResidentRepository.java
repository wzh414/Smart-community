package com.example.communitymanagement.dao;

import com.example.communitymanagement.entity.SysResident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ResidentRepository  extends JpaRepository<SysResident,Long> {
    /**
     * 查找指定openid的住户
     * @param openid openid
     * @return 住户
     */
    SysResident findByWxOpenid(String openid);

    /**
     * 模糊查询名字匹配的住户
     * @param residentName 名字
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_resident WHERE resident_name like %?1% ",
            countQuery = "SELECT count(*) FROM sys_resident WHERE resident_name like %?1% ",
            nativeQuery = true)
    Page<SysResident> findByResidentNameLike(String residentName, Pageable req);

    /**
     * 更改住户状态
     * @param locked 是否锁定状态
     * @param residentId 用户id
     */
    @Modifying
    @Query(value = "update sys_resident set locked = ?1 where resident_id = ?2",nativeQuery=true)
    void updateStatusByResidentId(int locked, Long residentId);


    @Query(value = "SELECT * FROM sys_resident WHERE resident_name like %?1% ",
            nativeQuery = true)
    List<SysResident> findByResidentNameLike(String residentName);
    SysResident findByResidentId(Long residentId);

    @Modifying
    @Query(value = "update sys_resident set total_check_times = total_check_times + 1 where resident_id = ?1",nativeQuery = true)
    void updateTotalCheckTimesByResidentId(Long residentId);
}
