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
 * QUsers is a Querydsl query type for QUsers
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUsers extends com.querydsl.sql.RelationalPathBase<QUsers> {

    private static final long serialVersionUID = -1225182549;

    public static final QUsers users = new QUsers("users");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> isDeleted = createNumber("isDeleted", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath snsId = createString("snsId");

    public final StringPath snsType = createString("snsType");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QUsers> primary = createPrimaryKey(userId);

    public final com.querydsl.sql.ForeignKey<QDailySummary> _dailySummaryToUsersFk = createInvForeignKey(Arrays.asList(userId, userId, userId), Arrays.asList("user_id", "user_id", "user_id"));

    public final com.querydsl.sql.ForeignKey<QUserTags> _userTagsToUsersFk = createInvForeignKey(Arrays.asList(userId, userId, userId), Arrays.asList("user_id", "user_id", "user_id"));

    public QUsers(String variable) {
        super(QUsers.class, forVariable(variable), "null", "users");
        addMetadata();
    }

    public QUsers(String variable, String schema, String table) {
        super(QUsers.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUsers(String variable, String schema) {
        super(QUsers.class, forVariable(variable), schema, "users");
        addMetadata();
    }

    public QUsers(Path<? extends QUsers> path) {
        super(path.getType(), path.getMetadata(), "null", "users");
        addMetadata();
    }

    public QUsers(PathMetadata metadata) {
        super(QUsers.class, metadata, "null", "users");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(email, ColumnMetadata.named("email").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(5).ofType(Types.VARCHAR).withSize(20).notNull());
        addMetadata(snsId, ColumnMetadata.named("sns_id").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(snsType, ColumnMetadata.named("sns_type").withIndex(2).ofType(Types.VARCHAR).withSize(10).notNull());
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

