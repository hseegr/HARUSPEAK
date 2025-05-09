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
 * QDailyMoments is a Querydsl query type for QDailyMoments
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QDailyMoments extends com.querydsl.sql.RelationalPathBase<QDailyMoments> {

    private static final long serialVersionUID = 98469911;

    public static final QDailyMoments dailyMoments = new QDailyMoments("daily_moments");

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> imageCount = createNumber("imageCount", Integer.class);

    public final NumberPath<Integer> momentId = createNumber("momentId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> momentTime = createDateTime("momentTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> summaryId = createNumber("summaryId", Integer.class);

    public final NumberPath<Integer> tagCount = createNumber("tagCount", Integer.class);

    public final DateTimePath<java.sql.Timestamp> updatedAt = createDateTime("updatedAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QDailyMoments> primary = createPrimaryKey(momentId);

    public final com.querydsl.sql.ForeignKey<QDailySummary> dailyMomentsToDailySummaryFk = createForeignKey(summaryId, "summary_id");

    public final com.querydsl.sql.ForeignKey<QMomentImages> _momentImagesToDailyMomentsFk = createInvForeignKey(Arrays.asList(momentId, momentId), Arrays.asList("moment_id", "moment_id"));

    public QDailyMoments(String variable) {
        super(QDailyMoments.class, forVariable(variable), "null", "daily_moments");
        addMetadata();
    }

    public QDailyMoments(String variable, String schema, String table) {
        super(QDailyMoments.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDailyMoments(String variable, String schema) {
        super(QDailyMoments.class, forVariable(variable), schema, "daily_moments");
        addMetadata();
    }

    public QDailyMoments(Path<? extends QDailyMoments> path) {
        super(path.getType(), path.getMetadata(), "null", "daily_moments");
        addMetadata();
    }

    public QDailyMoments(PathMetadata metadata) {
        super(QDailyMoments.class, metadata, "null", "daily_moments");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(content, ColumnMetadata.named("content").withIndex(3).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(8).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(imageCount, ColumnMetadata.named("image_count").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentId, ColumnMetadata.named("moment_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentTime, ColumnMetadata.named("moment_time").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(summaryId, ColumnMetadata.named("summary_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(tagCount, ColumnMetadata.named("tag_count").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(updatedAt, ColumnMetadata.named("updated_at").withIndex(9).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(viewCount, ColumnMetadata.named("view_count").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

