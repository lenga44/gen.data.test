package helper;

import java.util.Arrays;
import java.util.List;
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
    public static String removeString(String str,String replace){
        if(str.contains(replace)){
            str = str.replace(replace,"");
        }
        return str;
    }
    public static String splitString(String str,String splitStr){
        if(str.contains(splitStr)){
            str = Arrays.stream(str.split(splitStr)).toList().get(0);
        }
        return str;
    }
}
