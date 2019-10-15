package com.vortex.fileattributes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesMatcher {


    public static final Pattern SRC_PATTERN = Pattern.compile("(.*)\\..*");
    public static final String DST_PATTERN_SUFFIX = "[_]{0,}\\..*";

    public Map<File, File> matchFiles(Set<File> srcFiles, Set<File> dstFiles) {
        Map<File, File> result = new HashMap<>();
        for (File srcFile : srcFiles) {
            for (File dstFile : dstFiles) {
                if (srcFile.isFile() && dstFile.isFile() && !result.containsKey(srcFile) && !result.containsValue(dstFile)
                        && isFilesMatch(srcFile, dstFile)) {
                    result.put(srcFile, dstFile);
                }
            }
        }
        return result;
    }

    boolean isFilesMatch(File srcFile, File dstFile) {
        Matcher srcMatcher = SRC_PATTERN.matcher(srcFile.getName().toLowerCase());
        boolean matches = srcMatcher.matches();
        if (matches) {
            String srcNameWithoutExtension = srcMatcher.group(1);
            Matcher dstMatcher = Pattern.compile(srcNameWithoutExtension + DST_PATTERN_SUFFIX).matcher(dstFile.getName().toLowerCase());
            return dstMatcher.matches();
        } else {
            System.out.println("something went wrong!");
        }
        return false;
    }
}
