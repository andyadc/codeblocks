package com.andyadc.codeblocks.serialization.binary;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class KryoSerializerFactory {

	// TODO
	private static final Class<?>[] classArray = {
		Date.class,
		LocalDate.class,
		LocalTime.class,
		LocalDateTime.class,
		ArrayList.class,
		HashMap.class,
	};

	private static final int MAX_DEPTH = 1024;

	// Pool constructor arguments: thread safe, soft references, maximum capacity
	private static final Pool<Kryo> kryoPool = new Pool<Kryo>(true, false, 8) {
		@Override
		protected Kryo create() {
			Kryo kryo = new Kryo();
			// Configure the Kryo instance.
			for (Class<?> clazz : classArray) {
				kryo.register(clazz);
			}
			kryo.setAutoReset(true);
			kryo.setMaxDepth(MAX_DEPTH);
			return kryo;
		}
	};
	private static final int BUFFER_SIZE = 2 * 1024;
	private static final int MAX_BUFFER_SIZE = 512 * 1024;
	private static final Pool<Output> outputPool = new Pool<Output>(true, false, 16) {
		protected Output create() {
			return new Output(BUFFER_SIZE, MAX_BUFFER_SIZE);
		}
	};
	private static final Pool<Input> inputPool = new Pool<Input>(true, false, 16) {
		protected Input create() {
			return new Input();
		}
	};

	public static Pool<Kryo> getKryoPool() {
		return kryoPool;
	}

	public static Pool<Output> getOutputPool() {
		return outputPool;
	}

	public static Pool<Input> getInputPool() {
		return inputPool;
	}
}
