package com.example.nutritionlabeltest.custom;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.compose.ui.graphics.vector.VectorProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NutritionLabelParser {
    /*
    By using a switch statement and multiple sets I would say it is much less memory and dev friendly performant
    But it is more speed performant (by extension there for user friendly), as i can pick and choose which map to use
    to save iterating over for sure not used elements each time.

     */


    private Context context;

    private Queue parsingQueue = new PriorityQueue<String>();
    private HashMap<Nutrient, Pair<Double, NutrientMeasurement>> parsedNutrientsMap = new HashMap<>();

    private HashMap<Nutrient, Set<String>> macroSynonymsMap = new HashMap<>();

    public NutritionLabelParser(Context context) {
        this.context = context;

        //region Setting Parsing keywords
        try {
            InputStream filePath = context.getAssets().open("ParsingWords.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filePath));

            int line = 0;
            String synonyms = "";
            while ((synonyms = bufferedReader.readLine()) != null) {
                line++;

                if (line % 2 == 0) {
                    // Can check for synonyms
                    switch (line) {
                        case 2:
                            macroSynonymsMap.put(Nutrient.Calorie, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 4:
                            macroSynonymsMap.put(Nutrient.Protein, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 6:
                            macroSynonymsMap.put(Nutrient.TotalCarb, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 8:
                            macroSynonymsMap.put(Nutrient.DietaryFiber, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 10:
                            macroSynonymsMap.put(Nutrient.TotalSugar, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 12:
                            macroSynonymsMap.put(Nutrient.AddedSugar, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 14:
                            macroSynonymsMap.put(Nutrient.TotalFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 16:
                            macroSynonymsMap.put(Nutrient.SaturatedFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 18:
                            macroSynonymsMap.put(Nutrient.TransFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 20:
                            macroSynonymsMap.put(Nutrient.Cholesterol, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 22:
                            macroSynonymsMap.put(Nutrient.Sodium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                    }


                    System.out.println(line);
                }

            }
        } catch (IOException ex) {
            // Should not happen
        }
        //endregion


    }

    public void enqueueParse(String line) {
        if (shouldEnqueueParse(line)) {
            parsingQueue.add(line);
        }

    }

    private boolean shouldEnqueueParse(String line) {
        line = line.toLowerCase().trim();
        String regex = "([\\D\\s]+)\\s*(\\d+)(g|mg|mcg)";

        // Things to ignore for all cases
        if (line == null || line.isEmpty() || line.contains("%")) {
            return false;
        }

        // This method below falls short on things like
        // or 2g added sugar as it parses letters first then numbers

        // Nutrient by g, mg, mcg
        if (!line.contains("calories")) {
            Pattern r = Pattern.compile(regex);
            Matcher m = r.matcher(line);

            while (m.find()) {
                try {
                    String potentialNutrient = m.group(1).trim();
                    String potentialNutrientValue = m.group(2).trim();
                    String potentialNutrientMeasurement = m.group(3).trim();

                    for (Map.Entry<Nutrient, Set<String>> entry: macroSynonymsMap.entrySet()) {

                        //Try to remove "less than" or '>', where they appear on nutrition labels common nutrients (protein and carbs)
                        if (entry.getKey() == Nutrient.Protein || entry.getKey() == Nutrient.TotalCarb) {
                            String tmpPotentialNutrient = potentialNutrient;

                            potentialNutrient = potentialNutrient.replace("less than", "").trim();
                            potentialNutrient = potentialNutrient.replace(">", "").trim();

                            if (!tmpPotentialNutrient.equals(potentialNutrient)) {
                                // Default values less than one to zero
                                potentialNutrientValue = "0";
                            }
                        }

                        if (entry.getValue().contains(potentialNutrient) && parsedNutrientsMap.get(entry.getKey()) == null) {

                            parsedNutrientsMap.put(entry.getKey(), new Pair<>(Double.valueOf(potentialNutrientValue), Enum.valueOf(NutrientMeasurement.class, potentialNutrientMeasurement)));
                            Log.d("NORTH_NUTRIENT", "Added: " + entry.getKey().name() + " " + parsedNutrientsMap.get(entry.getKey()).first + parsedNutrientsMap.get(entry.getKey()).second);
                        } else {

                        }
                    }

                } catch (NullPointerException e) {
                    // False nutrient
                }

            }
        }


        // If it has a measurement and is not calories
        // if it does not have a percent
        // if it is not serving size

        return false;
    }

    public void parse() {
        while (!parsingQueue.isEmpty()) {
            // Check queue element for nutrient
            Log.d("NORTH_STRING", (String) parsingQueue.poll());
        }
    }



}
