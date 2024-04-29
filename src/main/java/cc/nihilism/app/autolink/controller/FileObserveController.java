package cc.nihilism.app.autolink.controller;

import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.filemonitor.service.FileObserveService;
import cc.nihilism.app.basic.HttpResult;
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
}
