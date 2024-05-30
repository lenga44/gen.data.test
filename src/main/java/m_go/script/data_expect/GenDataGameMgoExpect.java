package m_go.script.data_expect;

import java.io.IOException;

public class GenDataGameMgoExpect {
    public static void main(String[] args) throws IOException {
        run();
    }
    public static void run() throws IOException {
        int flow = Flow.getFlow("D:\\gen.data.test\\src\\main\\java\\m_go\\data\\Monkey Go (BE) - Data.xlsx",1);
        System.out.println(flow);
    }

}
