# adc-train

A java CLI app that calculates the quickest path between two stations.

## Problem

Write a CLI app that:
- When run in a terminal, the app imports a routes.csv file and stores route information in
memory
- The app finds the shortest route based on time, and returns this value along with how
many stops it takes between the destinations
- App interface:
    - The user inputs a single starting train station
    - The user inputs a single ending train station
    - The resulting output should display number of stops and length of trip
- Utilize object oriented patterns
- Include unit tests
- Do not use any external libraries. We are looking for original code.
- No need to use any algorithms; keep it simple and have fun!

## [Brainstorming/Design](https://docs.google.com/document/d/1hraWvOwVA5EXrtfoamw7gSW507F7Sk_0gxjSHfLqQew/edit#heading=h.lar6olxsub5)

Recursively traverse through all possible paths, then calculate duration for each path to determine the quickest path.

### Features & Limitations
- In the case of duplicate entries in the csv file, duplicates will be ignored, ie. only the first entry will be used
- Bidirectional support: To auto-generate bidirectional data, add the following flag:
  
      --autogen-bidirections=T
- Show paths: To show all paths, add the following flag:
  
      --showPaths=T
- Show quickest path: To show the quickest path, add the following flag:
  
      --showQuickest=T

## App Details
### To Run App
    1. cd adc-train-master
    2. mvn package
    3. java -cp target/adc-train-1.0-jar-with-dependencies.jar asia.crea.adc.train.adc_train.App --file=sampleData.csv

or after building, you can run:

    ./train.sh --file=sampleData.csv

### To Run Test
    1. cd adc-train-master
    2. mvn clean test
   
### Requirements
- Apache Maven 3.6.0
- Java 1.8.0_201
