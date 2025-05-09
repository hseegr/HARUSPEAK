package com.haruspeak.api.generator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagGenerator {

    public static void main(String[] args) {

        //user tag id : 1~14
        String query ="INSERT INTO moment_tags (moment_id, user_tag_id) VALUES";

        for(int i=0; i<momentIds.size(); i++) {
            int tagCount = (int)(Math.random()*4);

            int momentId = momentIds.get(i);
            Set<Integer> tags = new HashSet<>();
            for(int j=0; j<tagCount; j++) {
                int tagId = (int)(Math.random()*13) + 1;
                tags.add(tagId);
            }

            for(int user_tag_id : tags) {
                query += "\n(" + momentId + ", " + user_tag_id + "),";
            }
        }

        System.out.println(query);
    }




    static List<Integer> momentIds = List.of(
            1,
            2,
            101,
            99,
            97,
            102,
            104,
            106,
            98,
            100,
            103,
            105,
            67,
            68,
            69,
            70,
            71,
            72,
            73,
            74,
            75,
            76,
            77,
            78,
            79,
            80,
            81,
            111,
            110,
            109,
            107,
            108,
            82,
            83,
            84,
            85,
            86,
            87,
            88,
            89,
            90,
            91,
            92,
            93,
            94,
            95,
            96,
            112,
            117,
            115,
            113,
            114,
            116,
            118,
            122,
            121,
            123,
            120,
            119,
            124,
            127,
            126,
            125,
            131,
            135,
            130,
            133,
            129,
            132,
            134,
            128,
            137,
            142,
            144,
            139,
            136,
            138,
            143,
            140,
            141,
            149,
            147,
            148,
            146,
            145,
            156,
            151,
            150,
            157,
            154,
            152,
            153,
            158,
            155,
            159,
            160,
            162,
            161,
            170,
            167,
            164,
            166,
            165,
            163,
            168,
            169,
            175,
            171,
            174,
            173,
            172,
            179,
            176,
            177,
            178,
            186,
            184,
            187,
            180,
            182,
            185,
            181,
            183,
            188,
            190,
            189,
            191,
            194,
            192,
            193,
            200,
            195,
            199,
            198,
            204,
            202,
            201,
            197,
            203,
            196,
            9,
            10,
            5,
            6,
            7,
            8,
            3,
            4
    );
}
