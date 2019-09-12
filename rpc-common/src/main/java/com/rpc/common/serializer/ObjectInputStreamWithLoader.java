package com.rpc.common.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

/**
 * 反序列化中使用自定义加载的类
 * @author zhao
 *
 */
public class ObjectInputStreamWithLoader extends ObjectInputStream {

	private ClassLoader loader;

	public ObjectInputStreamWithLoader(InputStream in, ClassLoader loader)
	            throws IOException, StreamCorruptedException {
	
	        super(in);
	        if (loader == null) {
	            throw new IllegalArgumentException("Illegal null argument to ObjectInputStreamWithLoader");
	        }
	        this.loader = loader;
	    }

	/**
	 * Use the given ClassLoader rather than using the system class
	 */
	@SuppressWarnings("rawtypes")
	protected Class resolveClass(ObjectStreamClass classDesc) throws IOException, ClassNotFoundException {

		String cname = classDesc.getName();
		return loader.loadClass(cname);
	}

}
