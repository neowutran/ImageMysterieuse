package library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import biz.source_code.base64Coder.Base64Coder;

/**
 * Cette classe permet la serialization d'objet en string (Et inversement)
 * 
 * @author Cibert Gauthier
 * @author Martini Didier
 * @author Responsable: Martini Didier
 * @version 3.0
 */
public final class ObjectString {

	/**
	 * Read the object from Base64 string.
	 * 
	 * @param s
	 * @return Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object fromString(final String s) throws IOException,
					ClassNotFoundException {

		final byte[] data = Base64Coder.decode(s);
		final ObjectInputStream ois = new ObjectInputStream(
						new ByteArrayInputStream(data));
		final Object o = ois.readObject();
		ois.close();
		return o;
	}

	/**
	 * Write the object to a Base64 string.
	 * 
	 * @param o
	 * @return String
	 * @throws IOException
	 */
	public static String toString(final Serializable o) throws IOException {

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}
}