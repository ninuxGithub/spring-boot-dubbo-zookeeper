package com.example.consumer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);


	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		// 空对象不要抛出异常
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 处理空的情况
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性,不抛出异常
		mapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public static Map<String, TreeMap<String, Double>> readStringToMap(String content) {
		try {
			Map<String, TreeMap<String, Double>> result = mapper.readValue(content,
					new TypeReference<Map<String, TreeMap<String, Double>>>() {
					});
			return result;
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  Map<String, TreeMap<String, BigDecimal>> failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static <S, T> Map<S, T> readStringToMap(String content, Class<S> key, Class<T> value) {
		try {
			Map<S, T> result = mapper.readValue(content,
					mapper.getTypeFactory().constructMapLikeType(HashMap.class, key, value));
			return result;
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  Map failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static <S, T> TreeMap<S, T> readStringToTreeMap(String content, Class<S> key, Class<T> value) {
		try {
			TreeMap<S, T> result = mapper.readValue(content,
					mapper.getTypeFactory().constructMapLikeType(TreeMap.class, key, value));
			return result;
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  Map failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static <T> List<T> readStringToList(String content, Class<T> clazz) {
		try {
			List<T> result = mapper.readValue(content,
					mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz));
			return result;
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  List failure. {} ", e.getMessage());
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static String writeValueAsString(Object content) {
		try {
			return mapper.writeValueAsString(content);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("JsonUtil writeValueAsString  to  String failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static Map<String, Double[]> readStringToMapVArray(String content) {
		try {
			Map<String, Double[]> result = mapper.readValue(content, new TypeReference<Map<String, Double[]>>() {
			});
			return result;
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  Map<Integer,List<String>> failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}

	public static <T> T readStringToJavaBean(String content, Class<T> clazz) {
		try {
			return mapper.readValue(content, clazz);
		} catch (IOException e) {
			logger.error("JsonUtil readString  to  Map<Integer,List<String>> failure.");
			throw new RuntimeException("数据转换格式失败");
		}
	}
}
