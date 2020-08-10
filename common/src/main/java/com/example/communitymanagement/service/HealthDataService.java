package com.example.communitymanagement.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.communitymanagement.dao.AccessDataRepository;
import com.example.communitymanagement.dao.ResidentRepository;
import com.example.communitymanagement.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackOn = Exception.class)
public class HealthDataService {
    @Autowired
    ResidentRepository residentRepository;
    @Autowired
    AccessDataRepository accessDataRepository;

    public final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public String getHealthDataList(){
        Map<String, Object> map = new HashMap<>();
        List<HealthData> healthDataList = new ArrayList<>();
        List<SysResident> residents = residentRepository.findAll();

        map.put("totalElements",residents.size());

        for(SysResident resident : residents){
            HealthData healthData = HealthData.builder()
                    .residentId(resident.getResidentId())
                    .residentName(resident.getResidentName())
                    .idCardNumber(resident.getIdCardNumber())
                    .homeNumber(resident.getHomeNumber())
                    .totalCheckTimes(resident.getTotalCheckTimes())
                    .lastSevenDayTimes(
                            accessDataRepository.countAccessDataByResidentIdAndCreateTimeBetween(
                                    resident.getResidentId(),getPastDate(7),getNowDate()
                            )
                    )
                    .lastSevenDayHighTimes(
                            accessDataRepository.countHighAccessDataByResidentId(
                                    resident.getResidentId(),getPastDate(7),getNowDate()
                            )
                    )
                    .build();
            healthDataList.add(healthData);
        }
        map.put("content",healthDataList);
        return JSON.toJSONString(map);
    }

    public String getScanHealthData(String id){
        Map<String, Object> map = new HashMap<>();
        Long residentId = Long.valueOf(id);
        int count = accessDataRepository.countHighAccessDataByResidentId(residentId,getPastDate(7),getNowDate());
        SysResident resident = residentRepository.findByResidentId(residentId);
        map.put("id",resident.getResidentId().toString());
        map.put("avatar",resident.getAvatar());
        map.put("homeNumber",resident.getHomeNumber());
        map.put("idCard",resident.getIdCardNumber());
        map.put("name",resident.getResidentName());
        map.put("phoneNumber",resident.getPhoneNumber());
        map.put("locked",resident.getLocked());
        map.put("times",resident.getTotalCheckTimes());
        map.put("high",count);
        if (count >= 1){
            map.put("tip","warn");
        }else {
            map.put("tip","good");
        }
        return JSON.toJSONString(map);
    }

    public String getHealthDataByResidentId(Long residentId){
        return JSONArray.toJSONString(accessDataRepository.findByResidentIdOrderByCheckinIdDesc(residentId));
    }

    public List<ResidentDetail> getResidentDetailListByResidentId(Long residentId){
        List<ResidentDetail> residentDetails = new ArrayList<>();
        List<AccessData> accessDataList = accessDataRepository.findByResidentIdOrderByCheckinIdDesc(residentId);
        for(AccessData accessData : accessDataList){
            ResidentDetail residentDetail = ResidentDetail.builder()
                    .createTime(accessData.getCreateTime())
                    .residentName(accessData.getResidentName())
                    .checkerName(accessData.getCheckerName())
                    .temperature(accessData.getTemperature())
                    .build();
            residentDetails.add(residentDetail);
        }
        return residentDetails;
    }

    public String getLineChart(){
        List<LineChart> lineCharts = new ArrayList<>();
        for(int i=6;i>=0;i--){
            String date = SIMPLE_DATE_FORMAT.format(getPastDate(i));
            LineChart lineChart = LineChart.builder()
                    .date(date)
                    .normal(accessDataRepository.findOneDayNormalAccessData(date))
                    .high(accessDataRepository.findOneDayHighAccessData(date))
                    .build();
            lineCharts.add(lineChart);
        }
        return JSONArray.toJSONString(lineCharts);
    }

    public String getPieChart(){
        String date = SIMPLE_DATE_FORMAT.format(getNowDate());
        LineChart lineChart = LineChart.builder()
                .date(date)
                .normal(accessDataRepository.findOneDayNormalAccessData(date))
                .high(accessDataRepository.findOneDayHighAccessData(date))
                .build();
        return JSON.toJSONString(lineChart);
    }

    public String getHighData(){
        List<HealthData> healthDataList = new ArrayList<>();
        List<SysResident> residents = residentRepository.findAll();

        for(SysResident resident : residents){
            Integer lastSevenDayHighTimes = accessDataRepository.countHighAccessDataByResidentId(
                    resident.getResidentId(),getPastDate(7),getNowDate()
            );
            if(lastSevenDayHighTimes >= 1) {
                HealthData healthData = HealthData.builder()
                        .residentId(resident.getResidentId())
                        .residentName(resident.getResidentName())
                        .idCardNumber(resident.getIdCardNumber())
                        .homeNumber(resident.getHomeNumber())
                        .totalCheckTimes(resident.getTotalCheckTimes())
                        .lastSevenDayHighTimes(lastSevenDayHighTimes)
                        .locked(resident.getLocked())
                        .build();
                healthDataList.add(healthData);
            }
        }
        return JSON.toJSONString(healthDataList);
    }

    public Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        return calendar.getTime();

    }
    public Date getNowDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
