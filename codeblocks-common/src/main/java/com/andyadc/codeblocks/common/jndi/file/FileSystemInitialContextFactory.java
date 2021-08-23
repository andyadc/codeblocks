package com.andyadc.codeblocks.common.jndi.file;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * {@link InitialContextFactory} implementation based on File System.
 */
public class FileSystemInitialContextFactory implements InitialContextFactory {

	@Override
	public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
		return new FileSystemContext(environment);
	}
}
