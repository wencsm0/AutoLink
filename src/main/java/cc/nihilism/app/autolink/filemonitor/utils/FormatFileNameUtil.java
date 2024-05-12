package cc.nihilism.app.autolink.filemonitor.utils;

import cc.nihilism.app.autolink.basic.exception.ServiceException;
import cc.nihilism.app.autolink.basic.utils.StrUtil;
import cc.nihilism.app.autolink.filemonitor.service.FormatFileNameInterface;
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
    private final FormatFileNameInterface formatFileNameInterface;

    public FormatFileNameUtil(FormatFileNameInterface formatFileNameInterface) {
        this.formatFileNameInterface = formatFileNameInterface;
    }

    public List<String> format(String targetPath, int year, int season) {
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
                String targetName = file.getName();

                // 转换文件名
                targetName = formatFileNameInterface.format(targetName, year, season, count++);

                // 输出结果
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

        return res;
    }

    /**
     * 重命名形如[xxx] name xxx [01][xxx]格式的文件名
     *
     * @param fileName 文件名
     * @param year     剧集年份
     * @param season   季数
     * @param episode  集数
     * @return [xxx]name.xxx.year.season.episode[xxx]
     */
    public static String format1(String fileName, int year, int season, int episode) {
        final String seasonStr = "S" + String.format("%02d", season);
        final String episodeStr = "E" + String.format("%02d", episode);

        // 替换[01]格式的剧集命名为' <year> E<season>S<index> '
        fileName = fileName.replaceAll("\\[\\d+]", " " + year + " " + seasonStr + episodeStr + " ");

        // 全角转半角
        fileName = StrUtil.toDBC(fileName);

        // 去除多余空格
        fileName = fileName.replaceAll(" +", " ");
        fileName = fileName.replaceAll("(?<![a-zA-Z0-9]) | (?![a-zA-Z0-9])", "");
        fileName = StrUtil.trim(fileName);

        // 空格转'.'
        fileName = fileName.replaceAll("(?<!]) (?!\\[)", ".");
        return fileName;
    }

    public static void main(String[] args) {
        String targetName = "[UHA-WINGS&VCB-Studio] Kaguya-sama wa Kokurasetai꞉ Tensai-tachi no Renai Zunousen [01][Ma10p_1080p][x265_flac]";

        // 输出结果
        // [UHA-WINGS&VCB-Studio]Kaguya-sama.wa.Kokurasetai꞉Tensai-tachi.no.Renai.Zunousen.2019.E01S01[Ma10p_1080p][x265_flac]
        System.out.println(format1(targetName, 2019, 1, 1));
    }
}
