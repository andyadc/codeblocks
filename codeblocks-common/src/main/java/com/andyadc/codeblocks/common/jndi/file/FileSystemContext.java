package com.andyadc.codeblocks.common.jndi.file;

import com.andyadc.codeblocks.common.function.ThrowableAction;
import com.andyadc.codeblocks.common.function.ThrowableSupplier;
import com.andyadc.codeblocks.common.io.Deserializer;
import com.andyadc.codeblocks.common.io.Deserializers;
import com.andyadc.codeblocks.common.io.Serializer;
import com.andyadc.codeblocks.common.io.Serializers;
import org.apache.commons.io.FileUtils;

import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * {@link Context} implementation based on File System.
 */
class FileSystemContext implements Context {

	private static final String CONTEXT_NAME_PREFIX = "java:";

	private static final String ENV_CONTEXT_NAME = "java:comp/env";

	private static final Serializers serializers = new Serializers();

	private static final Deserializers deserializers = new Deserializers();

	static {
		serializers.loadSPI();
		deserializers.loadSPI();
	}

	private final File rootDirectory;

	private final Hashtable<Object, Object> environment;

	FileSystemContext(Hashtable<?, ?> environment) {
		this(getRootDirectory(environment), environment);
	}

	FileSystemContext(File rootDirectory, Hashtable<?, ?> environment) {
		this.rootDirectory = rootDirectory;
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}
		this.environment = (Hashtable<Object, Object>) environment.clone();
	}

	private static File getRootDirectory(Map<?, ?> environment) {
		final File rootDirectory;
		String dirPath = (String) environment.get("jndi.file.root.dir.path");
		if (dirPath != null) {
			rootDirectory = new File(dirPath);
		} else {
			rootDirectory = new File(System.getProperty("java.io.tmpdir"), ".jndi");
		}
		return rootDirectory;
	}

	@Override
	public Object lookup(Name name) throws NamingException {
		return lookup(name.toString());
	}

	@Override
	public Object lookup(String name) throws NamingException {
		int index = name.indexOf(CONTEXT_NAME_PREFIX);

		String targetName = name;
		if (index > -1) { // "java:" prefix was found
			targetName = name.substring(CONTEXT_NAME_PREFIX.length());
		}

		File targetFile = targetFile(targetName);

		if (ENV_CONTEXT_NAME.equals(name) || targetFile.isDirectory()) {
			return createSubContext(targetFile);
		} else if (targetFile.exists()) {
			return lookup(targetFile);
		}
//        throw new UnsupportedOperationException("The target [path : " + targetFile.getAbsolutePath()
//                + "] that is not a file or directory FileSystemContext can't support");
		return null;
	}

	private Object lookup(File targetFile) throws NamingException {
		return ThrowableSupplier.execute(() -> {
			byte[] bytes = readFileToByteArray(targetFile);
			Deserializer deserializer = deserializers.getMostCompatible(String.class);
			return deserializer.deserialize(bytes);
		}, NamingException.class);
	}

	@Override
	public void bind(Name name, Object obj) throws NamingException {
		bind(name.toString(), obj);
	}

	@Override
	public void bind(String name, Object obj) throws NamingException {
		bind(name, obj, false);
	}

	@Override
	public void rebind(Name name, Object obj) throws NamingException {
		rebind(name.toString(), obj);
	}

	@Override
	public void rebind(String name, Object obj) throws NamingException {
		bind(name, obj, true);
	}

	private void bind(String name, Object obj, boolean override) throws NamingException {
		File targetFile = targetFile(name);
		if (override && !targetFile.exists()) {
			return;
		}
		ThrowableAction.execute(() -> {
			Class<?> objClass = obj.getClass();
			Serializer serializer = serializers.getMostCompatible(objClass);
			byte[] bytes = serializer.serialize(obj);
			FileUtils.writeByteArrayToFile(targetFile, bytes);
		}, NamingException.class);
	}

	@Override
	public void unbind(Name name) throws NamingException {
		unbind(name.toString());
	}

	@Override
	public void unbind(String name) throws NamingException {
		File targetFile = targetFile(name);
		FileUtils.deleteQuietly(targetFile);
	}

	@Override
	public void rename(Name oldName, Name newName) throws NamingException {
		rename(oldName.toString(), newName.toString());
	}

	@Override
	public void rename(String oldName, String newName) throws NamingException {
		File targetFile = targetFile(oldName);
		targetFile.renameTo(targetFile(newName));
	}

	@Override
	public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
		return list(name.toString());
	}

	@Override
	public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
		File targetFile = targetFile(name);
		if (targetFile.exists() && targetFile.isDirectory()) {

		}
		return null;
	}

	@Override
	public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
		return listBindings(name.toString());
	}

	@Override
	public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
		return null;
	}

	@Override
	public void destroySubcontext(Name name) throws NamingException {
		destroySubcontext(name.toString());
	}

	@Override
	public void destroySubcontext(String name) throws NamingException {
		FileSystemContext context = (FileSystemContext) createSubcontext(name);
		context.close();
		FileUtils.deleteQuietly(context.rootDirectory);
	}

	@Override
	public Context createSubcontext(Name name) throws NamingException {
		return createSubcontext(name.toString());
	}

	@Override
	public Context createSubcontext(String name) throws NamingException {
		File targetFile = targetFile(name);
		return createSubContext(targetFile);
	}

	@Override
	public Object lookupLink(Name name) throws NamingException {
		return null;
	}

	@Override
	public Object lookupLink(String name) throws NamingException {
		return null;
	}

	@Override
	public NameParser getNameParser(Name name) throws NamingException {
		return getNameParser(name.toString());
	}

	@Override
	public NameParser getNameParser(String name) throws NamingException {
		return CompositeName::new;
	}

	@Override
	public Name composeName(Name name, Name prefix) throws NamingException {
		return null;
	}

	@Override
	public String composeName(String name, String prefix) throws NamingException {
		return null;
	}

	@Override
	public Object addToEnvironment(String propName, Object propVal) throws NamingException {
		return environment.put(propName, propVal);
	}

	@Override
	public Object removeFromEnvironment(String propName) throws NamingException {
		return environment.remove(propName);
	}

	@Override
	public Hashtable<?, ?> getEnvironment() throws NamingException {
		return environment;
	}

	@Override
	public void close() throws NamingException {
		this.environment.clear();
	}

	@Override
	public String getNameInNamespace() throws NamingException {
		return null;
	}

	private File targetFile(String targetName) {
		return new File(rootDirectory, targetName);
	}

	private FileSystemContext createSubContext(File targetFile) {
		return new FileSystemContext(targetFile, this.environment);
	}
}
