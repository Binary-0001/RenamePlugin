package com.xyz.sdk.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author gavin
 * @date 2019/2/18
 * lifecycle class visitor
 */
public class RenameClassVisitor extends ClassVisitor implements Opcodes {

    private String newName;

    public RenameClassVisitor(ClassVisitor cv, String newName) {
        super(Opcodes.ASM5, cv);
        this.newName = newName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("new name : "+newName);
        super.visit(version, access, newName, signature, superName, interfaces);
    }
}
