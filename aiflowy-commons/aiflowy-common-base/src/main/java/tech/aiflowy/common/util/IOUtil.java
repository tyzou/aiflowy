package tech.aiflowy.common.util;

import okio.BufferedSink;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOUtil {
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void writeBytes(byte[] bytes, File toFile) {
        try (FileOutputStream stream = new FileOutputStream(toFile)) {
            stream.write(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] readBytes(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return readBytes(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] readBytes(InputStream inputStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            copy(inputStream, outStream);
            return outStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void copy(InputStream inputStream, BufferedSink sink) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != -1; ) {
            sink.write(buffer, 0, len);
        }
    }

    public static void copy(InputStream inputStream, OutputStream outStream) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != -1; ) {
            outStream.write(buffer, 0, len);
        }
    }

    public static String readUtf8(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        copy(inputStream, outStream);
        return new String(outStream.toByteArray(), StandardCharsets.UTF_8);
    }


}
