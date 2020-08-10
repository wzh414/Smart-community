package com.example.communitymanagement.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthData  implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long residentId;

    private String residentName;

    private String idCardNumber;

    private String homeNumber;

    private Integer totalCheckTimes;

    private Integer lastSevenDayTimes;

    private Integer lastSevenDayHighTimes;

    private Integer locked;

}
