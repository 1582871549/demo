package com.dudu.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 大橙子
 */
public class FileUtil {

    /**
     * 功能描述：列出某文件夹及其子文件夹下面的文件，并可根据扩展名过滤
     *
     * @param path 文件夹
     */
    public static void list(File path) {
        //判断文件是否存在
        if (!path.exists()) {
            System.out.println("文件名称不存在!");
            return;
        }

        //判断该路径是否是一个文件
        if (!path.isFile()) {

            File[] files = path.listFiles();

            assert files != null;

            for (File file : files) {

                System.out.println(file.getName() + "==================");

                list(file);
            }
            return;
        }

        String java = ".java";

        boolean flag = path.getName().toLowerCase().endsWith(java);

        if (flag) {
            // 文件格式
            System.out.println(path + "     " + path.getName());
            System.out.println();
        }
    }


    /**
     * 功能描述：拷贝一个目录或者文件到指定路径下，即把源文件拷贝到目标文件路径下
     *
     * @param source 源文件
     * @param target 目标文件路径
     * @return void
     */
    public static void copy(File source, File target) {

        File tarpath = new File(target, source.getName());

        if (source.isDirectory()) {

            tarpath.mkdir();
            File[] dir = source.listFiles();
            assert dir != null;
            for (File aDir : dir) {
                copy(aDir, tarpath);
            }
        } else {
            try {
                // 用于读取文件的原始字节流
                InputStream is = new FileInputStream(source);
                // 用于写入文件的原始字节的流
                OutputStream os = new FileOutputStream(tarpath);
                // 存储读取数据的缓冲区大小
                byte[] buf = new byte[1024];

                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("D:\\project\\java");
        list(file);

        SimpleDateFormat format = new SimpleDateFormat();
        String time = format.format(new Date());

        System.out.println(time);
    }
}
