package com.andyadc.codeblocks.test.netty.im.serialize;

/**
 * @author andy.an
 * @since 2018/10/16
 */
public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    interface SerializerAlgorithm {

        /**
         * json 序列化标识
         */
        byte JSON = 1;
    }
}
