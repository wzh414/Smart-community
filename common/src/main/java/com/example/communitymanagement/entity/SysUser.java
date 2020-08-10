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
@Table(name = "sys_user" )
@NoArgsConstructor
@AllArgsConstructor
public class SysUser implements Serializable {
    private static final long serialVersionUID = 367960851023519541L;

    @Id
    @Column(name = "user_id", nullable = false)
    @JSONField(serializeUsing = ToStringSerializer.class)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.example.communitymanagement.utils.CustomIDGenerator")
    private Long userId;

    private String username;

    private String passwd;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    private String nickname;

    @Column(name = "user_status", nullable = false)
    private int userStatus;

}
