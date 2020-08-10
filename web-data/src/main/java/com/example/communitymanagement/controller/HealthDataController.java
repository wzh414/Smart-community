package com.example.communitymanagement.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.communitymanagement.entity.ResidentDetail;
import com.example.communitymanagement.service.HealthDataService;
import com.sun.istack.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/health/data")
public class HealthDataController {

    @Resource
    HealthDataService healthDataService;

    @RequestMapping("/listAllResident")
    public String listAllAdmin(){
        return healthDataService.getHealthDataList();
    }

    @RequestMapping("/getResidentDetail")
    public String getResidentDetail(@NotNull @RequestParam("residentId") String residentId){
        return healthDataService.getHealthDataByResidentId(Long.parseLong(residentId));
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("/residentDetailDownload")
    public void downloadFailedUsingJson(HttpServletResponse response, @NotNull @RequestParam("residentId") String residentId) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("用户"+residentId+"数据", StandardCharsets.UTF_8);;

            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ResidentDetail.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data(residentId));
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private List<ResidentDetail> data(String residentId){
        return healthDataService.getResidentDetailListByResidentId(Long.parseLong(residentId));
    }

    @RequestMapping("/getLineCharts")
    public String getLineCharts(){
        return healthDataService.getLineChart();
    }

    @RequestMapping("/getPieCharts")
    public String getPieCharts(){
        return healthDataService.getPieChart();
    }

    @RequestMapping("/getHighInfo")
    public String getHighInfo(){
        return healthDataService.getHighData();
    }
}
