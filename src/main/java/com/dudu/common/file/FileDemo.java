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

import com.dudu.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;

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

        String destFile = "D:\\Soft_Package\\coverage\\demo-1.0\\jacoco-client1.exec";
        String address = "127.0.0.1";
        int port = 4399;

        boolean b = new File("D:\\bbb\\").exists();
        System.out.println(b);
    }

    public void save(String path) {

        try {

            File file = new File(path, "");
            save(file, true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(final File file, final boolean append) throws IOException {

        // 如果拥有父目录、则创建父目录。没有则返回null
        final File folder = file.getParentFile();
        if (folder != null) {
            folder.mkdirs();
        }

        // 创建文件输出流，以写入由指定的file。
        // 如果第二个参数为true, 那么字节将被写入文件的末尾而不是开头。
        final FileOutputStream fileStream = new FileOutputStream(file, append);
        // 避免来自其他进程的并发写入
        fileStream.getChannel().lock();
        // 将数据写入指定的缓冲输出流
        final OutputStream bufferedStream = new BufferedOutputStream(fileStream);

        Socket socket = null;

        try {

            final ExecutionDataWriter localWriter = new ExecutionDataWriter(bufferedStream);

            socket = new Socket(InetAddress.getByName(""), 1);

            ExecFileLoader loader = new ExecFileLoader();

            loader.load(socket.getInputStream());

            ExecutionDataStore dataStore = loader.getExecutionDataStore();

            System.out.println(dataStore);

            // 写
            RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
            // 读 ExecutionDataReader
            RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());

            reader.setSessionInfoVisitor(localWriter);
            reader.setExecutionDataVisitor(localWriter);

            // 读取 reader 中的数据到localWriter中
            boolean read = reader.read();

            // 是否重置服务器探针信息
            writer.visitDumpCommand(true, true);

            if (!read) {
                System.out.println("socket 连接异常");
            }


        } catch (IOException e) {
            System.out.println("系统异常!");
        } finally {
            socket.close();
            bufferedStream.close();
        }
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
