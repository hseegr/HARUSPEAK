package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QMomentTags is a Querydsl query type for QMomentTags
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QMomentTags extends com.querydsl.sql.RelationalPathBase<QMomentTags> {

    private static final long serialVersionUID = -859835530;

    public static final QMomentTags momentTags = new QMomentTags("moment_tags");

    public final NumberPath<Integer> momentId = createNumber("momentId", Integer.class);

    public final NumberPath<Integer> momentTagId = createNumber("momentTagId", Integer.class);

    public final NumberPath<Integer> userTagId = createNumber("userTagId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QMomentTags> primary = createPrimaryKey(momentTagId);

    public final com.querydsl.sql.ForeignKey<QUserTags> momentTagsToUserTagsFk = createForeignKey(userTagId, "user_tag_id");

    public QMomentTags(String variable) {
        super(QMomentTags.class, forVariable(variable), "null", "moment_tags");
        addMetadata();
    }

    public QMomentTags(String variable, String schema, String table) {
        super(QMomentTags.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QMomentTags(String variable, String schema) {
        super(QMomentTags.class, forVariable(variable), schema, "moment_tags");
        addMetadata();
    }

    public QMomentTags(Path<? extends QMomentTags> path) {
        super(path.getType(), path.getMetadata(), "null", "moment_tags");
        addMetadata();
    }

    public QMomentTags(PathMetadata metadata) {
        super(QMomentTags.class, metadata, "null", "moment_tags");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(momentId, ColumnMetadata.named("moment_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentTagId, ColumnMetadata.named("moment_tag_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userTagId, ColumnMetadata.named("user_tag_id").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

