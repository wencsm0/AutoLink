package cc.nihilism.app.autolink.sysuser.mapper;

import cc.nihilism.app.autolink.sysuser.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserMapper extends JpaRepository<SysUser, Long> {

    /**
     * 根据 username 查询数据
     */
    SysUser findByUsername(String name);
}
