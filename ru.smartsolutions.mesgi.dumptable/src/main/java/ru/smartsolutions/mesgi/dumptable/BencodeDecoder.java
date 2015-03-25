package ru.smartsolutions.mesgi.dumptable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ryan Greene
 *
 */
public final class BencodeDecoder {

	private static final Charset CHARSET = Charset.forName("US-ASCII");

	private final byte[] buffer;

	private int pos = 0;

	public BencodeDecoder(final InputStream inputStream) throws IOException {
		buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
	}

	public BencodeDecoder(final String string) {
		buffer = string.getBytes(CHARSET);
	}

	public Object read() {
		if (buffer[pos] >= '0' && buffer[pos] <= '9') {
			return readString();
		} else if (buffer[pos] == 'i') {
			return readInteger();
		} else if (buffer[pos] == 'l') {
			return readList();
		} else if (buffer[pos] == 'd') {
			return readMap();
		}
		throw new IllegalStateException("the byte " + buffer[pos] + " (char " + (char) buffer[pos] + ") is not a valid key");
	}

	public String readString() {
		final int start = pos++;
		while (buffer[pos++] != ':') {
		}
		final String string = new String(buffer, pos, Integer.parseInt(new String(buffer, start, pos - start - 1, CHARSET)), CHARSET);
		pos += string.length();
		return string;
	}

	public long readInteger() {
		final int start = ++pos;
		while (buffer[pos] != 'e') {
			pos++;
		}
		return Long.parseLong(new String(buffer, start, pos++ - start, CHARSET));
	}

	public List<Object> readList() {
		final List<Object> list = new ArrayList<Object>();
		pos++;
		while (buffer[pos] != 'e') {
			final Object object = read();
			list.add(object);
		}
		pos++;
		return list;
	}

	public Map<String, Object> readMap() {
		final Map<String, Object> map = new HashMap<String, Object>();
		pos++;
		while (buffer[pos] != 'e') {
			map.put(readString(), read());
		}
		pos++;
		return map;
	}
}