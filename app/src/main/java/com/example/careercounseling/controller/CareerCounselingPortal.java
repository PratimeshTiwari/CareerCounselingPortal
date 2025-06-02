package com.example.careercounseling.controller;
import com.example.careercounseling.model.Student;
import com.example.careercounseling.model.User;
import com.example.careercounseling.service.GeminiAPIService;

import java.io.*;
import java.util.*;

import java.util.Scanner;

public class CareerCounselingPortal {

    private GeminiAPIService geminiService;
    private Scanner scanner;
    private Student currentStudent; // Changed to class-level variable
    private List<User> users;
    private static final String USER_DATA_FILE = "users.dat";

    public CareerCounselingPortal() {
        geminiService = new GeminiAPIService();
        scanner = new Scanner(System.in);
        currentStudent = null;
        users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private List<User> loadUsers() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load users, starting with empty list: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    private void logIn() {
        System.out.println("\n--- Log In ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                if (u instanceof Student) {
                    currentStudent = (Student) u;
                    System.out.println("Login successful! Welcome back, " + u.getName());
                    return;
                }
            }
        }
        System.out.println("Invalid credentials or user not found. Please try again.");
    }

    private void signUp() {
        System.out.println("\n--- Sign Up ---");
        String id = "STU" + (System.currentTimeMillis() % 1000);

        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("Academic Details:\n");
        System.out.print("  Major/Degree: ");
        String major = scanner.nextLine();
        System.out.print("  Current GPA: ");
        String gpa = scanner.nextLine();
        String academicProfile = major + ", GPA: " + gpa;

        Student student = new Student(id, name, email, academicProfile);
        student.setPassword(password);
        users.add(student);
        saveUsers();   // Persist new user
        currentStudent = student;

        System.out.println("Sign up successful! You are now logged in as " + name + ".");
    }

    public void run() {
        while (true) {
            if (currentStudent == null) {
                System.out.println("\n--- Welcome to Career Counseling Portal ---");
                System.out.println("1. Sign Up");
                System.out.println("2. Log In");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        signUp();
                        break;
                    case "2":
                        logIn();
                        break;
                    case "3":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } else {
                runCareerCounselingDemo();
                currentStudent = null;
            }
        }
    }


    public Student createStudentProfile() {
        System.out.println("\n--- Student Profile Creation ---");

        String id = "STU" + System.currentTimeMillis() % 1000;

        System.out.print("Enter your full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email address: ");
        String email = scanner.nextLine();

        System.out.println("Enter your academic details:");
        System.out.print("Major/Degree: ");
        String major = scanner.nextLine();

        System.out.print("Current GPA: ");
        String gpa = scanner.nextLine();

        String academicProfile = major + ", GPA: " + gpa;

        currentStudent = new Student(id, name, email, academicProfile);
        return currentStudent;
    }

    public void runCareerCounselingDemo() {
        while (true) {
            System.out.println("\n--- Career Counseling Menu ---");
            System.out.println("1. Get Career Suggestion");
            System.out.println("2. Log Out");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    processUserInput(currentStudent);
                    break;
                case "2":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void processUserInput(Student student) {
        System.out.println("\n--- Career Suggestion ---");
        String userData = "Name: " + student.getName() +
                ", Academic Profile: " + student.getAcademicProfile();

        String suggestion = geminiService.getCareerSuggestion(userData);
        System.out.println("Career Suggestion for " + student.getName() + ": " + suggestion);

        System.out.println("\nWould you like more details about this career path?");
        System.out.print("Enter 'Y' for Yes or 'N' for No: ");
        String moreDetails = scanner.nextLine();

        if (moreDetails.equalsIgnoreCase("Y")) {
            String detailedSuggestion = geminiService.getDetailedCareerPathway(suggestion);
            System.out.println("\n--- Detailed Career Pathway ---");
            System.out.println(detailedSuggestion);
        }
    }

    public static void main(String[] args) {
        CareerCounselingPortal portal = new CareerCounselingPortal();
        portal.run();
    }
}
