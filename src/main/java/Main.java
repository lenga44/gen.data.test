import ai.speak.course.script.GenDataAISpeakLessonActual;
import helper.JsonHandle;
import m_go.script.GenDataGameMgoActual;
import video.call.script.GenDataVideoCall;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        run();
        //test();
    }
    private static void test(){
        String a = "{\n" +
                "  \"file_zip\": \"1528344-57778-gjk.zip\",\n" +
                "  \"story_name\": {},\n" +
                "  \"thumb_end\": {},\n" +
                "  \"background\": \"\",\n" +
                "  \"act_id\": 1528344,\n" +
                "  \"id\": 1000162,\n" +
                "  \"turn\": [\n" +
                "    {\n" +
                "      \"right_answer\": [],\n" +
                "      \"word\": [\n" +
                "        {\n" +
                "          \"path\": \"18c50572-fec0-4d60-8d70-2db9a26153bb_358200.zip\",\n" +
                "          \"image\": [],\n" +
                "          \"word_id\": 40156156,\n" +
                "          \"text\": \"cake\",\n" +
                "          \"audio\": [\n" +
                "            {\n" +
                "              \"duration\": 0,\n" +
                "              \"file_path\": \"880e7159-9205-4024-b1f2-0cfade6f9553.mp3\",\n" +
                "              \"score\": 0,\n" +
                "              \"voices_type_id\": 65,\n" +
                "              \"tag_title\": \"cake\",\n" +
                "              \"link\": \"audio/880e7159-9205-4024-b1f2-0cfade6f9553.mp3\",\n" +
                "              \"name_original\": \"\",\n" +
                "              \"voices_id\": 65,\n" +
                "              \"id\": 255681,\n" +
                "              \"sync_data\": \"\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"video\": [],\n" +
                "          \"type\": \"question\",\n" +
                "          \"order\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"ae432953-87cc-45f1-85f0-970201ef4b43_358201.zip\",\n" +
                "          \"image\": [],\n" +
                "          \"word_id\": 40156157,\n" +
                "          \"text\": \"c\",\n" +
                "          \"audio\": [\n" +
                "            {\n" +
                "              \"duration\": 0,\n" +
                "              \"file_path\": \"402ed20a-b076-4278-82c5-a80d7b12edf8.mp3\",\n" +
                "              \"score\": 0,\n" +
                "              \"voices_type_id\": 65,\n" +
                "              \"tag_title\": \"c\",\n" +
                "              \"link\": \"audio/402ed20a-b076-4278-82c5-a80d7b12edf8.mp3\",\n" +
                "              \"name_original\": \"\",\n" +
                "              \"voices_id\": 65,\n" +
                "              \"id\": 255682,\n" +
                "              \"sync_data\": \"\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"video\": [],\n" +
                "          \"type\": \"phonic\",\n" +
                "          \"order\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"7934d7a2-a65a-4b10-a3f3-3d25151c416e_358202.zip\",\n" +
                "          \"image\": [],\n" +
                "          \"word_id\": 40156158,\n" +
                "          \"text\": \"a_e\",\n" +
                "          \"audio\": [\n" +
                "            {\n" +
                "              \"duration\": 0,\n" +
                "              \"file_path\": \"1aaaced2-e7ab-4250-b726-b6e960400641.mp3\",\n" +
                "              \"score\": 0,\n" +
                "              \"voices_type_id\": 65,\n" +
                "              \"tag_title\": \"a_e\",\n" +
                "              \"link\": \"audio/1aaaced2-e7ab-4250-b726-b6e960400641.mp3\",\n" +
                "              \"name_original\": \"\",\n" +
                "              \"voices_id\": 65,\n" +
                "              \"id\": 255683,\n" +
                "              \"sync_data\": \"\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"video\": [],\n" +
                "          \"type\": \"phonic\",\n" +
                "          \"order\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"path\": \"20e30abd-9f5d-40b9-aeb2-fb0a81609765_358203.zip\",\n" +
                "          \"image\": [],\n" +
                "          \"word_id\": 40156159,\n" +
                "          \"text\": \"k\",\n" +
                "          \"audio\": [\n" +
                "            {\n" +
                "              \"duration\": 0,\n" +
                "              \"file_path\": \"dea9dbe8-442d-4d1a-8445-f4878dbc565b.mp3\",\n" +
                "              \"score\": 0,\n" +
                "              \"voices_type_id\": 65,\n" +
                "              \"tag_title\": \"k\",\n" +
                "              \"link\": \"audio/dea9dbe8-442d-4d1a-8445-f4878dbc565b.mp3\",\n" +
                "              \"name_original\": \"\",\n" +
                "              \"voices_id\": 65,\n" +
                "              \"id\": 255684,\n" +
                "              \"sync_data\": \"\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"video\": [],\n" +
                "          \"type\": \"phonic\",\n" +
                "          \"order\": 0\n" +
                "        }\n" +
                "      ],\n" +
                "      \"phonic\": {},\n" +
                "      \"order\": 1\n" +
                "    }\n" +
                "  ],\n" +
                "  \"thumb_start\": {}\n" +
                "}";
        String s = JsonHandle.getValue(a,"$.length($.turn[0].word[?(@.type=='phonic')].text.length())");
        System.out.println(s);
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
