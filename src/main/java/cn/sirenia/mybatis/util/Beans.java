package cn.sirenia.mybatis.util;

public class Beans {
	//private static final Pattern PATTERN = Pattern.compile("_[a-z]",Pattern.MULTILINE); 
	/**
	 * 驼峰命名转下划线命名
	 * */
	public static String camel2underline(String prop){
		return prop.replaceAll("(?<Uper>[A-Z])", "_${Uper}").toLowerCase();
	}
	/**
	 * 下划线转驼峰命名
	 * @param prop
	 * @return
	 */
	/*public static String underline2camel(String column){
		if(column==null){ 
		return null; 
		} 
		Matcher matcher = PATTERN.matcher(column); 
		int index = 0; 
		StringBuilder res = new StringBuilder(); 
		int start = -1; 
		Object value = null; 
		while(matcher.find(index)){ 
			start = matcher.start(); 
			value = matcher.group(0).substring(1).toUpperCase(); 
			res.append(column.substring(index, start)).append(value); 
			index = matcher.end(); 
		} 
		res.append(column.substring(index)); 
		return res.toString(); 
	}*/
	public static String underline2camel(String column){
		if(column==null){ 
		return null; 
		} 
		String[] array = column.split("_(?=[a-z])");
		if(array.length==1){
			return column;
		}
		System.out.println(String.join(",", array));
		for(int i=1;i<array.length;i++){
			array[i] = array[i].substring(0, 1).toUpperCase()+array[i].substring(1);
		}
		return String.join("", array); 
	}
	public static void main(String[] args) {
		String column = "hello_world__avril_lavigne";
		String res = underline2camel(column);
		System.out.println(res);
		res = camel2underline(res);
		System.out.println(res);
	}
}
