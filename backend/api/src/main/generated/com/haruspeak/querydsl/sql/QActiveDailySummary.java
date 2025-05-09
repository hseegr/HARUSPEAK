package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QActiveDailySummary is a Querydsl query type for QActiveDailySummary
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QActiveDailySummary extends com.querydsl.sql.RelationalPathBase<QActiveDailySummary> {

    private static final long serialVersionUID = 17676720;

    public static final QActiveDailySummary activeDailySummary = new QActiveDailySummary("active_daily_summary");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> contentGenerateCount = createNumber("contentGenerateCount", Integer.class);

    public final NumberPath<Integer> imageGenerateCount = createNumber("imageGenerateCount", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> momentCount = createNumber("momentCount", Integer.class);

    public final NumberPath<Integer> summaryId = createNumber("summaryId", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final DatePath<java.sql.Date> writeDate = createDate("writeDate", java.sql.Date.class);

    public QActiveDailySummary(String variable) {
        super(QActiveDailySummary.class, forVariable(variable), "null", "active_daily_summary");
        addMetadata();
    }

    public QActiveDailySummary(String variable, String schema, String table) {
        super(QActiveDailySummary.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QActiveDailySummary(String variable, String schema) {
        super(QActiveDailySummary.class, forVariable(variable), schema, "active_daily_summary");
        addMetadata();
    }

    public QActiveDailySummary(Path<? extends QActiveDailySummary> path) {
        super(path.getType(), path.getMetadata(), "null", "active_daily_summary");
        addMetadata();
    }

    public QActiveDailySummary(PathMetadata metadata) {
        super(QActiveDailySummary.class, metadata, "null", "active_daily_summary");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(content, ColumnMetadata.named("content").withIndex(6).ofType(Types.VARCHAR).withSize(200).notNull());
        addMetadata(contentGenerateCount, ColumnMetadata.named("content_generate_count").withIndex(8).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(imageGenerateCount, ColumnMetadata.named("image_generate_count").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(imageUrl, ColumnMetadata.named("image_url").withIndex(5).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(momentCount, ColumnMetadata.named("moment_count").withIndex(9).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(summaryId, ColumnMetadata.named("summary_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(title, ColumnMetadata.named("title").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(viewCount, ColumnMetadata.named("view_count").withIndex(10).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(writeDate, ColumnMetadata.named("write_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
    }

}

