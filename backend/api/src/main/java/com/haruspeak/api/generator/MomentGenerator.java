package com.haruspeak.api.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MomentGenerator {

    public static void main(String[] args) {
        List<Integer> ids = List.of(1, 8, 9, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54);

        List<String> dates = List.of(
                "2025-04-28",
                "2025-05-01",
                "2025-05-02",
                "2025-05-08",
                "2025-05-06",
                "2025-05-04",
                "2025-05-03",
                "2025-04-30",
                "2025-04-29",
                "2025-04-26",
                "2025-04-24",
                "2025-04-22",
                "2025-04-20",
                "2025-04-18",
                "2025-04-16",
                "2025-04-14",
                "2025-04-12",
                "2025-04-10"
        );

        String image =  "https://haruspeak-storage.s3.ap-northeast-2.amazonaws.com/uploads/6936a327-e1b4-4706-9856-94f906ed26ab.jpg";

        String query ="INSERT INTO daily_moments (summary_id, content, moment_time, created_at) VALUES";

        for (int i=0; i<ids.size(); i++) {

            int count = (int)(Math.random()*10) + 1;

            for(int j=0; j<count; j++) {
                int summary_id = ids.get(i);
                String content = null;
                if(((int)(Math.random()*2)) == 0) {
                    content = "'순간 일기 내용입니다.'";
                }
                String moment_time = "'" + dates.get(i) + " " +((int)(Math.random()*13) + 10) + ":"+((int)(Math.random()*45) + 10) +":" + ((int)(Math.random()*20) + 10) +"'";

                query = query + "\n(" + summary_id + "," + content + "," + moment_time + ", " + moment_time + "),";
            }
        }

        query = query.substring(0, query.length()-1);
        System.out.println(query);
    }
}
