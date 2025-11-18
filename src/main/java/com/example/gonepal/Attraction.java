package com.example.gonepal;

import java.util.List;

public class Attraction {
    private String name;
    private String location;
    private String description;
    private String mainImagePath;
    private List<String> miniImagePaths; // up to 3 mini images

    public Attraction(String name, String location, String description, String mainImagePath, List<String> miniImagePaths) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.mainImagePath = mainImagePath;
        this.miniImagePaths = miniImagePaths;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public String getMainImagePath() { return mainImagePath; }
    public List<String> getMiniImagePaths() { return miniImagePaths; }
}