package com.zio.ziospace.data;

import java.util.ArrayList;

public class ModelZBit {

    private String name, id;
    private ArrayList<ModelZBitTopic> modelZBitTopics;

    public ModelZBit() {
    }

    public ModelZBit(String name, String id, ArrayList<ModelZBitTopic> modelZBitTopics) {
        this.name = name;
        this.id = id;
        this.modelZBitTopics = modelZBitTopics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ModelZBitTopic> getTopics() {
        return modelZBitTopics;
    }

    public void setTopics(ArrayList<ModelZBitTopic> modelZBitTopics) {
        this.modelZBitTopics = modelZBitTopics;
    }
}

