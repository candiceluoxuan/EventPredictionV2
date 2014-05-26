package com.luoxuan.prediction.utility;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.PersistentWeibo;
import com.luoxuan.prediction.domain.PreprocessedWeibo;

public class WeiboJsonAnalyzer {

	Pattern textPattern = Pattern.compile("^(.*?)\t(.*?)\t(\\{.*\\})$");

	@Autowired
	@Qualifier("encodingHelper")
	EncodingHelper encodingHelper;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper objectMapper;

	protected PersistentWeibo parse(JsonNode jsonNode) throws ParseException {
		if (jsonNode != null) {
			JsonNode jnId = jsonNode.get("id");
			JsonNode jnUid = jsonNode.get("uid");
			JsonNode jnText = jsonNode.get("text");
			JsonNode jnCreateAt = jsonNode.get("created_at");

			PersistentWeibo weibo = new PersistentWeibo();
			if (jnId != null) {
				weibo.setId(jnId.asText());
			}
			if (jnUid != null) {
				weibo.setUid(jnUid.asText());
			}
			if (jnText != null) {
				weibo.setContent(jnText.asText());
			}
			if (jnCreateAt != null) {
				DateFormat dateFormat = new SimpleDateFormat(
						"EEE MMM d HH:mm:ss Z yyyy", Locale.US);
				Date date = dateFormat.parse(jnCreateAt.asText());
				weibo.setDate(date);
			}

			return weibo;
		}
		return null;
	}

	protected PreprocessedWeibo parsePreprocessedWeibo(JsonNode jsonNode)
			throws ParseException {
		if (jsonNode != null) {
			JsonNode jnId = jsonNode.get("id");
			JsonNode jnUid = jsonNode.get("uid");
			JsonNode jnText = jsonNode.get("text");
			JsonNode jnCreateAt = jsonNode.get("created_at");

			PreprocessedWeibo weibo = new PreprocessedWeibo();
			if (jnId != null) {
				weibo.setId(jnId.asText());
			}
			if (jnUid != null) {
				weibo.setUid(jnUid.asText());
			}
			if (jnText != null) {
				weibo.setContent(jnText.asText());
			}
			if (jnCreateAt != null) {
				DateFormat dateFormat = new SimpleDateFormat(
						"EEE MMM d HH:mm:ss Z yyyy", Locale.US);
				Date date = dateFormat.parse(jnCreateAt.asText());
				weibo.setDate(date);
			}

			return weibo;
		}
		return null;
	}

	public List<PersistentWeibo> execute(String json) {
		List<PersistentWeibo> weibos = new LinkedList<PersistentWeibo>();

		try {
			JsonNode root = objectMapper.readTree(json);

			try {
				PersistentWeibo weibo = parse(root);
				if (weibo != null) {
					weibos.add(weibo);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				PersistentWeibo weibo = parse(root.get("retweeted_status"));
				if (weibo != null) {
					weibos.add(weibo);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weibos;
	}

	public List<PreprocessedWeibo> executePreprocessedWeibo(String json) {
		List<PreprocessedWeibo> weibos = new LinkedList<PreprocessedWeibo>();

		try {
			JsonNode root = objectMapper.readTree(json);

			try {
				PreprocessedWeibo weibo = parsePreprocessedWeibo(root);
				if (weibo != null) {
					weibos.add(weibo);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				PreprocessedWeibo weibo = parsePreprocessedWeibo(root
						.get("retweeted_status"));
				if (weibo != null) {
					weibos.add(weibo);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weibos;
	}

	public String extractJson(String line) {
		Matcher matcher = textPattern.matcher(line);
		if (matcher.find()) {
			return matcher.group(3);
		}
		return null;
	}
}
