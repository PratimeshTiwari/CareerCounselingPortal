//package com.example.careercounseling.model;
//
//public abstract class User {
//    protected String id;
//    protected String name;
//    protected String email;
//    protected String password;        // Added password field
//
//    public User(String id, String name, String email) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//    }
//
//    // New constructor for including password
//    public User(String id, String name, String email, String password) {
//        this(id, name, email);
//        this.password = password;
//    }
//
//    // Getters and setters
//    public String getId() { return id; }
//    public void setId(String id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getPassword() { return password; }     // Added
//    public void setPassword(String password) { this.password = password; } // Added
//}


package com.example.careercounseling.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String email;
    protected String password;        // Added password field

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // New constructor for including password
    public User(String id, String name, String email, String password) {
        this(id, name, email);
        this.password = password;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }     // Added
    public void setPassword(String password) { this.password = password; } // Added
}