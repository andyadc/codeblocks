package com.andyadc.codeblocks.serialization.binary;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.nustaq.serialization.FSTConfiguration;

/**
 * @author andaicheng
 * @version 2017/2/13
 */
public class FSTSerializerFactory {

    // 官方文档说，FSTConfiguration太重了，需要通过ThreadLocal或者静态对象去维护它
    private static FSTConfiguration fstConfiguration;
    private static ObjectPool<FSTConfiguration> fstConfigurationObjectPool;
	private static final boolean usePool = false;

    public static void initialize() {
        if (usePool) {
            createPool();
        } else {
            fstConfiguration = createFstConfiguratio();
        }
    }

    public static ObjectPool<FSTConfiguration> createPool() {
        new GenericObjectPool<>(new PooledObjectFactory<FSTConfiguration>() {
            @Override
            public PooledObject<FSTConfiguration> makeObject() {
                return null;
            }

            @Override
            public void destroyObject(PooledObject<FSTConfiguration> p) {

            }

            @Override
            public boolean validateObject(PooledObject<FSTConfiguration> p) {
                return false;
            }

            @Override
            public void activateObject(PooledObject<FSTConfiguration> p) {

            }

            @Override
            public void passivateObject(PooledObject<FSTConfiguration> p) {

            }
        });
        return null;
    }

    public static FSTConfiguration createFstConfiguratio() {
        return FSTConfiguration.createDefaultConfiguration();
    }

    public static ObjectPool<FSTConfiguration> getDefaultPool() {
        return fstConfigurationObjectPool;
    }

    public static FSTConfiguration getDefaultFstConfiguration() {
        if (usePool) {
            return getFstConfiguration(fstConfigurationObjectPool);
        } else {
            return fstConfiguration;
        }
    }

    public static FSTConfiguration getFstConfiguration(ObjectPool<FSTConfiguration> pool) {
        FSTConfiguration fstConfiguration = null;
        try {
            fstConfiguration = pool.borrowObject();
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            if (fstConfiguration != null) {
                try {
                    pool.returnObject(fstConfiguration);
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        return fstConfiguration;
    }
}
