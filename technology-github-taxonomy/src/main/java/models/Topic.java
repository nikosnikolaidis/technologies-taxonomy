package models;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String displayName;
    private String name;
    private String shortDescription;
    private List<Repository> repositoryList;

    public Topic(String displayName, String name, String shortDescription, List<Repository> repositoryList) {
        this.displayName = displayName;
        this.name = name;
        this.shortDescription = shortDescription;
        this.repositoryList = repositoryList;
    }

    public Topic(String fileLine) {
        String[] split = fileLine.split(";");
        this.displayName = split[0];
        this.name = split[1];
        this.shortDescription = split[2];
        this.repositoryList = new ArrayList<>();
    }

    public void addRepository(Repository repository){
        this.repositoryList.add(repository);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                '}';
    }
}
