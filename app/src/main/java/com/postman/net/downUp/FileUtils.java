package com.postman.net.downUp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;


public class FileUtils {
    /**
     * 检测文件是否存在
     *
     * @param filePath 要检测的文件路径
     * @return true, 文件存在;否则返回false
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }

    /**
     * 删除已存在的文件
     *
     * @param filePath 要删除的文件路径
     */
    public static void delExists(String filePath) {
        File file = new File(filePath);

        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 创建文件夹(包含父文件夹)
     *
     * @param dirPath 要创建的文件夹路径
     */
    public static void mkDirs(String dirPath) {
        File file = new File(dirPath);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取下载路径（收发文件之外的下载专用）
     *
     * @return 下载路径
     */
    public static String getDownloadDir() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = root + GlobalConfig.Dir.ROOT + GlobalConfig.Dir.DOWNLOAD;

        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }

        createNoMediumFile(dir);

        return dir;
    }

    /**
     * 获取收发文件下载路径(收发文件专用)
     *
     * @return 下载路径
     * <p>
     * add by 秘少帅 at 2016-11-12
     */
    public static String getFileDownloadDir() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = root + GlobalConfig.Dir.ROOT + GlobalConfig.Dir.DOWNLOAD_FILE;

        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return dir;
    }

    /**
     * 获取临时数据存储路径
     *
     * @return 存储路径
     * <p>
     * add by 王喆 at 2016-12-07
     */
    public static String getFileTempDir() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = root + GlobalConfig.Dir.ROOT + GlobalConfig.Dir.TEMP;

        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return dir;
    }

    /**
     * 获取临时数据存储路径
     *
     * @return 存储路径
     * <p>
     * add by 王喆 at 2016-12-07
     */
    public static String getDrawingDir() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = root + GlobalConfig.Dir.ROOT + GlobalConfig.Dir.DRAWING;

        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return dir;
    }

    /**
     * 获取临时数据存储路径
     *
     * @return 存储路径
     * <p>
     * add by 王喆 at 2016-12-07
     */
    public static String getVideoTempDir() {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = root + GlobalConfig.Dir.ROOT + GlobalConfig.Dir.VIDEO_TEMP;

        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }

        return dir;
    }

    /**
     * 从路径截取文件名
     *
     * @param url 路径参数，如“/sdcard/inter/down”,根据“/”截取
     * @return 文件名
     */
    public static String getFileName(String url) {
        String[] urlArray = url.split("/");
        int urlArrayLength = urlArray.length;

        // 截取文件名 
        String fileName = urlArray[urlArrayLength - 1];

        return fileName;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile     源文件File
     * @param destDir     目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    public static long copyFile(File srcFile, File destDir, String newFileName) {
        long copySizes = 0;

        if (!srcFile.exists()) {
            System.out.println("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            System.out.println("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            System.out.println("文件名为null");
            copySizes = -1;
        } else {
            try {
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                fcin.close();
                fcout.close();
                copySizes = size;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return copySizes;
    }

    /**
     * 创建".nomedia"文件(使该文件所在的文件夹内的媒体资源不在相册中显示)
     *
     * @param dir 要创建".nomedia"文件的文件夹
     * @return true, 如果文件创建成功;否则返回false
     */
    public static boolean createNoMediumFile(String dir) {
        mkDirs(dir);

        File file = new File(dir + "/.nomedia");

        if (!file.exists()) {
            try {
                file.createNewFile();

                return true;
            } catch (IOException e) {
            }
        }

        return false;
    }

    /**
     * 删除指定文件夹下 除file_name 外的文件
     *
     * @param file     指定文件夹 file 对象
     * @param fileName 指定例外的文件名称 可以为 null
     */
    public static void DeleteFile(File file, String fileName) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }

            if (file.isDirectory()) {
                File[] childFile = file.listFiles();

                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }

                for (File f : childFile) {
                    if (f.getName() != null && !f.getName().contains(fileName)) {
                        DeleteFile(f, fileName);
                    }
                }
            }
        }
    }

    /***
     * 获取文件/文件夹大小
     *
     * @param f 文件夹对象
     * @return long 文件大小（单位B）
     *
     * @throws Exception
     */
    public static long getFolderSize(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();

        int len = flist.length;
        for (int i = 0; i < len; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFolderSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }

        return size;
    }

    /**
     * 将byte 单位转换成 MB
     *
     * @param byteSize byte单位
     * @return double (单位MB)
     */
    public static double getDirectorySize(long byteSize) {
        double DirectorySize = 0;
        DirectorySize = ((double) byteSize / 1048576);

        return DirectorySize;
    }

    /**
     * bitmap转换为图片保存到本地
     *
     * @param path     本地图片存放路径
     * @param bitmap   待转换的bitmap
     * @param compress 图片压缩率
     */
    public static void saveToSdCard(String path, Bitmap bitmap, int compress) {
        if (null != bitmap && null != path && !path.equalsIgnoreCase("")) {
            try {
                FileOutputStream outputStream = null;

                //创建文件，并写入内容
                outputStream = new FileOutputStream(new File(path), true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, compress, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param bytes    数据传入
     * @param filePath 文件路径
     */
    public static void createFileWithByte(byte[] bytes, String filePath) {
        File file = new File(filePath);
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            outputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取根路径
     *
     * @return
     */
    public static String getRootPath() {
        File path = null;

        if (sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = Environment.getDataDirectory();
        }

        return path.getAbsolutePath();
    }

    /**
     * SD卡是否可用.
     *
     * @return true 可用，false 不可用
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else {
            return false;
        }
    }

    /**
     * 得到文件内容
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();

        if (fileSize > Integer.MAX_VALUE) {
            return null;
        }

        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;

        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset != buffer.length) {
            fi.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        fi.close();
        return buffer;
    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }
}
