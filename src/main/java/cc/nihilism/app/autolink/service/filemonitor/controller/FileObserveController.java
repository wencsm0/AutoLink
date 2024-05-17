package cc.nihilism.app.autolink.service.filemonitor.controller;

import cc.nihilism.app.autolink.basic.HttpResult;
import cc.nihilism.app.autolink.service.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.service.filemonitor.service.FileObserveService;
import cc.nihilism.app.autolink.service.filemonitor.utils.FormatFileNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public HttpResult rename(String targetPath, String fileName, int year, int season, String prefix, String suffix) throws IOException {
        List<String> res = FormatFileNameUtil.rename(targetPath, fileName, year, season, prefix, suffix);

        return HttpResult.data(res);
    }

    @PostMapping("/preview")
    public HttpResult preview(String targetPath, String fileName, int year, int season, String prefix, String suffix) {
        List<Map<String, String>> res = FormatFileNameUtil.preview(targetPath, fileName, year, season, prefix, suffix);

        return HttpResult.data(res);
    }

    @PostMapping("/scan")
    public HttpResult scan(String targetPath) {
        File directory = new File(targetPath);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return HttpResult.error("目标目录不存在");
        }

        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();

        return HttpResult.data(fileNames);
    }
}
