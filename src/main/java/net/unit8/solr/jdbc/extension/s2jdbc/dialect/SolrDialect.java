package net.unit8.solr.jdbc.extension.s2jdbc.dialect;

import net.unit8.solr.jdbc.extension.s2jdbc.types.ArrayType;
import net.unit8.solr.jdbc.extension.s2jdbc.types.ListType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.dialect.StandardDialect;

import javax.persistence.GenerationType;
import java.util.List;

public class SolrDialect extends StandardDialect {
	public final static ValueType ARRAY = new ArrayType(); 
	public final static ValueType LIST  = new ListType(); 

	@Override
	public String getName() {
	    return "solr";
	}

	@Override
	public boolean supportsLimit() {
	    return true;
	}

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (offset > 0) {
            buf.append(" limit ");
            buf.append(limit);
            buf.append(" offset ");
            buf.append(offset);
        } else {
            buf.append(" limit ");
            buf.append(limit);
        }
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean isInsertIdentityColumn() {
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return false;
    }

    @Override
    public boolean supportsSequence() {
        return false;
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
    	// TODO implementation
        return false;
    }

    @Override
    protected ValueType getValueTypeInternal(Class<?> clazz) {
    	if(List.class.isAssignableFrom(clazz)) {
    		return LIST;
    	}
    	return null;
    }

}
