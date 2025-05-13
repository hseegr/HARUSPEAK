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
 * QUserTags is a Querydsl query type for QUserTags
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUserTags extends com.querydsl.sql.RelationalPathBase<QUserTags> {

    private static final long serialVersionUID = -782062847;

    public static final QUserTags userTags = new QUserTags("user_tags");

    public final DateTimePath<java.sql.Timestamp> lastUsedAt = createDateTime("lastUsedAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final NumberPath<Integer> tagId = createNumber("tagId", Integer.class);

    public final NumberPath<Integer> totalUsageCount = createNumber("totalUsageCount", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> userTagId = createNumber("userTagId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QUserTags> primary = createPrimaryKey(userTagId);

    public final com.querydsl.sql.ForeignKey<QTags> userTagsToTagsFk = createForeignKey(tagId, "tag_id");

    public final com.querydsl.sql.ForeignKey<QUsers> userTagsToUsersFk = createForeignKey(userId, "user_id");

    public final com.querydsl.sql.ForeignKey<QMomentTags> _momentTagsToUserTagsFk = createInvForeignKey(Arrays.asList(userTagId, userTagId), Arrays.asList("user_tag_id", "user_tag_id"));

    public QUserTags(String variable) {
        super(QUserTags.class, forVariable(variable), "null", "user_tags");
        addMetadata();
    }

    public QUserTags(String variable, String schema, String table) {
        super(QUserTags.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUserTags(String variable, String schema) {
        super(QUserTags.class, forVariable(variable), schema, "user_tags");
        addMetadata();
    }

    public QUserTags(Path<? extends QUserTags> path) {
        super(path.getType(), path.getMetadata(), "null", "user_tags");
        addMetadata();
    }

    public QUserTags(PathMetadata metadata) {
        super(QUserTags.class, metadata, "null", "user_tags");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(lastUsedAt, ColumnMetadata.named("last_used_at").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(score, ColumnMetadata.named("score").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(searchCount, ColumnMetadata.named("search_count").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(tagId, ColumnMetadata.named("tag_id").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(totalUsageCount, ColumnMetadata.named("total_usage_count").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userTagId, ColumnMetadata.named("user_tag_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

