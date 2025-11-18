package com.example.gonepal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AttractionData {
    private static Attraction selectedAttraction = null;

    public static List<Attraction> loadAttractionsFromCSV() {
        List<Attraction> attractions = new ArrayList<>();
        File csvFile = new File("attractions_data.csv");
        if (!csvFile.exists()) return attractions;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = parseCSVLine(line);
                if (parts.length >= 6) {
                    List<String> miniImages = new ArrayList<>();
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        miniImages.add(unescapeCSVField(parts[4]));
                    }
                    if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                        miniImages.add(unescapeCSVField(parts[5]));
                    }

                    attractions.add(new Attraction(
                            unescapeCSVField(parts[0]), // name
                            unescapeCSVField(parts[1]), // location
                            unescapeCSVField(parts[2]), // description
                            unescapeCSVField(parts[3]), // main image
                            miniImages
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attractions;
    }

    public static Attraction getSelectedAttraction() {
        return selectedAttraction;
    }

    public static void setSelectedAttraction(Attraction attraction) {
        selectedAttraction = attraction;
    }

    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Double quote - add single quote to field
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        result.add(currentField.toString());

        return result.toArray(new String[0]);
    }

    private static String unescapeCSVField(String field) {
        if (field == null) return "";

        field = field.trim();

        // Remove surrounding quotes if present
        if (field.startsWith("\"") && field.endsWith("\"") && field.length() > 1) {
            field = field.substring(1, field.length() - 1);
            // Unescape double quotes
            field = field.replace("\"\"", "\"");
        }

        // Convert \\n back to actual newlines
        field = field.replace("\\n", "\n");

        return field;
    }
}