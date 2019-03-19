package com.example.provider.utils;

import java.util.Random;
import java.util.UUID;

public class NumberUtil {
	
	private static final ThreadLocal<Random> localRandom = new ThreadLocal<Random>() {
	
		@Override
		protected Random initialValue() {
			return new Random();
		};
	};
	
	public static int getRandomNum() {
		return localRandom.get().nextInt(10000);
	}
	
	public static String generateID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
