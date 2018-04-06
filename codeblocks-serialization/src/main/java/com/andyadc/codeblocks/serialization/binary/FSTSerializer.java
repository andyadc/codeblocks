package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import org.apache.commons.lang3.ArrayUtils;
import org.nustaq.serialization.FSTConfiguration;

/**
 * @author andaicheng
 * @version 2017/2/13
 */
public class FSTSerializer {

    public static <T> byte[] serialize(T object) {
        FSTConfiguration fstConfiguration = FSTSerializerFactory.getDefaultFstConfiguration();
        return serialize(fstConfiguration, object);
    }

    public static <T> byte[] serialize(FSTConfiguration fst, T object) {
        if (object == null) {
            throw new SerializerException("Object is null");
        }
        return fst.asByteArray(object);
    }

    public static <T> T deserialize(byte[] bytes) {
        FSTConfiguration fstConfiguration = FSTSerializerFactory.getDefaultFstConfiguration();
        return deserialize(fstConfiguration, bytes);
    }

    public static <T> T deserialize(FSTConfiguration fst, byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            throw new SerializerException("Bytes is null or empty");
        }
        return (T) fst.asObject(bytes);
    }
}
