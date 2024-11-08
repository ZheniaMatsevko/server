package com.example.eventsmanager.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ImagesManager {
    private final static String PREFIX_FOR_USER_FOLDER = "user_";
    private final static String PATH_TO_IMAGE = "/data/" + PREFIX_FOR_USER_FOLDER;
    private final static String ROOT_PATH = new File("").getAbsolutePath() + "\\client\\events-manager\\public\\data";

    public static void createFolderForUser(Long id) {
        // Create the folder name using the user's ID
        String folderName = PREFIX_FOR_USER_FOLDER + id;
        Path userFolderPath = Paths.get(ROOT_PATH, folderName);

        try {
            // Check if the folder doesn't exist, then create it
            if (Files.notExists(userFolderPath)) {
                Files.createDirectory(userFolderPath);
                System.out.println("Folder created successfully for user with ID " + id);
            } else {
                System.out.println("Folder already exists for user with ID " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating folder for user with ID " + id);
        }
    }

    public static void deleteImage(String path) throws IOException {
        if (path != null && !path.isEmpty()) {
            String[] parts = path.split("/");
            String pathToDelete = ROOT_PATH;
            for (int i = 2; i < parts.length; i++) {
                pathToDelete = pathToDelete.concat("\\" + parts[i]);
            }
            Path filePath = Paths.get(pathToDelete);
            Files.delete(filePath);
        }
    }

    public static void deleteUserFolder(Long userId) throws IOException {
        String folderName = PREFIX_FOR_USER_FOLDER + userId;
        Path userFolderPath = Paths.get(ROOT_PATH, folderName);

        try {
            // Check if the folder exists, then delete it recursively
            if (Files.exists(userFolderPath)) {
                Files.walk(userFolderPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                System.out.println("Folder deleted successfully for user with ID " + userId);
            } else {
                System.out.println("Folder does not exist for user with ID " + userId);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting folder for user with ID " + userId);
        }
    }

    public static String saveProfileImage(MultipartFile file, Long userId) throws IOException {
        String imageName = "profile." + getFileExtension(file.getOriginalFilename());
        String pathInProject = "\\" + PREFIX_FOR_USER_FOLDER + userId + "\\" + imageName;
        String imagePath = ROOT_PATH + pathInProject;
        Path imageFilePath = Paths.get(imagePath);

        // Delete existing image if it exists
        if (Files.exists(imageFilePath)) {
            try {
                Files.delete(imageFilePath);
                System.out.println("Existing image deleted successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error deleting existing image");
                // Handle error if needed
            }
        }
        file.transferTo(new File(imagePath));
        return PATH_TO_IMAGE + userId + "/" + imageName;
    }

    public static String saveEventImage(MultipartFile file, Long userId, Long eventId) throws IOException {
        String folderName = PREFIX_FOR_USER_FOLDER + userId + "\\events";
        Path userFolderPath = Paths.get(ROOT_PATH, folderName);

        try {
            // Check if the folder doesn't exist, then create it
            if (Files.notExists(userFolderPath)) {
                Files.createDirectory(userFolderPath);
                System.out.println("Folder created successfully for events");
            } else {
                System.out.println("Folder already exists for events");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating folder for events");
        }
        String imageName = eventId + "." + getFileExtension(file.getOriginalFilename());
        String pathInProject = "\\" + PREFIX_FOR_USER_FOLDER + userId + "\\events\\" + imageName;
        String imagePath = ROOT_PATH + pathInProject;
        Path imageFilePath = Paths.get(imagePath);

        // Delete existing image if it exists
        if (Files.exists(imageFilePath)) {
            try {
                Files.delete(imageFilePath);
                System.out.println("Existing image deleted successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error deleting existing image");
                // Handle error if needed
            }
        }

        file.transferTo(new File(imagePath));
        return PATH_TO_IMAGE + userId + "/events/" + imageName;
    }

    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return ""; // No file extension found
    }
}
