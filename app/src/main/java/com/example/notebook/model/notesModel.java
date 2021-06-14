package com.example.notebook.model;

public class notesModel {
    private String title;
    private String description;

    public notesModel() {
    }

    public notesModel(String title, String description) {
        this.title = title;
        this.description = description;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
