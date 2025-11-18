package com.example.gonepal;

import java.io.*;

public class UserData {
    private static final String CSV_FILE = "users.csv";
    private static final String DELIMITER = ",";

    public static void addUser(String fullName, String email, String password) {
        boolean fileExists = new File(CSV_FILE).exists();
        try (FileWriter fw = new FileWriter(CSV_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            if (!fileExists) {
                bw.write("FullName,Email,Password");
                bw.newLine();
            }
            String line = String.format("%s,%s,%s", fullName, email, password);
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] tokens = line.split(DELIMITER);
                if (tokens.length >= 2 && tokens[1].equalsIgnoreCase(email.trim())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateUser(String email, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] tokens = line.split(DELIMITER);
                if (tokens.length >= 3 && tokens[1].equalsIgnoreCase(email.trim()) && tokens[2].equals(password.trim())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // New method to get user's full name by email
    public static String getUserName(String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; }
                String[] tokens = line.split(DELIMITER);
                if (tokens.length >= 3 && tokens[1].equalsIgnoreCase(email.trim())) {
                    return tokens[0]; // Return the full name (first column)
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("User data file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "User"; // Default fallback name
    }
}