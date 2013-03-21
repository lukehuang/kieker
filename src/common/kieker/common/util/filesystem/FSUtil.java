/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.common.util.filesystem;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public final class FSUtil { // NOCS NOPMD (constants interface)

	public static final String FILE_PREFIX = "kieker";
	public static final String MAP_FILENAME = "kieker.map";

	public static final String LEGACY_FILE_PREFIX = "tpmon";
	public static final String LEGACY_MAP_FILENAME = "tpmon.map";

	public static final String NORMAL_FILE_EXTENSION = ".dat";
	public static final String ZIP_FILE_EXTENSION = ".zip";

	public static final String ENCODING = "UTF-8";

	private FSUtil() {
		// private default constructor
	}

	public static final String encodeNewline(final String str) {
		final int length = str.length();
		final StringBuilder sb = new StringBuilder(length + 16);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			final char c = str.charAt(i);
			if (c == '\\') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('\\'); // NOPMD (double append is faster)
			} else if (c == '\r') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('r'); // NOPMD (double append is faster)
			} else if (c == '\n') {
				changed = true;
				sb.append('\\'); // NOPMD (double append is faster)
				sb.append('n'); // NOPMD (double append is faster)
			} else {
				sb.append(c);
			}
		}
		if (changed) {
			return sb.toString();
		} else {
			return str;
		}
	}

	public static final String decodeNewline(final String str) {
		final int length = str.length();
		final StringBuilder sb = new StringBuilder(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			final char c = str.charAt(i);
			if (c == '\\') {
				final char d = str.charAt(++i); // NOCS
				if (d == '\\') {
					changed = true;
					sb.append('\\');
				} else if (d == 'r') {
					changed = true;
					sb.append('\r');
				} else if (d == 'n') {
					changed = true;
					sb.append('\n');
				} else {
					// we simply ignore unknown escapes
					sb.append(c);
					sb.append(d);
				}
			} else {
				sb.append(c);
			}
		}
		if (changed) {
			return sb.toString();
		} else {
			return str;
		}
	}
}