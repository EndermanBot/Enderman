package org.enderman;

import java.util.ArrayList;

public class ArrayUtils {
	public static <T> ArrayList<T> asArrayList(T[] array){
		ArrayList<T> list = new ArrayList<>();
		for(int i = 0; i < array.length; i++) {
			list.add(i, array[i]);
		}
		return list;
	}
}
