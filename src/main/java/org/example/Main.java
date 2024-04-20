package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        run();
    }
    private static void run() throws IOException {
        System.out.println("Nhập 1 để chạy GenDataAISpeakLesson");
        System.out.println("Nhập mã file để run: ");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        switch (number){
            case 1:
                GenDataAISpeakLesson.run();
                break;
            default:
                System.out.println("Invalid choice!");
        }
        scanner.close();
    }
}
