package org.intelligentjava.kaggle.main;

import static org.intelligentjava.machinelearning.decisiontree.feature.P.betweenD;
import static org.intelligentjava.machinelearning.decisiontree.feature.P.lessThanD;
import static org.intelligentjava.machinelearning.decisiontree.feature.P.moreThan;
import static org.intelligentjava.machinelearning.decisiontree.feature.P.moreThanD;
import static org.intelligentjava.machinelearning.decisiontree.feature.P.startsWith;
import static org.intelligentjava.machinelearning.decisiontree.feature.PredicateFeature.newFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.intelligentjava.machinelearning.decisiontree.DecisionTree;
import org.intelligentjava.machinelearning.decisiontree.data.DataSample;
import org.intelligentjava.machinelearning.decisiontree.data.SimpleDataSample;
import org.intelligentjava.machinelearning.decisiontree.feature.Feature;
import org.intelligentjava.machinelearning.decisiontree.label.BooleanLabel;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import com.google.common.collect.Lists;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
    	
    	//execution time measurement
    	Instant start = Instant.now();
    	
    	//memory usage
    	long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    	System.out.println("Before execution: " + beforeUsedMem);
    	
    	
        List<DataSample> trainingData = readData(true);
        DecisionTree tree = new DecisionTree();
        
        List<Feature> features = getFeatures();

        tree.train(trainingData, features);
        
        // print tree after training
        tree.printTree();
        
        // read test data
        List<DataSample> testingData = readData(false);
        List<String> predictions = Lists.newArrayList();
        // classify all test data
        for (DataSample dataSample : testingData) {
            predictions.add(dataSample.getValue("id").get() + ";" + tree.classify(dataSample).getPrintValue());
        }
        
        // write predictions to file
        FileWriter fileWriter = new FileWriter(new File("resources/predictions.csv"));
        fileWriter.append("id;YES").append("\n");
        for (String prediction : predictions) {
            fileWriter.append(prediction).append("\n");
        }
        fileWriter.flush();
        fileWriter.close();
      
        List<String> expectedList = new ArrayList<>();
        Scanner scannerE = new Scanner(new File("resources/expected.csv"));
        while (scannerE.hasNextLine()) {
           String line = scannerE.nextLine();
           expectedList.add(line);
        }
           
         List<String> predictionList = new ArrayList<>();
         Scanner scannerP = new Scanner(new File("resources/predictions.csv"));
         while (scannerP.hasNextLine()) {
        	 String linee = scannerP.nextLine();
        	 predictionList.add(linee);
        }
        int good = 0;
        int idExpected;
        int value;
        int idPredicted;
        int valuePredicted;
        for(int i = 1; i < expectedList.size(); i++) {
        	String[] splitExpected = expectedList.get(i).split(";");
        	idExpected = Integer.parseInt(splitExpected[0]);
        	value = Integer.parseInt(splitExpected[1]);
        	for(int j = 1; j < predictionList.size(); j++) {
            	String[] splitPredicted = predictionList.get(j).split(";");
            	idPredicted = Integer.parseInt(splitPredicted[0]);
            	valuePredicted = Integer.parseInt(splitPredicted[1]);
            	if(idExpected == idPredicted) {
            		if(value == valuePredicted) {
            			good++;
            			break;
            		}
            	}
        	}
        }
        System.out.println(good + "/" + (predictionList.size()-1)+"  -  "+((float)good/(predictionList.size()-1)*100)+"%");

        //execution time measurement
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println("Elapsed time in seconds: "+timeElapsed);
        
    	long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    	System.out.println("After execution: " + afterUsedMem);
    	double x = afterUsedMem-beforeUsedMem;
        
    	System.out.println("Memory used: " + x/1024/1024+"Mb");
        
    }
    
    private static List<Feature> getFeatures() {
    	
        Feature eNBcellConfig0dlBandwidth25 = newFeature("eNBcellConfig0dlBandwidth", 25);
        Feature eNBcellConfig0dlBandwidth50 = newFeature("eNBcellConfig0dlBandwidth", 50);
        Feature eNBcellConfig0dlBandwidth100 = newFeature("eNBcellConfig0dlBandwidth", 100);
        Feature mac_stats_total_bytes_sdus_ul1 = newFeature("mac_stats_total_bytes_sdus_ul", lessThanD(676510), "less than 676510");
        Feature mac_stats_total_bytes_sdus_ul2 = newFeature("mac_stats_total_bytes_sdus_ul", betweenD(676510, 1013564), "between 676510 and 1013564");
        Feature mac_stats_total_bytes_sdus_ul3 = newFeature("mac_stats_total_bytes_sdus_ul", betweenD(1013564, 1421581), "between 1013564 and 1421581");
        Feature mac_stats_total_bytes_sdus_ul4 = newFeature("mac_stats_total_bytes_sdus_ul", betweenD(1421581, 2061991), "between 1421581 and 2061991");
        Feature mac_stats_total_bytes_sdus_ul5 = newFeature("mac_stats_total_bytes_sdus_ul", betweenD(2061991, 368851552), "between 2061991 and 368851552");
        Feature mac_stats_total_bytes_sdus_ul6 = newFeature("mac_stats_total_bytes_sdus_ul", betweenD(368851552, 769558814), "between 368851552 and 769558814");
        Feature mac_stats_total_bytes_sdus_ul7 = newFeature("mac_stats_total_bytes_sdus_ul", moreThanD(769558814), "more than 769558814");
        Feature mac_stats_total_bytes_sdus_dl1 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(22947, 811077), "between 22947 and 811077");
        Feature mac_stats_total_bytes_sdus_dl2 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(811077, 1366987), "between 811077 and 1366987");
        Feature mac_stats_total_bytes_sdus_dl3 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(1366987, 153149709), "between 1366987 and 153149709");
        Feature mac_stats_total_bytes_sdus_dl4 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(153149709, 438259102), "between 153149709 and 438259102");
        Feature mac_stats_total_bytes_sdus_dl5 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(438259102, 574243368), "between 438259102 and 574243368");
        Feature mac_stats_total_bytes_sdus_dl6 = newFeature("mac_stats_total_bytes_sdus_dl", betweenD(574243368, 959924893), "between 574243368 and 959924893");
        Feature eNBcellConfig0siConfigsfn1 = newFeature("eNBcellConfig0siConfigsfn", 149);
        Feature eNBcellConfig0siConfigsfn2 = newFeature("eNBcellConfig0siConfigsfn", 501);
        Feature eNBcellConfig0siConfigsfn3 = newFeature("eNBcellConfig0siConfigsfn", 549);
        Feature eNBcellConfig0siConfigsfn4 = newFeature("eNBcellConfig0siConfigsfn", 625);
        Feature eNBcellConfig0siConfigsfn5 = newFeature("eNBcellConfig0siConfigsfn", 848);
        Feature eNBcellConfig0siConfigsfn6 = newFeature("eNBcellConfig0siConfigsfn", 925);
        Feature eNBcellConfig0ulPuschPower1 = newFeature("eNBcellConfig0ulPuschPower", -96);
        Feature eNBcellConfig0ulPuschPower2 = newFeature("eNBcellConfig0ulPuschPower", -86);
        Feature eNBcellConfig0ulPuschPower3 = newFeature("eNBcellConfig0ulPuschPower", -50);
        Feature eNBcellConfig0ulFreq = newFeature("eNBcellConfig0ulFreq", 2565);
        Feature LClcUeConfig0rnti = newFeature("LClcUeConfig0rnti", 5268);
        Feature eNBcellConfig0dlFreq = newFeature("eNBcellConfig0dlFreq", 2685);
       
       return Arrays.asList(eNBcellConfig0dlBandwidth25, eNBcellConfig0dlBandwidth50, eNBcellConfig0dlBandwidth100, mac_stats_total_bytes_sdus_ul1,
    		   mac_stats_total_bytes_sdus_ul2, mac_stats_total_bytes_sdus_ul3, mac_stats_total_bytes_sdus_ul4, mac_stats_total_bytes_sdus_ul5, 
    		   mac_stats_total_bytes_sdus_ul6, mac_stats_total_bytes_sdus_ul7, mac_stats_total_bytes_sdus_dl1, mac_stats_total_bytes_sdus_dl2,
    		   mac_stats_total_bytes_sdus_dl3, mac_stats_total_bytes_sdus_dl4, mac_stats_total_bytes_sdus_dl5, mac_stats_total_bytes_sdus_dl6,
    		   eNBcellConfig0siConfigsfn1, eNBcellConfig0siConfigsfn2, eNBcellConfig0siConfigsfn3, eNBcellConfig0siConfigsfn4, eNBcellConfig0siConfigsfn5,
    		   eNBcellConfig0siConfigsfn6, eNBcellConfig0ulPuschPower1, eNBcellConfig0ulPuschPower2, eNBcellConfig0ulPuschPower3, 
    		   eNBcellConfig0ulFreq, LClcUeConfig0rnti, eNBcellConfig0dlFreq);
    }
    
    private static List<DataSample> readData(boolean training) throws IOException {
        List<DataSample> data = Lists.newArrayList();
        String filename = training ? "resources/train.csv" : "resources/test.csv";
        File file = new File(filename);
        InputStreamReader stream = new InputStreamReader(new FileInputStream(file));
        try (ICsvListReader listReader = new CsvListReader(stream, CsvPreference.STANDARD_PREFERENCE);) {
            
            // the header elements are used to map the values to the bean (names must match)
            final String[] header = listReader.getHeader(true);
            
            List<Object> values;
            while ((values = listReader.read(getProcessors(training))) != null) {
//                System.out.println(String.format("lineNo=%s, rowNo=%s, data=%s", listReader.getLineNumber(), listReader.getRowNumber(), values));
                data.add(SimpleDataSample.newSimpleDataSample("y", header, values.toArray()));
            }
        }
        return data;
    }
    
    private static CellProcessor[] getProcessors(boolean training) {
        // TODO fix this is ugly
        if (training) {
            final CellProcessor[] processors = new CellProcessor[] { 
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseDouble()),
                    new Optional(new ParseDouble()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseBooleanLabel())
            };
            return processors;
        } else {
            final CellProcessor[] processors = new CellProcessor[] { 
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseDouble()),
                    new Optional(new ParseDouble()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt()),
                    new Optional(new ParseInt())
            };
            return processors;
        }
    }
    
    private static class ParseBooleanLabel extends ParseBool {
        
        public Object execute(final Object value, final CsvContext context) {
            Boolean parsed = (Boolean)super.execute(value, context);
            return parsed ? BooleanLabel.TRUE_LABEL : BooleanLabel.FALSE_LABEL;
        } 
    }
}
