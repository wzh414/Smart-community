package com.example.communitymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineChart implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    public String date;
    public Integer normal;
    public Integer high;
}
