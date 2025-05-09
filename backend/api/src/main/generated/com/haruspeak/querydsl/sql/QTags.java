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
 * QTags is a Querydsl query type for QTags
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QTags extends com.querydsl.sql.RelationalPathBase<QTags> {

    private static final long serialVersionUID = 1484451606;

    public static final QTags tags = new QTags("tags");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> tagId = createNumber("tagId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QTags> primary = createPrimaryKey(tagId);

    public final com.querydsl.sql.ForeignKey<QUserTags> _userTagsToTagsFk = createInvForeignKey(Arrays.asList(tagId, tagId), Arrays.asList("tag_id", "tag_id"));

    public QTags(String variable) {
        super(QTags.class, forVariable(variable), "null", "tags");
        addMetadata();
    }

    public QTags(String variable, String schema, String table) {
        super(QTags.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTags(String variable, String schema) {
        super(QTags.class, forVariable(variable), schema, "tags");
        addMetadata();
    }

    public QTags(Path<? extends QTags> path) {
        super(path.getType(), path.getMetadata(), "null", "tags");
        addMetadata();
    }

    public QTags(PathMetadata metadata) {
        super(QTags.class, metadata, "null", "tags");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(5).notNull());
        addMetadata(tagId, ColumnMetadata.named("tag_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

