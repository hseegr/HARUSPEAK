package com.haruspeak.api.common.querydsl;

import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.MetaDataExporter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class QuerydslSqlCodegen {

    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://52.79.61.124:3306/haruspeak-api"; // DB 이름
        String user = "haruspeak"; // 사용자
        String password = "haruspeak"; // 비밀번호

        System.out.println("DB 연결 시도 중: " + url);

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("DB 연결 성공");

        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("com.haruspeak.querydsl.sql");
        exporter.setTargetFolder(new File("src/main/generated"));
        exporter.setNamingStrategy(new DefaultNamingStrategy());

        exporter.export(conn.getMetaData());
        System.out.println("Q 클래스 생성 완료");
        conn.close();
    }
}