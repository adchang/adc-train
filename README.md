# adc-train

A java CLI app that calculates the quickest path between two stations.

## Problem

TODO

## [Brainstorming/Design](https://docs.google.com/document/d/1hraWvOwVA5EXrtfoamw7gSW507F7Sk_0gxjSHfLqQew/edit#heading=h.lar6olxsub5)

Recursively traverse through all possible paths, then calculate duration for each path to determine the quickest path.

### Features & Limitations
 - Bidirectional support: To auto-generate bidirectional data, add TODO flag
 - In the case of duplicate entries in the csv file TODO
 - TODO

### To Run App
    1. cd adc-train
    2. mvn package
    3. cd target
    3. java -cp adc-train-1.0.jar asia.crea.adc.train.adc-train.App
 
### To Run Test
    1. cd adc-train
    2. mvn clean test
   
## Requirments
- Apache Maven 3.6.0
- Java 1.8.0_201