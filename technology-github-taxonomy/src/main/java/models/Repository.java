package models;

import java.util.List;

public class Repository {
    private String name;
    private String fullName;
    private List<String> topics;

    public Repository(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public Repository(String name, String fullName, List<String> topics) {
        this.name = name;
        this.fullName = fullName;
        this.topics = topics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", topics=" + topics +
                '}';
    }
}

