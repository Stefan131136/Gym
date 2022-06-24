package com.example.gym_planner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bmijson {

    @SerializedName("bmi")
    @Expose
    private double bmi;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weightCategory")
    @Expose
    private String weightCategory;

    /**
     * No args constructor for use in serialization
     *
     */
    public Bmijson() {
    }

    /**
     *
     * @param weightCategory
     * @param weight
     * @param bmi
     * @param height
     */
    public Bmijson(double bmi, String weight, String height, String weightCategory) {
        super();
        this.bmi = bmi;
        this.weight = weight;
        this.height = height;
        this.weightCategory = weightCategory;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeightCategory() {
        return weightCategory;
    }

    public void setWeightCategory(String weightCategory) {
        this.weightCategory = weightCategory;
    }

}