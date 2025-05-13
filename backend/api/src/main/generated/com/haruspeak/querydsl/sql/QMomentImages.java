package com.haruspeak.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QMomentImages is a Querydsl query type for QMomentImages
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QMomentImages extends com.querydsl.sql.RelationalPathBase<QMomentImages> {

    private static final long serialVersionUID = -1972248939;

    public static final QMomentImages momentImages = new QMomentImages("moment_images");

    public final NumberPath<Integer> imageId = createNumber("imageId", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> momentId = createNumber("momentId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<QMomentImages> primary = createPrimaryKey(imageId);

    public final com.querydsl.sql.ForeignKey<QDailyMoments> momentImagesToDailyMomentsFk = createForeignKey(momentId, "moment_id");

    public QMomentImages(String variable) {
        super(QMomentImages.class, forVariable(variable), "null", "moment_images");
        addMetadata();
    }

    public QMomentImages(String variable, String schema, String table) {
        super(QMomentImages.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QMomentImages(String variable, String schema) {
        super(QMomentImages.class, forVariable(variable), schema, "moment_images");
        addMetadata();
    }

    public QMomentImages(Path<? extends QMomentImages> path) {
        super(path.getType(), path.getMetadata(), "null", "moment_images");
        addMetadata();
    }

    public QMomentImages(PathMetadata metadata) {
        super(QMomentImages.class, metadata, "null", "moment_images");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(imageId, ColumnMetadata.named("image_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(imageUrl, ColumnMetadata.named("image_url").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(momentId, ColumnMetadata.named("moment_id").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

