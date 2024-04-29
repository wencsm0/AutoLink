package cc.nihilism.app.autolink.controller;

import cc.nihilism.app.autolink.basic.HttpResult;
import cc.nihilism.app.autolink.filemonitor.domain.FileObserve;
import cc.nihilism.app.autolink.filemonitor.service.FileObserveService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        File directory = new File(targetPath);
        File[] files = directory.listFiles();

        if (!directory.exists() || !directory.isDirectory() || files == null) {
            return HttpResult.error("目标文件夹不存在");
        }

        Arrays.sort(files, (o1, o2) -> {
            String o1Suffix = FilenameUtils.getExtension(o1.getPath());
            String o2Suffix = FilenameUtils.getExtension(o2.getPath());
            int suffixCompare = o1Suffix.compareTo(o2Suffix);
            if (0 == suffixCompare) {
                return o1.getName().compareTo(o2.getName());
            }

            return suffixCompare;
        });

        final String seasonStr = "S" + String.format("%02d", season);

        List<String> res = new ArrayList<>();
        Map<String, List<File>> map = Arrays.stream(files).filter(File::isFile).collect(Collectors.groupingBy(e -> FilenameUtils.getExtension(e.getPath())));
        map.forEach((k, v) -> {
            int count = 1;
            for (File file : v) {
                String index = "E" + String.format("%02d", count++);
                String targetName = file.getName();
                targetName = targetName.replaceAll("\\[\\d{1,3}].?\\[Ma10p", " " + year + " " + seasonStr + index + " " + "[Ma10p");
                targetName = targetName.replace("  ", " ");
                targetName = targetName.replaceAll(" *： *", ":");
                targetName = targetName.replaceAll(" *꞉ *", ":");
                targetName = targetName.replaceAll("(?<!]) ", ".");
                targetName = "/" + FilenameUtils.getPath(file.getAbsolutePath()) + targetName;

                res.add(targetName);

                try {
                    File target = new File(targetName);
                    if (!target.exists()) {
                        FileUtils.moveFile(file, new File(targetName));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return HttpResult.data(res);
    }
}
