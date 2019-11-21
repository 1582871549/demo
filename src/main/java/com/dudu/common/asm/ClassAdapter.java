package com.dudu.common.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author 大橙子
 * @create 2019/11/4
 * @since 1.0.0
 */
// ClassAdapter：
//     ClassAdapter 是 ClassVisitor 的实现类。它的构造方法中需要一个 ClassVisitor 对象，
//     并保存为字段 protected ClassVisitor cv。在它的实现中，
//     每个方法都是原封不动的直接调用 cv 的对应方法，并传递同样的参数。
//     可以通过继承 ClassAdapter 并修改其中的部分方法达到过滤的作用。它可以看做是事件的过滤器。
public class ClassAdapter extends ClassVisitor implements Opcodes {

    private String owner;
    private boolean isInterface;

    public ClassAdapter(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    /**
     * 访问类的声明部分
     *
     * @param version    class 文件的版本号，这就是我们需要修改的内容
     * @param access     该类的访问级别
     * @param name       该类的内部名称
     * @param signature  该类的签名，如果该类与泛型无关，这个参数就是 null
     * @param superName  父类的内部名称
     * @param interfaces 该类实现的接口的内部名称
     */
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {

        // version      52
        // access       33
        // name         com/dudu/common/configuration/example/impl/SecondarySchoolServiceImpl
        // signature    null
        // superName    java/lang/Object
        // interfaces   [com/dudu/common/configuration/example/SchoolService]

        System.out.println("version " + version);
        System.out.println("access " + access);
        System.out.println("name " + name);
        System.out.println("signature " + signature);
        System.out.println("superName " + superName);
        System.out.println("interfaces " + Arrays.toString(interfaces));

        cv.visit(version, access, name, signature, superName, interfaces);

        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    /**
     * 此方法每次调用时都必须返回一个新的{@link MethodVisitor}实例(或{null})，即不应该返回以前返回的实例
     *
     * @param access     方法的访问标志， 此参数指示该方法是合成的和 / 或是不推荐使用的
     * @param name       方法的名称
     * @param desc       方法的描述符
     * @param signature  方法的签名。如果方法参数、返回类型和异常不使用泛型类型，则可能为null。
     * @param exceptions 方法异常类的内部名称 。可能为空
     * @return 访问方法字节代码的对象，如果该类访问者对访问该方法的代码没有关系，则为null。
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {


        return super.visitMethod(access, name, desc, signature, exceptions);
    }


}


