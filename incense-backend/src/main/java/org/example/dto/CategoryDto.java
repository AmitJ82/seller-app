
// src/main/java/com/ecommerce/sellerapp/dto/CategoryDto.java
package org.example.dto;

import java.util.Objects;

public class CategoryDto {
    private Long id;
    private String label;
    private String name;
    private String description;
    private String image;
    private String comments;

    // Constructors, Getters and Setters
    public CategoryDto() {}

    public CategoryDto(String label, String name, String description, String image, String comments) {
        this.label = label;
        this.name = name;
        this.description = description;
        this.image = image;
        this.comments = comments;
    }

    public CategoryDto(Long id, String label, String name, String description, String image, String comments) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.description = description;
        this.image = image;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDto that = (CategoryDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}