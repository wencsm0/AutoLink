package cc.nihilism.app.autolink.controller;

import cc.nihilism.app.autolink.basic.HttpResult;
import cc.nihilism.app.autolink.sysuser.domain.SysUser;
import cc.nihilism.app.autolink.sysuser.mapper.SysUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys-user")
public class SysUserController {
    private final SysUserMapper sysUserMapper;

    public SysUserController(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @GetMapping("/list")
    public HttpResult list() {
        List<SysUser> websiteUser = sysUserMapper.findAll();
        return HttpResult.data(websiteUser);
    }
}
