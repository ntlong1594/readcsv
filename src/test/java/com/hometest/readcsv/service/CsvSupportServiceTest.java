package com.hometest.readcsv.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CsvSupportServiceTest {

    @ParameterizedTest
    @MethodSource("testCase")
    public void getUniquePhoneNumberMap(String inputPath, Map<String, String> expected) throws Exception {
        // When
        Map<String, String> result = CsvSupportService.getInstance().getUniquePhoneNumberMap(inputPath);
        // Then
        assertThat(result.size()).isEqualTo(expected.size());
        for (String key : result.keySet()) {
            assertThat(result.get(key)).isEqualTo(expected.get(key));
        }
    }

    private static Stream<Arguments> testCase() {
        return Stream.of(
                Arguments.of("input/input-testcase-1.csv", Stream.of(
                        new AbstractMap.SimpleEntry<>("0987000001", "2016-06-01"),
                        new AbstractMap.SimpleEntry<>("0987000002", "2016-02-01"),
                        new AbstractMap.SimpleEntry<>("0987000003", "2016-01-01"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))),
                Arguments.of("input/input-testcase-2.csv", Stream.of(
                        new AbstractMap.SimpleEntry<>("0987000002", "2016-05-01"),
                        new AbstractMap.SimpleEntry<>("0987000003", "2016-07-01"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))),
                Arguments.of("input/input-testcase-3.csv", Stream.of(
                        new AbstractMap.SimpleEntry<>("0987000001", "2016-06-01"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
        );
    }

    @Test
    public void exportToCsvShouldWorkCorrectly() throws IOException {
        // Given
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("123", "2020-01-01");
        dataMap.put("456", "2019-01-01");
        dataMap.put("789", "2018-01-01");
        String output = "test.csv";
        // When
        CsvSupportService.getInstance().exportToCsv(dataMap, output);
        // Then
        assertThat(Files.exists(Paths.get(output))).isTrue();
        String lines[] = readFromInputStream(new FileInputStream(output)).split("\\r?\\n");
        assertThat(lines.length).isEqualTo(4);
        assertThat(lines[0]).isEqualTo("\"PHONE_NUMBER, ACTIVATION_DATE\"");
        assertThat(lines[1]).isEqualTo("\"123\",\"2020-01-01\"");
        assertThat(lines[2]).isEqualTo("\"456\",\"2019-01-01\"");
        assertThat(lines[3]).isEqualTo("\"789\",\"2018-01-01\"");
        // Cleanup
        Files.deleteIfExists(Paths.get(output));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}