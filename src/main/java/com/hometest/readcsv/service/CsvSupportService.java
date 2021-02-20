package com.hometest.readcsv.service;

import com.hometest.readcsv.binder.PhoneNumberUsageTracking;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.input.BOMInputStream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CsvSupportService {

    private static CsvSupportService INSTANCE;

    private static final String CHARSET_NAME = "UTF-8";
    private static final String[] OUTPUT_HEADERS = {"PHONE_NUMBER, ACTIVATION_DATE"};
    private static final String DELIMITER = ",";
    private static final int ACTIVATION_DATE_POSITION = 0;
    private static final int DEACTIVATION_DATE_POSITION = 1;

    public static synchronized CsvSupportService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CsvSupportService();
        }
        return INSTANCE;
    }

    public void exportToCsv(Map<String, String> dataMap, String output) throws IOException {
        List<String[]> dataSource = new ArrayList<>();
        dataSource.add(OUTPUT_HEADERS);
        dataMap.forEach((key, value) -> {
            String[] record = {key, value};
            dataSource.add(record);
        });
        try (CSVWriter writer = new CSVWriter(new FileWriter(output))) {
            writer.writeAll(dataSource);
        }
    }


    public Map<String, String> getUniquePhoneNumberMap(final String csvFilePath) throws Exception {
        try (InputStreamReader ip = new InputStreamReader(new BOMInputStream(getClass().getClassLoader().getResourceAsStream(csvFilePath)), CHARSET_NAME)) {
            Map<String, SortedSet<String>> sortedSetMap = new ConcurrentHashMap<>();
            CsvToBean<PhoneNumberUsageTracking> csvToBean = new CsvToBeanBuilder<PhoneNumberUsageTracking>(ip)
                    .withType(PhoneNumberUsageTracking.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            Iterator<PhoneNumberUsageTracking> iterator = csvToBean.iterator();
            while (iterator.hasNext()) {
                PhoneNumberUsageTracking csvObject = iterator.next();
                buildPhoneNumberMapSet(sortedSetMap, csvObject);
            }
            return Collections.unmodifiableMap(buildResultMap(sortedSetMap));
        }
    }

    private void buildPhoneNumberMapSet(final Map<String, SortedSet<String>> map,
                                        final PhoneNumberUsageTracking phoneNumberUsageTracking) {
        if (map.get(phoneNumberUsageTracking.getPhoneNumber()) == null) {
            map.put(phoneNumberUsageTracking.getPhoneNumber(), new TreeSet<>());
        }
        map.get(phoneNumberUsageTracking.getPhoneNumber()).add(phoneNumberUsageTracking.getActivationDate().concat(DELIMITER).concat(phoneNumberUsageTracking.getDeactivationDate()));
    }

    private Map<String, String> buildResultMap(final Map<String, SortedSet<String>> sortedSetMap) {
        Map<String, String> result = new HashMap<>();
        sortedSetMap.forEach((phoneNumber, listDateRange) -> result.put(phoneNumber, findActualActivationDate(listDateRange)));
        return result;
    }

    private String findActualActivationDate(SortedSet<String> listDateRange) {
        String deactivatedDate = null;
        String actualActivatedDate = null;
        if (listDateRange.size() == 1) {
            return listDateRange.first().split(DELIMITER)[ACTIVATION_DATE_POSITION];
        }

        for (String dateRange : listDateRange) {
            if (actualActivatedDate == null && deactivatedDate == null) {
                actualActivatedDate = dateRange.split(DELIMITER)[ACTIVATION_DATE_POSITION];
                deactivatedDate = dateRange.split(DELIMITER)[DEACTIVATION_DATE_POSITION];
                continue;
            }
            String activateDate = dateRange.split(DELIMITER)[ACTIVATION_DATE_POSITION];
            if (!activateDate.equals(deactivatedDate)) {
                return activateDate;
            }
            deactivatedDate = dateRange.split(DELIMITER).length == 2 ? dateRange.split(DELIMITER)[DEACTIVATION_DATE_POSITION] : null;
        }
        return actualActivatedDate;
    }
}
