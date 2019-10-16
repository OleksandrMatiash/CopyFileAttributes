package com.vortex.fileattributes;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.junit.Assert.*;

public class FilesHelperTest {
    private static final String EXECUTION_PATH_STR = getExecutionPath();

    @Test
    public void isFilesMatch_noMatch() throws IOException {
        boolean result = new FilesHelper().isFilesMatch(
                getFile("/A.txt"),
                getFile("/B.txt"));

        assertFalse(result);
    }

    @Test
    public void isFilesMatch_match1() throws IOException {
        boolean result = new FilesHelper().isFilesMatch(
                getFile("/A.txt"),
                getFile("/innerDirectory/A_.txt"));

        assertTrue(result);
    }

    @Test
    public void isFilesMatch_match2() throws IOException {
        boolean result = new FilesHelper().isFilesMatch(
                getFile("/A.txt"),
                getFile("/innerDirectory/a.pdf"));

        assertTrue(result);
    }

    private File getFile(String result1) {
        return new File(EXECUTION_PATH_STR + result1);
    }

    @Test
    public void matchFiles_noMatches() {
        HashSet<File> srcFiles = new HashSet<>();
        srcFiles.add(getFile("/A.txt"));

        HashSet<File> dstFiles = new HashSet<>();
        dstFiles.add(getFile("/B.txt"));

        Map<String, String> result = new FilesHelper().matchFiles(srcFiles, dstFiles);

        assertTrue(result.isEmpty());
    }

    @Test
    public void matchFiles_oneMatch() {
        LinkedHashSet<File> srcFiles = new LinkedHashSet<>();
        srcFiles.add(getFile("/A.txt"));

        LinkedHashSet<File> dstFiles = new LinkedHashSet<>();
        dstFiles.add(getFile("/innerDirectory/A_.txt"));
        dstFiles.add(getFile("/innerDirectory/a.pdf"));

        Map<String, String> result = new FilesHelper().matchFiles(srcFiles, dstFiles);

        assertEquals(1, result.size());
        assertTrue(result.keySet().iterator().next().contains("A.txt"));
        assertTrue(result.values().iterator().next().contains("A_.txt"));
    }

    private static String getExecutionPath() {
        return new File(FilesHelperTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getAbsolutePath();
    }
}