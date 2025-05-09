package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QDailySummary is a Querydsl query type for QDailySummary
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QDailySummary extends com.querydsl.sql.RelationalPathBase<QDailySummary> {

    private static final long serialVersionUID = 1300525386;

    public static final QDailySummary dailySummary = new QDailySummary("daily_summary");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> contentGenerateCount = createNumber("contentGenerateCount", Integer.class);

    public final NumberPath<Integer> imageGenerateCount = createNumber("imageGenerateCount", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> momentCount = createNumber("momentCount", Integer.class);

    public final NumberPath<Integer> summaryId = createNumber("summaryId", Integer.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.sql.Timestamp> updatedAt = createDateTime("updatedAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final DatePath<java.sql.Date> writeDate = createDate("writeDate", java.sql.Date.class);

    public final com.querydsl.sql.PrimaryKey<QDailySummary> primary = createPrimaryKey(summaryId);

    public final com.querydsl.sql.ForeignKey<QUsers> dailySummaryToUsersFk = createForeignKey(userId, "user_id");

    public final com.querydsl.sql.ForeignKey<QDailyMoments> _dailyMomentsToDailySummaryFk = createInvForeignKey(Arrays.asList(summaryId, summaryId), Arrays.asList("summary_id", "summary_id"));

    public QDailySummary(String variable) {
        super(QDailySummary.class, forVariable(variable), "null", "daily_summary");
        addMetadata();
    }

    public QDailySummary(String variable, String schema, String table) {
        super(QDailySummary.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDailySummary(String variable, String schema) {
        super(QDailySummary.class, forVariable(variable), schema, "daily_summary");
        addMetadata();
    }

    public QDailySummary(Path<? extends QDailySummary> path) {
        super(path.getType(), path.getMetadata(), "null", "daily_summary");
        addMetadata();
    }

    public QDailySummary(PathMetadata metadata) {
        super(QDailySummary.class, metadata, "null", "daily_summary");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(content, ColumnMetadata.named("content").withIndex(6).ofType(Types.VARCHAR).withSize(200).notNull());
        addMetadata(contentGenerateCount, ColumnMetadata.named("content_generate_count").withIndex(8).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(imageGenerateCount, ColumnMetadata.named("image_generate_count").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(imageUrl, ColumnMetadata.named("image_url").withIndex(5).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(12).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(momentCount, ColumnMetadata.named("moment_count").withIndex(9).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(summaryId, ColumnMetadata.named("summary_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(title, ColumnMetadata.named("title").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(11).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(viewCount, ColumnMetadata.named("view_count").withIndex(10).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(writeDate, ColumnMetadata.named("write_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
    }

}

