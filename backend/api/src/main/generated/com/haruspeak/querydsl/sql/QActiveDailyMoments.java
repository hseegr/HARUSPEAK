package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QActiveDailyMoments is a Querydsl query type for QActiveDailyMoments
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QActiveDailyMoments extends com.querydsl.sql.RelationalPathBase<QActiveDailyMoments> {

    private static final long serialVersionUID = -1184378755;

    public static final QActiveDailyMoments activeDailyMoments = new QActiveDailyMoments("active_daily_moments");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> imageCount = createNumber("imageCount", Integer.class);

    public final NumberPath<Integer> momentId = createNumber("momentId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> momentTime = createDateTime("momentTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> summaryId = createNumber("summaryId", Integer.class);

    public final NumberPath<Integer> tagCount = createNumber("tagCount", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QActiveDailyMoments(String variable) {
        super(QActiveDailyMoments.class, forVariable(variable), "null", "active_daily_moments");
        addMetadata();
    }

    public QActiveDailyMoments(String variable, String schema, String table) {
        super(QActiveDailyMoments.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QActiveDailyMoments(String variable, String schema) {
        super(QActiveDailyMoments.class, forVariable(variable), schema, "active_daily_moments");
        addMetadata();
    }

    public QActiveDailyMoments(Path<? extends QActiveDailyMoments> path) {
        super(path.getType(), path.getMetadata(), "null", "active_daily_moments");
        addMetadata();
    }

    public QActiveDailyMoments(PathMetadata metadata) {
        super(QActiveDailyMoments.class, metadata, "null", "active_daily_moments");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(content, ColumnMetadata.named("content").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(imageCount, ColumnMetadata.named("image_count").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentId, ColumnMetadata.named("moment_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentTime, ColumnMetadata.named("moment_time").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(summaryId, ColumnMetadata.named("summary_id").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(tagCount, ColumnMetadata.named("tag_count").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(viewCount, ColumnMetadata.named("view_count").withIndex(8).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

