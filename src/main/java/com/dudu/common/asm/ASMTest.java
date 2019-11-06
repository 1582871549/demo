package com.dudu.common.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 大橙子
 * @create 2019/11/1
 * @since 1.0.0
 */
public class ASMTest {

    // ASM 提供了三个基于 ClassVisitor 接口的类来实现 class 文件的生成和转换：
    //
    // ClassReader：
    //     ClassReader 解析一个类的 class 字节码，该类的 accept 方法接受一个 ClassVisitor 的对象，
    //     在 accept 方法中，会按上文描述的顺序逐个调用 ClassVisitor 对象的方法。它可以被看做事件的生产者。
    // ClassAdapter：
    //     ClassAdapter 是 ClassVisitor 的实现类。它的构造方法中需要一个 ClassVisitor 对象，
    //     并保存为字段 protected ClassVisitor cv。在它的实现中，
    //     每个方法都是原封不动的直接调用 cv 的对应方法，并传递同样的参数。
    //     可以通过继承 ClassAdapter 并修改其中的部分方法达到过滤的作用。它可以看做是事件的过滤器。
    // ClassWriter：
    //     ClassWriter 也是 ClassVisitor 的实现类。
    //     ClassWriter 可以用来以二进制的方式创建一个类的字节码。
    //     对于 ClassWriter 的每个方法的调用会创建类的相应部分。
    //     例如：调用 visit 方法就是创建一个类的声明部分，每调用一次 visitMethod
    //     方法就会在这个类中创建一个新的方法。在调用 visitEnd 方法后即表明该类的创建已经完成。
    //     它最终生成一个字节数组，这个字节数组中包含了一个类的 class 文件的完整字节码内容 。
    //     可以通过 toByteArray 方法获取生成的字节数组。ClassWriter 可以看做事件的消费者。

    public static void main(String[] args) throws IOException {

        String className = "com.dudu.common.file.JsonDemo2";

        ASMTest test = new ASMTest();

        test.aa(className);
    }

    private void aa(String className) throws IOException {

        ClassReader classReader = new ClassReader(className);
        ClassWriter classWriter = new ClassWriter(0);

        ClassAdapter ca = new ClassAdapter(classWriter);

        classReader.accept(ca, 0);



        byte[] bytes = classWriter.toByteArray();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(bytes);

        System.out.println(stream.toString());
    }


}
