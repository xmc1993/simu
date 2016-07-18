package cn.superid.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.util.Date;

/**
 * Created by 维 on 2014/8/29.
 */
public class FileUtil {

    private static final int BYTES_READ_ONCE_FROM_FILE_STREAM = 1024 * 1024; // 从文件流中每次读取的字节数

    public static boolean isSubPath(String subPath, String parentPath) {
        File subFile = new File(subPath);
        File parentFile = new File(parentPath);
        return subFile.getAbsolutePath().startsWith(parentFile.getAbsolutePath());
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean isExistFile(String path) {
        if (!exists(path)) {
            return false;
        }
        File file = new File(path);
        return file.isFile();
    }

    public static boolean isReadable(String path) {
        if (!isExistFile(path)) {
            return false;
        }
        File file = new File(path);
        return file.canRead();
    }

    public static Date getLastModifiedDate(String path) {
        if (!isReadable(path)) {
            return null;
        }
        File file = new File(path);
        return new Date(file.lastModified());
    }

    public static long writeFullyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        return writeFullyStreamAtRate(inputStream, outputStream, -1);
    }

    /**
     * 限制速度读写流
     *
     * @param inputStream
     * @param outputStream
     * @param rate         限制的速度，单位Byte/s，如果rate <= 0，表示不限速
     * @return
     * @throws java.io.IOException
     */
    public static long writeFullyStreamAtRate(InputStream inputStream, OutputStream outputStream, double rate) throws IOException {
        byte[] bytes = new byte[8092];
        long totalSize = 0;
        Date startTime = new Date();
        while (inputStream != null && inputStream.available() > 0) {
            int size = inputStream.read(bytes);
            totalSize += size;
            outputStream.write(bytes, 0, size);
            // 根据时间判断读写速度，如果达到阈值，就sleep一下
            if (rate > 0) {
                Date now = new Date();
                long mSeconds = now.getTime() - startTime.getTime();
                if (mSeconds > 0) {
                    double ioRate = totalSize * 1.0 / mSeconds;
                    System.out.println("now io rate is " + ioRate);
                    if (ioRate > rate) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new IOException(e);
                        }
                    }
                    ;
                }
            }
        }
        outputStream.flush();
        Date endTime = new Date();
        System.out.println("total io size is: " + totalSize + " Bytes and use time: " + (endTime.getTime() - startTime.getTime()) + "ms");
        return totalSize;
    }

    public static byte[] readFullyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeFullyStream(inputStream, byteArrayOutputStream);
        try {
            return byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
        }
    }

    public static String tryParseStreamToString(InputStream inputStream) throws IOException {
        return tryParseStreamToString(inputStream, null);
    }

    public static String tryParseStreamToString(InputStream inputStream, String encoding) throws IOException {
        if (inputStream == null) {
            return null;
        }
        if(StringUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeFullyStream(inputStream, byteArrayOutputStream);
        if (byteArrayOutputStream.size() > BYTES_READ_ONCE_FROM_FILE_STREAM) { // now max support 1MB file preview
            return null;
        }
        try {
            return new String(byteArrayOutputStream.toByteArray(), encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static class PipeWriteExecutor implements Runnable {
        private InputStream inputStream;
        private OutputStream outputStream;

        public PipeWriteExecutor(InputStream inputStream, OutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            int c;
            try {
                while ((c = inputStream.read()) != -1) {
                    outputStream.write(c);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static InputStream loadResource(String path) throws IOException {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static String loadResourceContent(String path) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = loadResource(path);
            return new String(readFullyInputStream(inputStream), "UTF-8");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static Document loadXmlResource(String path) throws IOException {
        String xmlStr = loadResourceContent(path);
        if (xmlStr == null) {
            return null;
        }
        return Jsoup.parse(xmlStr, "", Parser.xmlParser());
    }
}