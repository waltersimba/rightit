package co.za.rightit.taxibook.spec.update;

import com.mongodb.BasicDBObject;

/**
 * The utility for creating update queries.
 */
public class UpdateQueryBuilder {

    /**
     * The update query.
     */
    private final BasicDBObject query;

    /**
     * Initializes the update builder.
     */
    public UpdateQueryBuilder() {
        query = new BasicDBObject();
    }

    /**
     * Sets the new value for the field.
     *
     * @param key    field name
     * @param object value to set
     * @return UpdateQueryBuilder instance
     */
    public UpdateQueryBuilder set(final String key,final Object object) {
        addToQuery(Operators.SET, key, object);
        return this;
    }

    /**
     * Increments the field by a specified value.
     *
     * @param key   field name
     * @param value number value
     * @return UpdateQueryBuilder instance
     */
    public UpdateQueryBuilder inc(final String key, final int value) {
        addToQuery(Operators.INC, key, value);
        return this;
    }

    /**
     * Deletes the field.
     *
     * @param key field name
     * @return UpdateQueryBuilder instance
     */
    public UpdateQueryBuilder unset(final String key) {
        addToQuery(Operators.UNSET, key, 1);
        return this;
    }

    /**
     * Checks whether the builder is empty.
     *
     * @return true if the builder is empty, false otherwise
     */
    public boolean isEmpty() {
        return query.keySet().isEmpty();
    }

    /**
     * Creates the BasicDBObject-based query to be used for update operations.
     *
     * @return the query instance
     */
    public BasicDBObject get() {
        return query;
    }

    /**
     * Adds the operation to the query.
     *
     * @param operator update operator
     * @param key      param to update
     * @param object   value to set
     */
    private void addToQuery(final String operator, final String key, final Object object) {
        final BasicDBObject subQuery = query.get(operator) != null ? (BasicDBObject) query.get(operator) : new BasicDBObject();
        query.put(operator, subQuery.append(key, object));
    }
}
