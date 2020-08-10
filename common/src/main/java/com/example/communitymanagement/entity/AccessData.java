package com.example.communitymanagement.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Data
@Entity
@Builder
@Table(name = "sys_checkin" )
@NoArgsConstructor
@AllArgsConstructor
public class AccessData implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    @Id
    @ExcelProperty("记录ID")
    @Column(name = "checkin_id", nullable = false)
    @NumberFormat("#####################")
    @JSONField(serializeUsing = ToStringSerializer.class)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.example.communitymanagement.utils.CustomIDGenerator")
    private Long checkinId;

    @ExcelProperty("住户ID")
    @NumberFormat("#####################")
    @Column(name = "FK_resident_id", nullable = false)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long residentId;

    @ExcelProperty("住户姓名")
    private String residentName;

    @ExcelProperty("检查人员ID")
    @NumberFormat("#####################")
    @Column(name = "FK_checker_id", nullable = false)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long checkerId;

    @ExcelProperty("检查人员昵称")
    private String checkerName;

    @ExcelProperty("登记时间")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @ExcelProperty("温度")
    @Column(name = "temperature", nullable = false)
    private Double temperature;
}
