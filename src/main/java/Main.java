import ai.speak.course.script.GenDataAISpeakLessonActual;
import ai.speak.course.script.GenDataAISpeakLessonExpect;
import m_go.script.GenDataGameMgo;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        run();
    }
    private static void run() throws IOException {
        try {
            System.out.println("Nhập 1 để chạy GenDataAISpeakLesson");
            System.out.println("Nhập mã file để run: ");
            Scanner scanner = new Scanner(System.in);
            //int number = scanner.nextInt();
            int number = 1;
            switch (number) {
                case 1:
                    GenDataAISpeakLessonActual.run();
                    GenDataAISpeakLessonExpect.run();
                    break;
                case 2:
                    GenDataGameMgo.run();
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
