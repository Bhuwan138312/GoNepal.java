package com.example.gonepal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.List;

public class AddedattractionController {
    @FXML
    public Button viewguides;
    @FXML
    public Button backbutton;
    @FXML
    private Label attractionLocationLabel;
    @FXML
    private TextArea attractionDescriptionArea;
    @FXML
    private ImageView mainImageView;
    @FXML
    private ImageView miniImageView1;
    @FXML
    private ImageView miniImageView2;
    @FXML
    private Label topBarTitleLabel;
    @FXML
    private ImageView nextattraction;

    @FXML
    public void initialize() {
        System.out.println("Initializing AddedattractionController...");

        // Apply rounded corners to main image
        if (mainImageView != null) {
            Rectangle clip = new Rectangle(373, 164);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            mainImageView.setClip(clip);
        }

        // Always show KDS image in nextattraction ImageView with fixed size and cropping
        loadKDSImage();

        // Apply rounded corners to nextattraction ImageView
        if (nextattraction != null) {
            Rectangle nextClip = new Rectangle(144, 92);
            nextClip.setArcWidth(20);
            nextClip.setArcHeight(20);
            nextattraction.setClip(nextClip);
        }

        // Get the selected attraction
        Attraction selected = AttractionData.getSelectedAttraction();
        if (selected == null) {
            System.out.println("No selected attraction found, loading from CSV...");
            // If no selected attraction, use the most recent one from CSV
            List<Attraction> attractions = AttractionData.loadAttractionsFromCSV();
            if (!attractions.isEmpty()) {
                selected = attractions.get(attractions.size() - 1);
                AttractionData.setSelectedAttraction(selected);
                System.out.println("Loaded attraction: " + selected.getName());
            }
        }

        if (selected != null) {
            System.out.println("Displaying attraction: " + selected.getName());
            loadAttractionDetails(selected);
        } else {
            System.out.println("No attraction data available!");
        }
    }

    private void loadAttractionDetails(Attraction attraction) {
        // Update attraction location label
        if (attractionLocationLabel != null) {
            attractionLocationLabel.setText(attraction.getLocation());
            System.out.println("Set location: " + attraction.getLocation());
        }

        // Update description
        if (attractionDescriptionArea != null) {
            String description = attraction.getDescription();
            // Convert \\n back to actual newlines for display (if not already converted)
            description = description.replace("\\n", "\n");
            attractionDescriptionArea.setText(description);
            System.out.println("Set description");
        }

        // Update top bar title with attraction name
        if (topBarTitleLabel != null) {
            topBarTitleLabel.setText(attraction.getName());
            System.out.println("Set title: " + attraction.getName());
        }

        // Update main image
        loadMainImage(attraction.getMainImagePath());

        // Update mini images
        loadMiniImages(attraction.getMiniImagePaths());
    }

    private void loadMainImage(String imagePath) {
        if (mainImageView != null && imagePath != null && !imagePath.isEmpty()) {
            try {
                System.out.println("Loading main image: " + imagePath);
                Image img = loadImageFromPath(imagePath);
                if (img != null) {
                    mainImageView.setImage(img);
                    mainImageView.setFitWidth(373);
                    mainImageView.setFitHeight(164);
                    mainImageView.setPreserveRatio(false);

                    // Center crop logic for better image display
                    double imgW = img.getWidth();
                    double imgH = img.getHeight();
                    double viewW = 373;
                    double viewH = 164;
                    double scale = Math.max(viewW / imgW, viewH / imgH);
                    double cropW = viewW / scale;
                    double cropH = viewH / scale;
                    double x = Math.max(0, (imgW - cropW) / 2);
                    double y = Math.max(0, (imgH - cropH) / 2);
                    mainImageView.setViewport(new Rectangle2D(x, y, cropW, cropH));
                    System.out.println("Main image loaded successfully");
                } else {
                    System.out.println("Failed to load main image");
                }
            } catch (Exception e) {
                System.err.println("Error loading main image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadMiniImages(List<String> miniImagePaths) {
        // Load first mini image
        if (miniImageView1 != null) {
            if (miniImagePaths.size() > 0 && !miniImagePaths.get(0).isEmpty()) {
                try {
                    System.out.println("Loading mini image 1: " + miniImagePaths.get(0));
                    Image img = loadImageFromPath(miniImagePaths.get(0));
                    if (img != null) {
                        miniImageView1.setImage(img);
                        miniImageView1.setFitWidth(53);
                        miniImageView1.setFitHeight(59);
                        miniImageView1.setPreserveRatio(true);
                        miniImageView1.setVisible(true);
                        System.out.println("Mini image 1 loaded successfully");
                    } else {
                        miniImageView1.setVisible(false);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading mini image 1: " + e.getMessage());
                    miniImageView1.setVisible(false);
                }
            } else {
                miniImageView1.setVisible(false);
            }
        }

        // Load second mini image
        if (miniImageView2 != null) {
            if (miniImagePaths.size() > 1 && !miniImagePaths.get(1).isEmpty()) {
                try {
                    System.out.println("Loading mini image 2: " + miniImagePaths.get(1));
                    Image img = loadImageFromPath(miniImagePaths.get(1));
                    if (img != null) {
                        miniImageView2.setImage(img);
                        miniImageView2.setFitWidth(53);
                        miniImageView2.setFitHeight(59);
                        miniImageView2.setPreserveRatio(true);
                        miniImageView2.setVisible(true);
                        System.out.println("Mini image 2 loaded successfully");
                    } else {
                        miniImageView2.setVisible(false);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading mini image 2: " + e.getMessage());
                    miniImageView2.setVisible(false);
                }
            } else {
                miniImageView2.setVisible(false);
            }
        }
    }

    private Image loadImageFromPath(String imagePath) {
        try {
            // Handle both file URI and regular file path
            if (imagePath.startsWith("file:")) {
                return new Image(imagePath);
            } else {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    return new Image(imageFile.toURI().toString());
                } else {
                    System.err.println("Image file does not exist: " + imagePath);
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating image from path: " + imagePath + " - " + e.getMessage());
            return null;
        }
    }

    private void loadKDSImage() {
        if (nextattraction != null) {
            try {
                System.out.println("Loading KDS image for nextattraction ImageView...");
                // Load the KDS image from resources
                Image kdsImage = new Image(getClass().getResource("images/Rectangle 46.png").toExternalForm());
                if (kdsImage != null) {
                    nextattraction.setImage(kdsImage);
                    nextattraction.setFitWidth(144);
                    nextattraction.setFitHeight(92);
                    nextattraction.setPreserveRatio(false);

                    // Center crop logic for better image display
                    double imgW = kdsImage.getWidth();
                    double imgH = kdsImage.getHeight();
                    double viewW = 144;
                    double viewH = 92;
                    double scale = Math.max(viewW / imgW, viewH / imgH);
                    double cropW = viewW / scale;
                    double cropH = viewH / scale;
                    double x = Math.max(0, (imgW - cropW) / 2);
                    double y = Math.max(0, (imgH - cropH) / 2);
                    nextattraction.setViewport(new Rectangle2D(x, y, cropW, cropH));

                    System.out.println("KDS image loaded successfully in nextattraction");
                } else {
                    System.out.println("Failed to load KDS image");
                }
            } catch (Exception e) {
                System.err.println("Error loading KDS image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void availableguides(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(getClass().getResource("availableguides.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotohomescreen(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(getClass().getResource("homescreen.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoanotherattraction(ActionEvent event) {
        try {
            javafx.scene.Parent nextRoot = javafx.fxml.FXMLLoader.load(getClass().getResource("arttractiondetails.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(nextRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}