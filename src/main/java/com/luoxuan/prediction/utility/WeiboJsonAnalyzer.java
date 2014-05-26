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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luoxuan.prediction.domain.PersistentWeibo;

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
			PersistentWeibo weibo = new PersistentWeibo();
			weibo.setId(jsonNode.get("id").asText());
			weibo.setUid(jsonNode.get("uid").asText());
			weibo.setContent(jsonNode.get("text").asText());

			DateFormat dateFormat = new SimpleDateFormat(
					"EEE MMM d HH:mm:ss Z yyyy", Locale.US);
			Date date = dateFormat.parse(jsonNode.get("created_at").asText());
			weibo.setDate(date);

			return weibo;
		}
		return null;
	}

	protected PersistentWeibo parse(String json)
			throws JsonProcessingException, ParseException, IOException {
		return parse(objectMapper.readTree(json));
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

	public String extractJson(String line) {
		Matcher matcher = textPattern.matcher(line);
		if (matcher.find()) {
			return matcher.group(3);
		}
		return null;
	}
}
