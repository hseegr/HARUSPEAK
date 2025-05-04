package com.haruspeak.api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CommaSeparatedToIntegerListConverter implements Converter<String, List<Integer>> {

    @Override
    public List<Integer> convert(String source) {
        if (source == null || source.isBlank()) return null;
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }
}

