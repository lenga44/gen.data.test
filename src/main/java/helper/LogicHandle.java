package helper;

import common.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogicHandle {
    public static String getFileName(String path) {
        List<String> list = Arrays.stream(path.split("/")).toList();
        String result = null;
        for (String item : list) {
            if (item.contains(".zip")) {
                result = item;
                break;
            }
        }
        return result;
    }
    public static boolean isNumeric(String strNum){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
    public static String removeString(String str,String replace){
        if(str.contains(replace)){
            str = str.replace(replace,"");
        }
        return str;
    }
    public static String removeString(String str,List<String> replaces){
        for (String replace:replaces){
            str=removeString(str,replace);
        }
        return str;
    }
    public static String removeLetterAndSpace(String str){
        return str.replaceAll("[^0-9]", "");
    }
    public static String replaceString(String str,String oldStr,String newStr){
        if(str.contains(oldStr)){
            str = str.replace(oldStr,newStr);
        }
        return str;
    }
    public static String replaceString(String str,String newStr,String... oldStr){
        for (String old: oldStr) {
            if (str.contains(old)) {
                str = str.replace(old, newStr);
            }
        }
        return str;
    }
    public static String splitString(String str,String splitStr){
        if(str.contains(splitStr)){
            str = Arrays.stream(str.split(splitStr)).toList().get(0);
        }
        return str;
    }
    public static List<String> splitStrings(String str,String splitStr){
        List<String> list = new ArrayList<>();
        if(str.contains(splitStr)) {
            list = Arrays.stream(str.split(splitStr)).toList();
        }else {
            list.add(str);
        }
        return list;
    }
    public static String splitString(String str,String splitStr,int index){
        return splitStrings(str,splitStr).get(index);
    }
    private static String splitString(String str){
        String result = str;
        if(str.startsWith("[") && str.endsWith("]")){
            result = str.replace("[","").replace("]","");
        }
        return result;
    }
    public static List<String> convertStringToList(String inputString){
        inputString = splitString(inputString);
        List<String> resultList = new ArrayList<>();
        for (String spitStr: Constant.splits) {
            if(inputString.contains(spitStr)){
                resultList = Arrays.asList(inputString.split(spitStr));
                break;
            }
        }
        if (resultList.size()==0){
            resultList.add(inputString);
        }
        return resultList;
    }
    public static List<String> convertStringToListSplit(String inputString){
        if(inputString.equals("no/ No, I can't draw./ Drawing is hard.")){
            System.out.println("1");
        }
        inputString = splitString(inputString);
        List<String> resultList = new ArrayList<>();
        for (String spitStr : Constant.splits1) {
            if (inputString.contains(spitStr)) {
                for (String text : inputString.split(spitStr)) {
                    resultList.add(text.trim());
                }
                break;
            }
        }
        if (resultList.size()==0 && inputString.length()>0){
            resultList.add(inputString);
        }
        return resultList;
    }
    public static List<Integer> convertStringsToIntegers(List<String> list){
        List<Integer> values = new ArrayList<>();
        for(String item: list){
            values.add(Integer.parseInt(item));
        }
        return values;
    }
    public static String deleteMultipleSpaces(String input){
        input = input.replaceAll("\\s+", " ");
        return input.trim();
    }
}
