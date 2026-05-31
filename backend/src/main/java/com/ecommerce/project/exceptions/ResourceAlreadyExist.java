package com.ecommerce.project.exceptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResourceAlreadyExist extends RuntimeException {
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceAlreadyExist() {
    }

    public ResourceAlreadyExist(String resourceName, String field, String fieldName) {
        super(String.format("%s already exist with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceAlreadyExist(String resourceName, String field, Long fieldId) {
        super(String.format("%s already exist with %s : %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceAlreadyExist(String s) {
    }
}
