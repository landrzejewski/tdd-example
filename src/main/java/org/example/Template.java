package org.example;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Template {

    private static final String EXPRESSION_START = "\\$\\{";
    private static final String EXPRESSION_END = "}";
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION_START + "\\w+" + EXPRESSION_END);
    private static final String INVALID_VALUE = ".*\\W+.*";

    private final String text;

    public Template(String text) {
        this.text = text;
        validateTemplateText();
    }

    private void validateTemplateText() {
        if (getUniqueExpressionsCount() != getExpressions().count()) {
            throw new IllegalArgumentException();
        }
    }

    public String evaluate(Map<String, String> parameters) {
        validate(parameters);
        return substitute(parameters);
    }

    private void validate(Map<String, String> parameters) {
        if (isIncomplete(parameters) || isNotValid(parameters)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNotValid(Map<String, String> parameters) {
        return parameters.values()
                .stream()
                .anyMatch(value -> value.matches(INVALID_VALUE));
    }

    private boolean isIncomplete(Map<String, String> parameters) {
        return parameters.size() != getExpressions().count();
    }

    private String substitute(Map<String, String> parameters) {
        var result = text;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            var expression = createExpression(entry.getKey());
            result = result.replaceAll(expression, entry.getValue());
        }
        return result;
    }

    private String createExpression(String key) {
        return EXPRESSION_START + key + EXPRESSION_END;
    }

    private Stream<MatchResult> getExpressions() {
        return EXPRESSION_PATTERN.matcher(text).results();
    }

    private int getUniqueExpressionsCount() {
        return getExpressions().map(MatchResult::group).collect(Collectors.toSet()).size();
    }

}
