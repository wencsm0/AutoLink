package cc.nihilism.app.autolink.sysuser.mapper;

import cc.nihilism.app.autolink.sysuser.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserMapper extends JpaRepository<SysUser, Long> {
}
