/**
 * FileName: ExecutionDataClient
 * Author:   大橙子
 * Date:     2019/4/10 10:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.dudu.common.file;

import com.dudu.common.exception.BusinessException;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.runtime.RemoteControlReader;
import org.jacoco.core.runtime.RemoteControlWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * <p>
 *     tcpClient 获取覆盖率代码
 * </p>
 *
 * @author 大橙子
 * @date 2019/4/10
 * @since 1.0.0
 */
public class ExecutionDataClient {



    /**
     * Starts the execution data request.
     *
     * @param args
     */
    public static void main(final String[] args) {

        ExecutionDataClient client = new ExecutionDataClient();

        String destFile = "D:\\Soft_Package\\coverage\\demo-1.0\\jacoco-client1.exec";
        String address = "127.0.0.1";
        int port = 4399;

        String projectDirectory = "D:\\Soft_Package\\coverage\\demo-1.0";
        String executionDataFile = "jacoco-client.exec";
        String classesDirectory = "target\\classes";
        String sourceDirectory = "src\\main\\java";
        String reportDirectory = "report";

        // client.getExecFile(destFile, address, port);
        // client.getReportFile(projectDirectory, executionDataFile, classesDirectory, sourceDirectory, reportDirectory);

        List<JacocoReport> list = new ArrayList<>();

        JacocoReport jacocoReport = new JacocoReport();
        jacocoReport.setTitle("第一次");
        jacocoReport.setProjectDirectory(projectDirectory);
        jacocoReport.setExecutionDataFile(executionDataFile);
        jacocoReport.setClassDirectory(classesDirectory);
        jacocoReport.setSourceDirectory(sourceDirectory);
        jacocoReport.setReportDirectory(reportDirectory);

        JacocoReport jacocoReport2 = new JacocoReport();
        jacocoReport2.setTitle("第二次");
        jacocoReport2.setProjectDirectory(projectDirectory);
        jacocoReport2.setExecutionDataFile("jacoco-client1.exec");
        jacocoReport2.setClassDirectory(classesDirectory);
        jacocoReport2.setSourceDirectory(sourceDirectory);
        jacocoReport2.setReportDirectory(reportDirectory + "1");

        list.add(jacocoReport);
        list.add(jacocoReport2);

        CoverageReport coverageReport = new CoverageReport();

        coverageReport.create(list, "demo");
    }

    private void getExecFile(List<JacocoReport> jacocoReportList) {

        jacocoReportList.forEach(JacocoReport::validate);

        long count = jacocoReportList.parallelStream().map(jacocoReport -> {

            try {
                FileOutputStream localFile = new FileOutputStream(jacocoReport.getRepo());
                ExecutionDataWriter localWriter = new ExecutionDataWriter(localFile);

                Socket socket = new Socket(InetAddress.getByName(jacocoReport.getAddress()), Integer.parseInt(jacocoReport.getPort()));

                RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
                RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());

                reader.setSessionInfoVisitor(localWriter);
                reader.setExecutionDataVisitor(localWriter);

                writer.visitDumpCommand(true, true);

                if (!reader.read()) {
                    throw new BusinessException("[coverage] socket connection exception by userId [ "+ jacocoReport.getUserId() +" ]");
                }
                socket.close();
                localFile.close();
            } catch (IOException e) {
                throw new BusinessException("[coverage] exec file generation failed by userId [ "+ jacocoReport.getUserId() +" ]", e);
            }
            return null;
        }).count();

    }

    public void getReportFile(String projectDirectory, String executionDataFile, String classesDirectory, String sourceDirectory, String reportDirectory){
        try {

            ReportGenerator generator = new ReportGenerator(projectDirectory, executionDataFile, classesDirectory, sourceDirectory, reportDirectory);

            generator.create();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("获取覆盖率文件成功");
    }


}
