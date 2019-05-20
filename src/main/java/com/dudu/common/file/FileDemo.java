/**
 * FileName: FileDemo
 * Author:   大橙子
 * Date:     2019/3/29 14:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author 大橙子
 * @create 2019/3/29
 * @since 1.0.0
 */
public class FileDemo {

    private static String path = "D:" + File.separatorChar
            + "Soft_Package" + File.separatorChar
            + "coverage" + File.separatorChar
            + "java";

    private static String fileName = "hello.txt";

    public static void main(String[] args) throws IOException {

        // "-6470234121391229264" -> "ExecutionData[name=org/apache/tomcat/util/bcel/classfile/AnnotationEntry, id=a6351c58ecdd36b0]"

        long a = -6470234121391229264L;

        System.out.println(a);

        // FileDemo demo = new FileDemo();
        //
        // // demo.bbb("");
        // File file = new File(path + File.separatorChar);
        //
        // System.out.println(file.exists());

    }

    public void aaa () throws IOException {

        File file = new File(path, fileName);

        File file2 = new File(path);
        File[] list = file2.listFiles();

        for (File s : list) {
            System.out.println(s);
        }
    }

    /**
     * 字节流
     * 读文件内容
     * */
    public void bbb (String status) throws IOException {

        if (StringUtils.isBlank(status)) {
            System.out.println("系统错误, status不能为空");
            return;
        }

        File f1 = new File(path, fileName);
        File f2 = new File(path, "c.txt");

        if (!f1.exists()) {
            System.out.println("source 文件不存在");
            return;
        }

        InputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];

        while (true) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            out.write(buf, 0, len);
        }

        out.close();
        in.close();
    }
}
