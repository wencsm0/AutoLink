package cc.nihilism.app.autolink.service.filemonitor.domain;

import cc.nihilism.app.autolink.basic.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Table(name = "file_observe")
@EqualsAndHashCode(callSuper = true)
@Data
public class FileObserve extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String filePath;

    @Column(nullable = false)
    private String linkPath;

    @Column(nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT '1'")
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
