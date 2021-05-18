package com.andyadc.codeblocks.test.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class AddOperationAsm2 extends ClassLoader {

	public static void main(String[] args) throws Exception {
		byte[] bytes = generate();
		// 输出字节码
		outputClazz(bytes);
		AddOperationAsm2 instance = new AddOperationAsm2();
		Class<?> clazz = instance.defineClass("com.andyadc.codeblocks.test.asm.AddOperationAsm", bytes, 0, bytes.length);
		Method method = clazz.getMethod("sum", int.class, int.class);
		Object result = method.invoke(clazz.newInstance(), 1, 11);
		System.out.println(result);
	}

	private static void outputClazz(byte[] bytes) {
		try (FileOutputStream outputStream = new FileOutputStream("AddOperationAsm.class")) {
			System.out.println("ASM类输出路径: " + (new File("")).getAbsolutePath());
			outputStream.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * package com.andyadc.codeblocks.test.asm;
	 * public class AddOperationAsm {
	 * public AddOperationAsm() {
	 * }
	 * public int sum(int i, int j) {
	 * return i + j;
	 * }
	 * }
	 * </p>
	 */
	private static byte[] generate() {
		ClassWriter classWriter = new ClassWriter(0);
		{
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			methodVisitor.visitInsn(Opcodes.RETURN);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
		}

		{
			// 定义对象头；版本号、修饰符、全类名、签名、父类、实现的接口
			classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/andyadc/codeblocks/test/asm/AddOperationAsm", null, "java/lang/Object", null);
			// 添加方法；修饰符、方法名、描述符、签名、异常
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "sum", "(II)I", null, null);
			methodVisitor.visitVarInsn(Opcodes.ILOAD, 1);
			methodVisitor.visitVarInsn(Opcodes.ILOAD, 2);
			methodVisitor.visitInsn(Opcodes.IADD);
			// 返回
			methodVisitor.visitInsn(Opcodes.IRETURN);
			// 设置操作数栈的深度和局部变量的大小
			methodVisitor.visitMaxs(2, 3);
			methodVisitor.visitEnd();
		}

		classWriter.visitEnd();
		return classWriter.toByteArray();
	}

	public int sum(int i, int j) {
		return i + j;
	}
}
