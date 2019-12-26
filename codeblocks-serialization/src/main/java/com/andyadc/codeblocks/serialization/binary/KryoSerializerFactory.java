package com.andyadc.codeblocks.serialization.binary;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;

/**
 * andy.an
 * 2019/12/26
 */
public class KryoSerializerFactory {

	private static final int MAX_DEPTH = 1024;

	private static KryoPool pool;

	public static void initialize() {
		pool = creatPool(MAX_DEPTH);
	}

	private static KryoPool creatPool(final int maxDepth) {
		return new KryoPool.Builder(() ->
			createKryo(maxDepth)
		).softReferences().build();
	}

	public static Kryo createKryo() {
		return createKryo(MAX_DEPTH);
	}

	private static Kryo createKryo(int maxDepth) {
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.setMaxDepth(maxDepth);
		kryo.setAutoReset(true);
//		kryo.setAsmEnabled(true);
		kryo.getFieldSerializerConfig().setUseAsm(true);

		return kryo;
	}

	public static KryoPool getDefaultPool() {
		return pool;
	}
}
