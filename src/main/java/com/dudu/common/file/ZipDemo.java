/**
 * FileName: ZipDemo
 * Author:   大橙子
 * Date:     2019/9/18 10:18
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 〈一句话功能简述〉<br>
 *
 * <p>
 * 程序实现了ZIP压缩[compression]
 * 大致功能包括用了多态，递归等JAVA核心技术，可以对单个文件和任意级联文件夹进行压缩和解压。 需在代码中自定义源输入路径和目标输出路径。
 * <p>
 *
 * @author 大橙子
 * @create 2019/9/18
 * @since 1.0.0
 */
public class ZipDemo {

    /**
     * 将 <code>inFile</code> 压缩, 输出 <code>outZip</code>
     *
     * @param outZip 压缩后输出的zip文件路径
     * @param inFile 需要进行压缩的文件或目录
     * @throws Exception
     */
    private void zip(String outZip, File inFile) throws Exception {

        System.out.println("压缩中...");

        try (ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outZip)))) {
            compress(zout, inFile, inFile.getName());
        }

        System.out.println("压缩完成");
    }

    /**
     * 创建压缩点
     *
     * @param zout   压缩文件输出流
     * @param inFile 需要进行压缩的文件或目录
     * @param base   当前文件路径
     * @throws IOException 创建压缩点失败或写入流错误时抛出异常
     */
    private void compress(ZipOutputStream zout, File inFile, String base) throws IOException {

        // 如果路径为目录
        if (inFile.isDirectory()) {
            // 取出文件夹中的文件
            File[] listFile = inFile.listFiles();

            // 文件夹为空, 创建一个压缩点. 不为空则递归调用compress, 对每一个文件进行压缩
            if (listFile == null || listFile.length == 0) {
                zout.putNextEntry(new ZipEntry(base + "/"));
            } else {
                for (File f : listFile) {
                    compress(zout, f, base + "/" + f.getName());
                }
            }

        } else {
            // 创建zip压缩进入点base
            zout.putNextEntry(new ZipEntry(base));

            try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(inFile))) {
                int len;
                while ((len = bin.read()) != -1) {
                    zout.write(len);
                }
            }
        }
    }

    /**
     * 将压缩包解压到指定路径
     *
     * @param zipPath
     * @param unzipPath
     * @throws Exception
     */
    public void unzip(String zipPath, String unzipPath) throws Exception {

        System.out.println("解压中...");

        try (ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipPath)))) {
            unzip(zin, unzipPath);
        }

        System.out.println("解压完成");
    }

    /**
     * 将压缩包解压到指定路径
     *
     * @param zin       压缩包流
     * @param unzipPath 解压到指定路径
     * @throws Exception
     */
    public void unzip(ZipInputStream zin, String unzipPath) throws Exception {

        ZipEntry entry;

        while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {

            File unzieFile = new File(unzipPath, entry.getName());

            if (!unzieFile.exists()) {
                new File(unzieFile.getParent()).mkdirs();
            }

            try (BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(unzieFile))) {
                int len;
                while ((len = zin.read()) != -1) {
                    bout.write(len);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        ZipDemo book = new ZipDemo();

        String zip = "D:\\aaa\\report.zip";

        book.zip(zip, new File("D:\\aaa\\report"));

        book.unzip(zip, "D:\\aaa\\bbb");
    }
}