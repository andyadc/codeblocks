package com.andyadc.codeblocks.test.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class AddOperationAsm extends ClassLoader {

	public static void main(String[] args) throws Exception {
		byte[] bytes = generate();
		// 输出字节码
		outputClazz(bytes);
		// 加载AddOperationAsm
		Class<?> clazz = new AddOperationAsm().defineClass("com.andyadc.codeblocks.test.asm.AddOperationAsm", bytes, 0, bytes.length);
		// 反射获取 main 方法
		Method main = clazz.getMethod("main", String[].class);
		main.invoke(null, new Object[]{new String[]{}});
	}

	private static void outputClazz(byte[] bytes) {
		try (FileOutputStream outputStream = new FileOutputStream("AddOperationAsm.class")) {
			System.out.println("ASM类输出路径: " + (new File("")).getAbsolutePath());
			outputStream.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte[] generate() {
		ClassWriter classWriter = new ClassWriter(0);
		{
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitCode();
			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			methodVisitor.visitInsn(Opcodes.RETURN);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
		}

		{
			classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/andyadc/codeblocks/test/asm/AddOperationAsm", null, "java/lang/Object", null);
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
			methodVisitor.visitTypeInsn(Opcodes.NEW, "com/andyadc/codeblocks/test/asm/AddOperationAsm");
			methodVisitor.visitInsn(Opcodes.DUP);
			methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/andyadc/codeblocks/test/asm/AddOperationAsm", "<init>", "()V");
			methodVisitor.visitInsn(Opcodes.ICONST_1);
			methodVisitor.visitInsn(Opcodes.ICONST_5);
			methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/andyadc/codeblocks/test/asm/AddOperationAsm", "sum", "(II)I");
			methodVisitor.visitVarInsn(Opcodes.ISTORE, 1);
			methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
			methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
			methodVisitor.visitInsn(Opcodes.RETURN);

			methodVisitor.visitMaxs(3, 2);
			methodVisitor.visitEnd();
		}

		{
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sum", "(II)I", null, null);
			methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
			methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
			methodVisitor.visitInsn(Opcodes.IADD);
			methodVisitor.visitInsn(Opcodes.IRETURN);
			methodVisitor.visitMaxs(2, 3);
			methodVisitor.visitEnd();
		}

		classWriter.visitEnd();
		return classWriter.toByteArray();
	}
}
