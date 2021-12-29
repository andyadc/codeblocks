package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import org.nustaq.serialization.FSTConfiguration;

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

    @SuppressWarnings({"unchecked"})
    public static <T> T deserialize(FSTConfiguration fst, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
			throw new SerializerException("Bytes is null or empty");
		}
        return (T) fst.asObject(bytes);
    }
}
