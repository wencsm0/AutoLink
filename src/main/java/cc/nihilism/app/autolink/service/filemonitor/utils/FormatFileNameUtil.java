package cc.nihilism.app.autolink.service.filemonitor.utils;

import cc.nihilism.app.autolink.basic.exception.ServiceException;
import cc.nihilism.app.autolink.basic.utils.StrUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FormatFileNameUtil {

    public static List<String> rename(String targetPath, String fileName, int year, int season) {
        return rename(targetPath, fileName, year, season, "", "");
    }

    public static List<String> rename(String targetPath, String fileName, int year, int season, String prefix, String suffix) {
        File directory = new File(targetPath);
        File[] files = directory.listFiles();

        if (!directory.exists() || !directory.isDirectory() || files == null) {
            throw new ServiceException("目标文件夹不存在");
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

        List<String> res = new ArrayList<>();
        Map<String, List<File>> map = Arrays.stream(files).filter(File::isFile).collect(Collectors.groupingBy(e -> FilenameUtils.getExtension(e.getPath())));
        map.forEach((k, v) -> {
            int count = 1;
            for (File file : v) {
                // 转换文件名
                String targetName = format(fileName, year, season, count++, prefix, suffix);

                // 输出结果
                targetName = "/" + FilenameUtils.getPath(file.getAbsolutePath()) + targetName;
                res.add(targetName);

                // 重命名文件
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

        return res;
    }

    /**
     * 指定文件名的重命名
     */
    private static String format(String fileName, int year, int season, int episode, String prefix, String suffix) {
        String seasonStr = "S" + String.format("%02d", season);
        String episodeStr = "E" + String.format("%02d", episode);

        String format = fileName + "." + year + "." + seasonStr + "." + episodeStr;

        if (StrUtil.isNotEmpty(prefix)) {
            format = prefix + " " + format;
        }

        if (StrUtil.isNotEmpty(suffix)) {
            format = format + " " + suffix;
        }

        return format;
    }
}
