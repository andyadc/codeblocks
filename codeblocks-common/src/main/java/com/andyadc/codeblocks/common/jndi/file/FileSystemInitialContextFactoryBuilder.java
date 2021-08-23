package com.andyadc.codeblocks.common.jndi.file;

import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import java.util.Hashtable;

/**
 * {@link InitialContextFactoryBuilder} implementation based on File System
 */
public class FileSystemInitialContextFactoryBuilder implements InitialContextFactoryBuilder {

	@Override
	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
		FileSystemInitialContextFactory initialContextFactory = new FileSystemInitialContextFactory();
		return initialContextFactory;
	}
}
