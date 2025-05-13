package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QMomentTagNames is a Querydsl query type for QMomentTagNames
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QMomentTagNames extends com.querydsl.sql.RelationalPathBase<QMomentTagNames> {

    private static final long serialVersionUID = -1171152725;

    public static final QMomentTagNames momentTagNames = new QMomentTagNames("moment_tag_names");

    public final NumberPath<Integer> momentId = createNumber("momentId", Integer.class);

    public final NumberPath<Integer> momentTagId = createNumber("momentTagId", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final NumberPath<Integer> userTagId = createNumber("userTagId", Integer.class);

    public QMomentTagNames(String variable) {
        super(QMomentTagNames.class, forVariable(variable), "null", "moment_tag_names");
        addMetadata();
    }

    public QMomentTagNames(String variable, String schema, String table) {
        super(QMomentTagNames.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QMomentTagNames(String variable, String schema) {
        super(QMomentTagNames.class, forVariable(variable), schema, "moment_tag_names");
        addMetadata();
    }

    public QMomentTagNames(Path<? extends QMomentTagNames> path) {
        super(path.getType(), path.getMetadata(), "null", "moment_tag_names");
        addMetadata();
    }

    public QMomentTagNames(PathMetadata metadata) {
        super(QMomentTagNames.class, metadata, "null", "moment_tag_names");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(momentId, ColumnMetadata.named("moment_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(momentTagId, ColumnMetadata.named("moment_tag_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(5).ofType(Types.VARCHAR).withSize(5).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(userTagId, ColumnMetadata.named("user_tag_id").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

