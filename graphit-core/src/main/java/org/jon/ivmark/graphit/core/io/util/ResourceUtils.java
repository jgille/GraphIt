package org.jon.ivmark.graphit.core.io.util;

import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public final class ResourceUtils {

    private ResourceUtils() {}

    public static File resourceFile(String path) {
        URL url = Resources.getResource(path);
        return new File(url.getFile());
    }

    public static String resourceAsString(String path) {
        URL url = Resources.getResource(path);
        try {
            return Resources.toString(url, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read resource file", e);
        }
    }

    public static byte[] resource(String path) {
        URL url = Resources.getResource(path);
        try {
            return Resources.toByteArray(url);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read resource file", e);
        }
    }
}
