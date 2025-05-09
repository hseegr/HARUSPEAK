package com.haruspeak.api.generator;

import java.time.LocalDate;

public class SummaryGenerator {

    public static void main(String[] args) {
        String image =  "https://haruspeak-storage.s3.ap-northeast-2.amazonaws.com/uploads/6936a327-e1b4-4706-9856-94f906ed26ab.jpg";

        int summaryCount = 15;

        String query ="INSERT INTO daily_summary (user_id, write_date, title, image_url, content) VALUES"
         ;

        for(int i = 0; i < summaryCount; i++) {
//            int r = (int)(Math.random()*30);
            LocalDate date = LocalDate.now().minusDays(i*2);
            String value = "\n(1, '" + date + "', '제목이여ㅓ" + i + "','" + image + "', '내용이여 " + i + "'),";
            query = query + value;
        }

        query = query.substring(0, query.length() - 1);
        System.out.println(query);
    }
}
