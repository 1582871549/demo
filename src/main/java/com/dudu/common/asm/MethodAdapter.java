package com.dudu.common.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author 大橙子
 * @create 2019/11/4
 * @since 1.0.0
 */
public class MethodAdapter extends MethodVisitor implements Opcodes {

    protected String className = null;
    protected int access = -1;
    protected String name = null;
    protected String desc = null;
    protected String signature = null;
    protected String[] exceptions = null;

    public MethodAdapter(final MethodVisitor mv,
                         final String className,
                         final int access,
                         final String name,
                         final String desc,
                         final String signature,
                         final String[] exceptions) {

        super(ASM7, mv);
        this.className = className;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }


    /**
     * 当开始访问一个方法时，会调用visitCode方法，
     * 我们在这里加入一段输出“方法开始信息”的代码，当访问每个操作前会调用visitInsn，
     * 我们获取它的操作类型，如果是返回类型或者是抛出异常，则输出方法结束的信息。
     */
    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("CALL classname:"+ className+ " access"+access+" name:" + name +" desc"+desc+" singature:"+signature);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("CALL classname:"+ className+ " access"+access+" name:" + name +" desc"+desc+" singature:"+signature);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }

}
