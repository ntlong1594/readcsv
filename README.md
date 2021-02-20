This project is used to read the csv file which contain phone number , activation & deactivation date and then extract it to activate 

# Pre condition before starting
- Using java 1.8 , please ensure you already installed jdk & jre 1.8
- Using maven as package manager tool, please ensure you already installed maven.

# Instruction
- Before starting , please run `mvn clean install`
- The project can be start in Main.java in `src/main/java/com/hometest/readcsv/Main.java`
- The code is tested in test package in `src/test/java/come/hometest`
- It's developed on MacOs BigSur version 11.2.1 with IntelliJ IDE.

# Algorithm describe:


- Read csv file and load to memory
- We use MAP<String, SortedSet<String>> to store the data loaded from CSV file which KEY is phone number and Value will be SORTED_SET of Activation & Deactivation Date
  for example: 
  ```
  {
      "0987000002": [
           "2016-02-01,2016-03-01",
           "2016-03-01,2016-05-01",
           "2016-05-01,"
      ]
  }
  ```
- So now we can for the phone number "0987000002" , the deactivationDate of the previous element will be equals to the activationDate of next element.
- Base on that logical, we can identify actual activationDate and return another result Map<String, String> (Key = phonenumber , Value = actualActivationDate)
- For this algorithm we use 1 loop to iterate csv file and map to MAP<String, SortedSet<String>> and another 2 nested loop to iterate and build the final result. Therefore the final BigO complexity would be: n + (n * log(n)) = nlog(n)
- All of collection I use in this task is concurrent collection which mean it can handle it can handle multi-thread accessing & modifying, in case
we would like to reduce processing time, we could run it parallel when loading from csv and put it to Map. 