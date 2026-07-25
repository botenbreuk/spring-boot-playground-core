package nl.rdb.springbootplayground.config;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

import org.springframework.boot.jackson.JacksonComponent;
import org.springframework.boot.jackson.ObjectValueSerializer;
import org.springframework.data.domain.Page;

/**
 * Defines the serialization to a JSON String from a Page object.
 *
 * @param <T> Type of entity in Page content list.
 * @author bas
 */
@JacksonComponent
public final class PageSerializer<T> extends ObjectValueSerializer<Page<T>> {

    /**
     * Serializes a Page object to a JSON String with one-based page number.
     */
    @Override
    protected void serializeObject(Page<T> page, JsonGenerator jgen, SerializationContext context) {
        context.defaultSerializeProperty("content", page.getContent(), jgen);
        context.defaultSerializeProperty("last", page.isLast(), jgen);
        context.defaultSerializeProperty("totalPages", page.getTotalPages(), jgen);
        context.defaultSerializeProperty("totalElements", page.getTotalElements(), jgen);
        context.defaultSerializeProperty("sort", page.getSort(), jgen);
        context.defaultSerializeProperty("numberOfElements", page.getNumberOfElements(), jgen);
        context.defaultSerializeProperty("first", page.isFirst(), jgen);
        context.defaultSerializeProperty("size", page.getSize(), jgen);
        context.defaultSerializeProperty("number", page.getNumber() + 1, jgen);
    }
}
