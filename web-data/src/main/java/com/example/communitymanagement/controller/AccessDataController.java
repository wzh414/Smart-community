package com.example.communitymanagement.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.example.communitymanagement.entity.AccessData;
import com.example.communitymanagement.service.AccessDataService;
import com.sun.istack.NotNull;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/access/data")
@PreAuthorize("hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')")
public class AccessDataController {
    @Resource
    AccessDataService accessDataService;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/listAllAccessData")
    public String listAllAdmin(@NotNull @RequestParam("pageNum") int pageNum, @NotNull @RequestParam("tag") String tag,  @NotNull @RequestParam("sorted") String sorted,
                               @NotNull @RequestParam("start") String start, @NotNull @RequestParam("end") String end){
        try {
            Date dd = df.parse(end);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dd);
            calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
            end = df.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if("all".equals(tag)) {
            return accessDataService.getAllAccessDataPage(pageNum, 10, start, end , sorted);
        }
        else {
            return accessDataService.getTagAccessDataPage(pageNum, 10, tag, start, end, sorted);
        }
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("/accessDataDownload")
    public void downloadFailedUsingJson(HttpServletResponse response, @NotNull @RequestParam("tag") String tag,  @NotNull @RequestParam("sorted") String sorted,
                                        @NotNull @RequestParam("start") String start, @NotNull @RequestParam("end") String end) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName;
            if("all".equals(tag)) {
                fileName = URLEncoder.encode("体温全部数据", StandardCharsets.UTF_8);
            }else if("normal".equals(tag)){
                fileName = URLEncoder.encode("体温正常数据", StandardCharsets.UTF_8);
            }else{
                fileName = URLEncoder.encode("体温偏高数据", StandardCharsets.UTF_8);
            }
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), AccessData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(data(tag, sorted, start, end));
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

    private List<AccessData> data(String tag, String sorted, String start, String end) {
        if("all".equals(tag)) {
            return accessDataService.getAllAccessDataList(start, end , sorted);
        }
        else {
            return accessDataService.getTagAccessDataList(tag, start, end, sorted);
        }
    }
}
