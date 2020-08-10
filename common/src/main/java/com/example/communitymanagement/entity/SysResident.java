package com.example.communitymanagement.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Builder
@Table(name = "sys_resident" )
@NoArgsConstructor
@AllArgsConstructor
public class SysResident implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    @Id
    @Column(name = "resident_id", nullable = false)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.example.communitymanagement.utils.CustomIDGenerator")
    private Long residentId;

    @Column(name = "wx_openid", nullable = false)
    private String wxOpenid;

    private String avatar;

    @Column(name = "resident_name", nullable = false)
    private String residentName;

    @Column(name = "id_card_number", nullable = false)
    private String idCardNumber;

    @Column(name = "home_number", nullable = false)
    private String homeNumber;

    @Column(name = "phone_Number", nullable = false)
    private String phoneNumber;

    @Column(name = "total_check_times", nullable = false)
    private int totalCheckTimes;

    private int locked;

}
