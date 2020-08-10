package com.example.communitymanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDetail implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    @ExcelProperty("检查时间")
    private Date createTime;

    @ExcelProperty("住户姓名")
    private String residentName;

    @ExcelProperty("检查人员")
    private String checkerName;

    @ExcelProperty("登记体温")
    private Double temperature;
}
