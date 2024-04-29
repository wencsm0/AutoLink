package cc.nihilism.app.autolink.sysuser.domain;

import cc.nihilism.app.basic.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false, length = 16)
    private String salt;

    @Column(name = "status", nullable = false, length = 16, columnDefinition = "CHAR(1) DEFAULT '1'")
    private String status;

    @Column
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updateTime;

    @Column
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;
}
