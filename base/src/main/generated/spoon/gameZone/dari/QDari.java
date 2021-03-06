package spoon.gameZone.dari;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDari is a Querydsl query type for Dari
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDari extends EntityPathBase<Dari> {

    private static final long serialVersionUID = 1720423579L;

    public static final QDari dari = new QDari("dari");

    public final ArrayPath<long[], Long> amount = createArray("amount", long[].class);

    public final BooleanPath cancel = createBoolean("cancel");

    public final BooleanPath closing = createBoolean("closing");

    public final DateTimePath<java.util.Date> gameDate = createDateTime("gameDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath line = createString("line");

    public final StringPath oddeven = createString("oddeven");

    public final ArrayPath<double[], Double> odds = createArray("odds", double[].class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public final StringPath sdate = createString("sdate");

    public final StringPath start = createString("start");

    public QDari(String variable) {
        super(Dari.class, forVariable(variable));
    }

    public QDari(Path<? extends Dari> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDari(PathMetadata metadata) {
        super(Dari.class, metadata);
    }

}

