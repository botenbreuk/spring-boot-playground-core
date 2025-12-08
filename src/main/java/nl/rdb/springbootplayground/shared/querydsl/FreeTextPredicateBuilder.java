package nl.rdb.springbootplayground.shared.querydsl;

import static com.querydsl.core.types.ExpressionUtils.allOf;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeTextPredicateBuilder {

    protected static Predicate build(String searchArgument, StringPath searchField) {
        if (isNotBlank(searchArgument)) {
            List<Predicate> predicates = stream(split(searchArgument))
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .map(StringUtils::stripAccents)
                    .map(sa -> stringTemplate("CAST(unaccent({0}) as string)", searchField.lower())
                            .contains(sa))
                    .collect(toList());
            return allOf(predicates);
        }
        return null;
    }
}
