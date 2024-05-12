package cc.nihilism.app.autolink.controller;

import cc.nihilism.app.autolink.basic.HttpResult;
import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.filemonitor.service.FileObserveService;
import cc.nihilism.app.autolink.filemonitor.utils.FormatFileNameUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file-observe")
public class FileObserveController {
    private final FileObserveService service;

    public FileObserveController(FileObserveService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public HttpResult list() {
        List<FileObserve> list = service.list();
        return HttpResult.data(list);
    }

    @PostMapping("/create")
    public HttpResult create(@RequestBody FileObserve fileObserve) {
        service.save(fileObserve);
        return HttpResult.success();
    }

    @PostMapping("/remove")
    public HttpResult remove(@RequestBody FileObserve fileObserve) {
        service.delete(fileObserve);
        return HttpResult.success();
    }

    @PostMapping("/update")
    public HttpResult update(@RequestBody FileObserve fileObserve) {
        service.update(fileObserve);
        return HttpResult.success();
    }

    @PostMapping("/rename-ma10p")
    public HttpResult renameMa10p(String targetPath, int year, int season) {
        FormatFileNameUtil util = new FormatFileNameUtil(FormatFileNameUtil::format1);
        List<String> res = util.format(targetPath, year, season);

        return HttpResult.data(res);
    }
}
