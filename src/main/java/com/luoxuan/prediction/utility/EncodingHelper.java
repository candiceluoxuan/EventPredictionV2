package com.luoxuan.prediction.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingHelper {

	Pattern p = Pattern.compile("\\\\u([\\S]{4})([^\\\\]*)");

	public String unicodeToUTF8(String unicode) {
		final StringBuffer buffer = new StringBuffer();

		Matcher match = p.matcher(unicode);
		while (match.find()) {
			char letter = (char) Integer.parseInt(match.group(1), 16);
			buffer.append(new Character(letter).toString());
			buffer.append(match.group(2));
		}

		return buffer.toString();
	}
}
