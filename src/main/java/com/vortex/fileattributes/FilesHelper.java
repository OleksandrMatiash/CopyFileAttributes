package com.vortex.fileattributes;

import com.vortex.fileattributes.domain.OperationResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesHelper {

    private static final Pattern SRC_PATTERN = Pattern.compile("(.*)\\..*");
    private static final String DST_PATTERN_SUFFIX = "[_]{0,}\\..*";

    public Map<String, String> matchFiles(Set<File> srcFiles, Set<File> dstFiles) {
        Map<String, String> result = new HashMap<>();
        for (File srcFile : srcFiles) {
            for (File dstFile : dstFiles) {
                if (srcFile.isFile() && dstFile.isFile()
                        && !result.containsKey(srcFile.getAbsolutePath())
                        && !result.containsValue(dstFile.getAbsolutePath())
                        && isFilesMatch(srcFile, dstFile)) {

                    result.put(srcFile.getAbsolutePath(), dstFile.getAbsolutePath());
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
            System.err.println("something went wrong!");
        }
        return false;
    }

    public OperationResult<List<String>, List<String>> copyAttributes(Map<File, File> matchedFiles) {
        List<String> successfullyUpdatedFiles = new ArrayList<>();
        List<String> notUpdatedFiles = null;
        for (Map.Entry<File, File> entry : matchedFiles.entrySet()) {
            File srcFile = entry.getKey();
            File dstFile = entry.getValue();

            try {
                BasicFileAttributes srcAttr = Files.getFileAttributeView(srcFile.toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS).readAttributes();
                BasicFileAttributeView dstAttrView = Files.getFileAttributeView(dstFile.toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

                dstAttrView.setTimes(srcAttr.lastModifiedTime(), srcAttr.lastAccessTime(), srcAttr.creationTime());
                successfullyUpdatedFiles.add(dstFile.getAbsolutePath());
            } catch (IOException e) {
                if (notUpdatedFiles == null) {
                    notUpdatedFiles = new ArrayList<>();
                }
                notUpdatedFiles.add(dstFile.getAbsolutePath());
            }
        }
        return new OperationResult<>(successfullyUpdatedFiles, notUpdatedFiles);
    }
}
