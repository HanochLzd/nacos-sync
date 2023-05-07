package com.alibaba.nacossync.dao;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DMUpperCaseStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final long serialVersionUID = 1383021413247872469L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier physicalTableName = apply(name, context);
        // 将表名全部转换成大写
        String tableName = physicalTableName.getText().toUpperCase();
        return Identifier.toIdentifier("\"" + tableName + "\"");
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        Identifier physicalTableName = apply(name, context);
        // 将列名全部转换成大写
        String columnName = physicalTableName.getText().toUpperCase();
        return Identifier.toIdentifier("\"" + columnName + "\"");
    }

    /**
     * copy {@link SpringPhysicalNamingStrategy#apply(Identifier, JdbcEnvironment)}
     *
     * @param name
     * @param jdbcEnvironment
     * @return
     */
    private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));

            for (int i = 1; i < builder.length() - 1; ++i) {
                if (this.isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
                    builder.insert(i++, '_');
                }
            }

            return this.getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
        }
    }

    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        if (this.isCaseInsensitive(jdbcEnvironment)) {
            name = name.toLowerCase(Locale.ROOT);
        }

        return new Identifier(name, quoted);
    }

    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return true;
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

}