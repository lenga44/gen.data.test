import ai.speak.course.script.GenDataAISpeakLessonActual;
import m_go.script.GenDataGameMgoActual;
import video.call.script.GenDataVideoCall;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        run();
    }
    private static void run() throws IOException {
        try {
            System.out.println("Nhập 1 để chạy GenDataAISpeakLesson");
            System.out.println("Nhập 2 để chạy game monkey go");
            System.out.println("Nhập 3 để gen data game video call");
            System.out.println("Nhập mã file để run: ");
            Scanner scanner = new Scanner(System.in);
            int number = scanner.nextInt();
            switch (number) {
                case 1:
                    GenDataAISpeakLessonActual.run();
                    break;
                case 2:
                    System.out.println("Nhập game id: ");
                    Scanner scanner2 = new Scanner(System.in);
                    int id = scanner2.nextInt();
                    GenDataGameMgoActual.run(id);
                    break;
                case 3:
                    GenDataVideoCall.run();
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
            scanner.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
