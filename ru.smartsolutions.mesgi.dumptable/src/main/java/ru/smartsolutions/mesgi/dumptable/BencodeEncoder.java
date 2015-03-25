package ru.smartsolutions.mesgi.dumptable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
*
* @author Ryan Greene
*
*/
public final class BencodeEncoder {

	@SuppressWarnings("unchecked")
	public String encode(final Object object) {
		if (object instanceof String) {
			return encodeString((String) object);
		} else if (object instanceof Integer) {
			return encodeInteger((Integer) object);
		} else if (object instanceof List) {
			return encodeList((List<Object>) object);
		} else if (object instanceof Map) {
			return encodeMap((Map<String, Object>) object);
		}
		throw new IllegalStateException("the object " + object + " is not a type that can be encoded in bencode");
	}

	public String encodeString(final String string) {
		return new StringBuffer().append(string.length()).append(':').append(string).toString();
	}

	public String encodeInteger(final int integer) {
		return new StringBuffer().append('i').append(integer).append('e').toString();
	}

	public String encodeList(final List<Object> list) {
		final StringBuffer stringBuffer = new StringBuffer().append('l');
		for (final Object object : list) {
			stringBuffer.append(encode(object));
		}
		return stringBuffer.append('e').toString();
	}

	public String encodeMap(final Map<String, Object> map) {
		final StringBuffer stringBuffer = new StringBuffer().append('d');
		final Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry<String, Object> entry = iterator.next();
			stringBuffer.append(encodeString(entry.getKey())).append(encode(entry.getValue()));	
			iterator.remove();
		}
		return stringBuffer.append('e').toString();
	}
}