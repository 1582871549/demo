package com.dudu.common.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author 大橙子
 * @create 2019/11/1
 * @since 1.0.0
 */
public class Demo {

    // public interface com.dudu.asm.Comparable extends com.dudu.asm.Mesurable {
    //
    //     public static final int LESS;
    //
    //     public static final int EQUAL;
    //
    //     public static final int GREATER;
    //
    //     public abstract int compareTo (java.lang.Object);
    // }
    public static void main(String[] args) throws IOException {


        //生成一个类只需要ClassWriter组件即可
        ClassWriter cw = new ClassWriter(0);

        //通过visit方法确定类的头部信息
        cw.visit(Opcodes.V1_8,
                Opcodes.ACC_PUBLIC,
                "com/dudu/coverage/service/impl/StudentServiceImpl",
                null,
                "java/lang/Object",
                new String[]{"com/dudu/coverage/service/StudentService"});

        // access       2
        // name         sex
        // desc         Ljava/lang/String;
        // signature    null
        // value        null
        // -------------------------------------------------------------------------
        // access       10
        // name         list
        // desc         Ljava/util/List;
        // signature    Ljava/util/List<Ljava/lang/String;>;
        // value        null

        //定义类的属性
        cw.visitField(
                Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL,
                "name",
                "Ljava/lang/String;",
                null,
                null).visitEnd();

        cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL,
                "list",
                "Ljava/util/List;",
                "Ljava/util/List<Ljava/lang/String;>;",
                null).visitEnd();

        // 定义类的方法
        // cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
        //         "study",
        //         "Ljava/util/List;",
        //         "Ljava/util/List<Ljava/lang/String;>;",
        //         null).visitEnd();

        cw.visitEnd(); //使cw类已经完成
        //将cw转换成字节数组写到文件里面去

        byte[] data = cw.toByteArray();

        File file = new File("D:\\aaa\\Student.class");
        FileOutputStream fout = new FileOutputStream(file);

        fout.write(data);
        fout.close();
    }
}
