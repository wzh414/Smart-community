package com.example.communitymanagement.dao;

import com.example.communitymanagement.entity.AccessData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface AccessDataRepository extends JpaRepository<AccessData,Long> {

    /**
     * 查找全部数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE create_time between ?1 and ?2 order by create_time desc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllBetweenDESC(String start, String end, Pageable req);

    /**
     * 查找全部数据
     * @return 全部数据
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE create_time between ?1 and ?2 order by create_time desc",
            nativeQuery = true)
    List<AccessData> findAllListBetweenDESC(String start, String end);

    /**
     * 查找全部数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE create_time between ?1 and ?2 order by create_time asc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllBetweenASC(String start, String end, Pageable req);

    /**
     * 查找全部数据
     * @return 全部数据
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE create_time between ?1 and ?2 order by create_time asc",
            nativeQuery = true)
    List<AccessData> findAllListBetweenASC(String start, String end);

    /**
     * 查找体温正常的数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2 order by create_time desc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllNormalBetweenDesc(String start, String end, Pageable req);

    /**
     * 查找体温正常的数据
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2 order by create_time desc",
            nativeQuery = true)
    List<AccessData> findAllNormalListBetweenDesc(String start, String end);

    /**
     * 查找体温正常的数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2 order by create_time asc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllNormalBetweenAsc(String start, String end, Pageable req);

    /**
     * 查找体温正常的数据
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature <= 38 and create_time between ?1 and ?2 order by create_time asc",
            nativeQuery = true)
    List<AccessData> findAllNormalListBetweenAsc(String start, String end);

    /**
     * 查找体温偏高的数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2 order by create_time desc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllHighBetweenDesc(String start, String end, Pageable req);

    /**
     * 查找体温偏高的数据
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2 order by create_time desc",
            nativeQuery = true)
    List<AccessData> findAllHighListBetweenDesc(String start, String end);

    /**
     * 查找体温偏高的数据
     * @param req 分页参数
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2 order by create_time asc",
            countQuery = "SELECT count(*) FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2",
            nativeQuery = true)
    Page<AccessData> findAllHighBetweenAsc(String start, String end, Pageable req);

    /**
     * 查找体温偏高的数据
     * @return 分页
     */
    @Query(value = "SELECT * FROM sys_checkin WHERE temperature > 38 and create_time between ?1 and ?2 order by create_time asc",
            nativeQuery = true)
    List<AccessData> findAllHighListBetweenAsc(String start, String end);

    /**
     * 统计某住户一段时间内出入次数
     * @param residentId 住户id
     * @param start 起始时间
     * @param end 截止时间
     * @return 次数
     */
    Integer countAccessDataByResidentIdAndCreateTimeBetween(Long residentId, Date start, Date end);


    /**
     * 统计某住户一段时间内体温异常次数
     * @param residentId 住户id
     * @param start 起始时间
     * @param end 结束时间
     * @return 次数
     */
    @Query(value = "SELECT count(*) FROM sys_checkin WHERE FK_resident_id = ?1 and create_time between ?2 and ?3 and temperature > 38",
            nativeQuery = true)
    Integer countHighAccessDataByResidentId(Long residentId, Date start, Date end);


    @Query(value = "SELECT * FROM sys_checkin WHERE FK_checker_id = ?1 and create_time > ?2 order by create_time desc",nativeQuery = true)
    List<AccessData> findTodayEnter(Long checkId,Date today);


    /**
     * 查找某住户ID的所有出入数据
     * @param residentId 住户ID
     * @return 出入数据
     */
    List<AccessData> findByResidentIdOrderByCheckinIdDesc(Long residentId);

    /**
     * 查找当天正常数据数
     * @param day 日期
     * @return 数量
     */
    @Query(value = "select count(*) from sys_checkin where date_format(create_time,'%Y-%m-%d') = ?1 and temperature <= 38",
            nativeQuery = true)
    Integer findOneDayNormalAccessData(String day);

    /**
     * 查找当天偏高数据数
     * @param day 日期
     * @return 数量
     */
    @Query(value = "select count(*) from sys_checkin where date_format(create_time,'%Y-%m-%d') = ?1 and temperature > 38",
            nativeQuery = true)
    Integer findOneDayHighAccessData(String day);

    @Modifying
    void deleteAccessDataByResidentId(Long residentId);

    @Modifying
    @Query(value = "update sys_checkin set checker_name = ?1 where FK_checker_id = ?2",
            nativeQuery = true)
    void modifyCheckerName(String nickname,Long checkerId);
}
