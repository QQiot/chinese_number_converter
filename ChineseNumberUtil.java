package com.fengcone.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChineseNumberUtil {

	public static String convertString(String string) {
		StringBuilder builder = new StringBuilder();
		List<NumberEnum> tempList = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			NumberEnum numberEnum = NumberEnum.getByChar(string.charAt(i));
			if (numberEnum == null) {
				if (tempList.size() != 0) {
					builder.append(convert2Number(tempList));
					tempList = new ArrayList<>();
				}
				builder.append(string.charAt(i));
				continue;
			}
			tempList.add(numberEnum);
			if (i == string.length() - 1) {
				builder.append(convert2Number(tempList));
			}
		}
		return builder.toString();
	}

	private static Long convert2Number(List<NumberEnum> numberList) {
		List<NumberEnum> tempList = new ArrayList<ChineseNumberUtil.NumberEnum>();
		NumberEnum last = null;
		Long result = 0L;
		for (int i = 0; i < numberList.size(); i++) {
			NumberEnum numberEnum = numberList.get(i);
			if (numberEnum.type == 3) {
				if (last.type == 3) {
					result = result * numberEnum.value;
				} else {
					result = result + convert2BasicNum(tempList) * numberEnum.value;
					tempList = new ArrayList<>();
				}
			} else {
				tempList.add(numberList.get(i));
			}
			if (i == numberList.size() - 1) {
				result = result + convert2BasicNum(tempList);
			}
			last = numberEnum;
		}
		return result;
	}


	private static Long convert2BasicNum(List<NumberEnum> numberList) {
		NumberEnum last = NumberEnum.ONE;
		Long result = 0L;
		for (int i = 0; i < numberList.size(); i++) {
			NumberEnum numberEnum = numberList.get(i);
			if (numberEnum.type == 2) {
				result = result + last.value * numberEnum.value;
			}
			if (i == numberList.size() - 1 && numberEnum.type == 1) {
				if (last == NumberEnum.ZERO || numberList.size() == 1) {
					result = result + numberEnum.value;
				} else {
					result = result + (numberEnum.value * last.value) / 10;
				}
			}
			last = numberEnum;
		}
		return result;
	}

	public static void main(String[] args) {
		String number = "物流九千零五段誉si八百三十二万零5";
		System.out.println(ChineseNumberUtil.convertString(number));
	}

	enum NumberEnum {
		ZERO("零〇", 0L, 1), 
		ONE("一壹", 1L, 1),
		TWO("二贰", 2L, 1),
		THREE("三叁", 3L, 1), 
		FOUR("四肆", 4L, 1), 
		FIVE("五伍", 5L, 1), 
		SIX("六陆", 6L, 1), 
		SEVEN("七柒", 7L, 1), 
		EIGHT("八捌", 8L, 1),
		NINE("九玖", 9L, 1), 
		TEN("十拾", 10L, 2), 
		HUNDRED("百佰", 100L, 2), 
		THOUSAND("千仟", 1000L, 2), 
		TEN_THOUSAND("万萬", 10000L,3), 
		HUNDRED_MILLION("亿億", 100000000L, 3);

		String key;
		Long value;
		Integer type;
		private static Map<Character, NumberEnum> map = new HashMap<>();

		public static NumberEnum getByChar(Character character) {
			map = new HashMap<>();
			for (NumberEnum number : NumberEnum.values()) {
				for (int i = 0; i < number.key.length(); i++) {
					map.put(number.key.charAt(i), number);
				}
				if (number.type == 1) {
					map.put(number.value.toString().charAt(0), number);
				}
			}
			return map.get(character);
		}

		private NumberEnum(String key, Long value, Integer type) {
			this.key = key;
			this.value = value;
			this.type = type;
		}
	}
}