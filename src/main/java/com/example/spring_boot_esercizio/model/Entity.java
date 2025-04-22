package com.example.spring_boot_esercizio.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Entity class represents a node composed of:
 * - id : String
 * - value : Double
 * - parents : List<String>
 */
@Node("Entity")
public class Entity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property(name = "value")
    private Double value;

    @Property(name = "parents")
    private List<String> parents;


    /* Constructors */
    public Entity() {
        this.id = null;
        this.value = null;
        this.parents = new ArrayList<String>();
    }

    public Entity(String id) {
        this.id = id;
        this.value = null;
        this.parents = new ArrayList<String>();
    }

    public Entity(String id, Double value) {
        this.id = id;
        this.value = value;
        this.parents = new ArrayList<String>();
    }

    public Entity(String id, Double value, List<String> parents) {
        this.id = id;
        this.value = value;
        this.parents = parents;
    }

    /* Getters and Setters */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public List<String> getParents() {
        return this.parents;
    }

    @Override
    public String toString() {
        return "\nID: " + this.id + "\n"
            + "VALUE: " + this.value + "\n"
            + "PARENTS: " + this.parents.toString() + "\n";
    }

    public String toJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            System.out.println(e);
            return "{}";
        }
    }
}