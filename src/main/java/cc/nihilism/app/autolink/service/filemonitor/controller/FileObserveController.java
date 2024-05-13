package cc.nihilism.app.autolink.service.filemonitor.controller;

import cc.nihilism.app.autolink.basic.HttpResult;
import cc.nihilism.app.autolink.service.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.service.filemonitor.service.FileObserveService;
import cc.nihilism.app.autolink.service.filemonitor.utils.FormatFileNameUtil;
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

    @PostMapping("/rename")
    public HttpResult renameMa10p(String targetPath, String fileName, int year, int season, String prefix, String suffix) {
        List<String> res = FormatFileNameUtil.rename(targetPath, fileName, year, season, prefix, suffix);

        return HttpResult.data(res);
    }
}
