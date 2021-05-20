package com.andyadc.codeblocks.test.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;

public class MonitorTest extends ClassLoader {

	public static void main(String[] args) throws Exception {
		ClassReader classReader = new ClassReader(Monitor.class.getName());
		ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

		{
			MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			methodVisitor.visitInsn(Opcodes.RETURN);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
		}

		{

		}

		ClassVisitor visitor = new ProfilingClassAdapter(classWriter, Monitor.class.getSimpleName());

		classReader.accept(visitor, ClassReader.EXPAND_FRAMES);

		byte[] bytes = classWriter.toByteArray();

		Class<?> clazz = new MonitorTest().defineClass("com.andyadc.codeblocks.test.asm.Monitor", bytes, 0, bytes.length);

		Method queryUserInfo = clazz.getMethod("look", String.class);
		Object obj = queryUserInfo.invoke(clazz.newInstance(), "cat");
		System.out.println("测试结果：" + obj);
	}

	static class ProfilingClassAdapter extends ClassVisitor {

		public ProfilingClassAdapter(ClassVisitor cv, String innerClassName) {
			super(Opcodes.ASM5, cv);
		}

		public MethodVisitor visitMethod(int access,
										 String name,
										 String desc,
										 String signature,
										 String[] exceptions) {
			System.out.println("access：" + access);
			System.out.println("name：" + name);
			System.out.println("desc：" + desc);

			if (!"look".equals(name)) {
				return null;
			}

			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			return new ProfilingMethodVisitor(mv, access, name, desc);
		}
	}

	static class ProfilingMethodVisitor extends AdviceAdapter {

		private String methodName = "";

		protected ProfilingMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
			super(Opcodes.ASM5, methodVisitor, access, name, descriptor);
			this.methodName = name;
		}

		@Override
		protected void onMethodEnter() {
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
			mv.visitVarInsn(Opcodes.LSTORE, 2);
			mv.visitVarInsn(Opcodes.ALOAD, 1);
		}

		@Override
		protected void onMethodExit(int opcode) {
			if ((Opcodes.IRETURN <= opcode && opcode <= Opcodes.RETURN) || (opcode == Opcodes.ATHROW)) {
				mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

				mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
				mv.visitInsn(DUP);
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
				mv.visitLdcInsn("方法执行耗时(纳秒)->" + methodName + "：");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

				mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
				mv.visitVarInsn(LLOAD, 2);
				mv.visitInsn(LSUB);

				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			}
		}
	}
}
