package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QUserTagDetails is a Querydsl query type for QUserTagDetails
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUserTagDetails extends com.querydsl.sql.RelationalPathBase<QUserTagDetails> {

    private static final long serialVersionUID = -1157524944;

    public static final QUserTagDetails userTagDetails = new QUserTagDetails("user_tag_details");

    public final DateTimePath<java.sql.Timestamp> lastUsedAt = createDateTime("lastUsedAt", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> searchCount = createNumber("searchCount", Integer.class);

    public final NumberPath<Integer> tagId = createNumber("tagId", Integer.class);

    public final NumberPath<Integer> totalUsageCount = createNumber("totalUsageCount", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> userTagId = createNumber("userTagId", Integer.class);

    public QUserTagDetails(String variable) {
        super(QUserTagDetails.class, forVariable(variable), "null", "user_tag_details");
        addMetadata();
    }

    public QUserTagDetails(String variable, String schema, String table) {
        super(QUserTagDetails.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUserTagDetails(String variable, String schema) {
        super(QUserTagDetails.class, forVariable(variable), schema, "user_tag_details");
        addMetadata();
    }

    public QUserTagDetails(Path<? extends QUserTagDetails> path) {
        super(path.getType(), path.getMetadata(), "null", "user_tag_details");
        addMetadata();
    }

    public QUserTagDetails(PathMetadata metadata) {
        super(QUserTagDetails.class, metadata, "null", "user_tag_details");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(lastUsedAt, ColumnMetadata.named("last_used_at").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(5).notNull());
        addMetadata(score, ColumnMetadata.named("score").withIndex(8).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(searchCount, ColumnMetadata.named("search_count").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(tagId, ColumnMetadata.named("tag_id").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(totalUsageCount, ColumnMetadata.named("total_usage_count").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userTagId, ColumnMetadata.named("user_tag_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

