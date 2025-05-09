package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QUser is a Querydsl query type for QUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUser extends com.querydsl.sql.RelationalPathBase<QUser> {

    private static final long serialVersionUID = 1484498632;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> isDeleted = createNumber("isDeleted", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath snsId = createString("snsId");

    public final StringPath snsType = createString("snsType");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QUser> primary = createPrimaryKey(userId);

    public QUser(String variable) {
        super(QUser.class, forVariable(variable), "null", "user");
        addMetadata();
    }

    public QUser(String variable, String schema, String table) {
        super(QUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUser(String variable, String schema) {
        super(QUser.class, forVariable(variable), schema, "user");
        addMetadata();
    }

    public QUser(Path<? extends QUser> path) {
        super(path.getType(), path.getMetadata(), "null", "user");
        addMetadata();
    }

    public QUser(PathMetadata metadata) {
        super(QUser.class, metadata, "null", "user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(email, ColumnMetadata.named("email").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(4).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(snsId, ColumnMetadata.named("sns_id").withIndex(5).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(snsType, ColumnMetadata.named("sns_type").withIndex(6).ofType(Types.VARCHAR).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

