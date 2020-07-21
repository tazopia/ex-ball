package spoon.support.dialect;

import org.hibernate.dialect.SQLServer2012Dialect;

import java.sql.Types;

public class SqlServerDialectWithNvarchar extends SQLServer2012Dialect {

    public SqlServerDialectWithNvarchar() {
        registerColumnType(Types.TIMESTAMP, "datetime");
    }

}
