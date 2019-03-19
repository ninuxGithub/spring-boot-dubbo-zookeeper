package com.example.consumer.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonBinder {
	public static Log log = LogFactory.getLog(JsonBinder.class);
	
	private ObjectMapper mapper;
	
	/**
	 * 注意，原来的版本使用的是Inclusion类型，现在替换为了Include
	 * @param include
	 */
	public JsonBinder(Include include){
		mapper = new ObjectMapper();
		
		//空对象不要抛出异常
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		//设置输出时json字符串包含的对象属性策略
		//mapper.getSerializationConfig().withSerializationInclusion(include);
		
		//设置输入时忽略JSON字符串中存在而Java对象实际没有的属性,不抛出异常
		mapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	/**
	 * 创建能够输出全部属性到Json字符串的JsonBinder对象
	 * @return
	 */
	public static JsonBinder buildNormalBinder(){
		return new JsonBinder(Include.ALWAYS);
	}
	
	/**
	 * 创建只输出非空属性到Json字符串的JsonBinder对象
	 * @return
	 */
	public static JsonBinder buildNotNullBinder(){
		return new JsonBinder(Include.NON_NULL);
	}
	
	/**
	 * 创建只输出初始化值被的属性到Json字符串的JsonBinder对象
	 * @return
	 */
	public static JsonBinder buildNotDefaultBinder(){
		return new JsonBinder(Include.NON_DEFAULT);
	}
	
	/**
	 * 如果JSON字符串为Null或null"字符或返回Null.
	 * 如果JSON字符串为"[]",返回空集合
	 * 
	 * 如需读取集合如List/Map,且不是List<String>这种类型时使用如下语
	 * List<MyBean> beanList = binder.getMapper().readValue(listString, new TypeReference<List<MyBean>>() {});
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz){
		if(StringUtils.isBlank(jsonString)){
			return null;
		}
		
		try {
			return mapper.readValue(jsonString, clazz);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.error("parse json string error", e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.error("parse json string error", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("parse json string error", e);
		}
		
		return null;
	}
	
	/**
	 * 如果对象为Null,返回"null".
	 * 如果集合为空集合,返回"[]".
	 * @param object
	 * @return
	 */
	public String toJson(Object object){
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			log.error("generate json string error", e);
		}
		
		return null;
	}
	
	/**
	 * 设置日期序列化格式
	 * @param pattern
	 */
	public void setDateFormat(String pattern){
		if(StringUtils.isNoneBlank(pattern)){
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			mapper.setDateFormat(sdf);
		}
	}
	
	/**
	 * 取出mapper做进一步的设置或使用其他序列化API
	 * @return
	 */
	public ObjectMapper getMapper(){
		return mapper;
	}
}
