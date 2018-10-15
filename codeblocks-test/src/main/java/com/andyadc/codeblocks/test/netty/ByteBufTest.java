package com.andyadc.codeblocks.test.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author andy.an
 * @since 2018/10/15
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);

        print("allocate ByteBuf(9, 100)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3});
        print("writeBytes(1,2,3)", buffer);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

    }

    private static void print(String action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        System.out.println("capacity: " + buffer.capacity());
        System.out.println("maxCapacity: " + buffer.maxCapacity());
        System.out.println("readerIndex: " + buffer.readerIndex());
        System.out.println("readableBytes: " + buffer.readableBytes());
        System.out.println("isReadable: " + buffer.isReadable());
        System.out.println("writerIndex: " + buffer.writerIndex());
        System.out.println("writableBytes: " + buffer.writableBytes());
        System.out.println("isWritable: " + buffer.isWritable());
        System.out.println("maxWritableBytes: " + buffer.maxWritableBytes());
        System.out.println();
    }
}
