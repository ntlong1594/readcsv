package com.hometest.readcsv;

import com.hometest.readcsv.service.CsvSupportService;

import java.util.Map;

public class Main {

    private static final String INPUT_PATH = "input/input.csv";
    private static final String OUTPUT_PATH = "output.csv";

    /**
     * Algorithm describe:
     * We use MAP<String, SortedSet<String>> to store the data loaded from CSV file which KEY is phone number and Value will be SORTED_SET of Activation & Deactivation Date
     * for example: {
     * "0987000002": [
     *      "2016-02-01,2016-03-01",
     *      "2016-03-01,2016-05-01",
     *      "2016-05-01,"
     * ]
     * }
     * So now we can for the phone number "0987000002" , the deactivationDate of the previous element will be equals to the activationDate of next element.
     * Base on that logical, we can identify actual activationDate and return another result Map<String, String> (Key = phonenumber , Value = actualActivationDate)
     * For this algorithm we use 1 loop to iterate csv file and map to MAP<String, SortedSet<String>> and another 2 nested loop to iterate and build the final result
     * Therefore the final BigO complexity would be: n + (n * log(n)) = nlog(n)
     */
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        CsvSupportService csvSupportService = CsvSupportService.getInstance();
        Map<String, String> uniquePhoneNumberMap = csvSupportService.getUniquePhoneNumberMap(INPUT_PATH);
        csvSupportService.exportToCsv(uniquePhoneNumberMap, OUTPUT_PATH);
        long stopTime = System.currentTimeMillis();
        System.out.println("The output file is: " + OUTPUT_PATH + "\nExecution time: " + (stopTime - startTime) + " miliseconds");
    }

}
