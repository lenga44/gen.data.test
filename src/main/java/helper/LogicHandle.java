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
}
