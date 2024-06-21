import models.Repository;
import models.Topic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static String API_TOKEN = "......";

    public static void main(String[] args){
        GitHub gitHub = new GitHub();
        //List<Topic> topicList = gitHub.getTopicsFromGitHubAPI();

        //get topics from files
        List<Topic> topicList = getTopicsFromFiles();


        //search each topic's repository for an instance of another topic
        try {

            FileWriter writer = new FileWriter(new File("results.txt"));

            for(Topic topic: topicList){
                writer.append(topic.getDisplayName() +";"+ topic.getName() + System.lineSeparator());

                for(Repository repository: topic.getRepositoryList()){
                    if(repository.getName().equals(topic.getName()))
                        continue;

                    Optional<Topic> optionalTopic = topicList
                            .stream()
                            .filter(t->t.getName().equals(repository.getName()))
                            .findFirst();

                    if(optionalTopic.isPresent()){
                        writer.append(" --"+optionalTopic.get().getDisplayName() +";"+ optionalTopic.get().getName() + System.lineSeparator());
                    }
                }
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<Topic> getTopicsFromFiles() {

        try {
            List<String> contents = Files.readAllLines((new File("github-topics.csv")).toPath());
            List<Topic> topics = new ArrayList<>();
            for(int i=1; i<contents.size(); i++){
                topics.add(new Topic(contents.get(i)));
            }

            List<String> contents2 = Files.readAllLines((new File("github-topics-repositories.csv")).toPath());
            for(int i=1; i<contents2.size(); i++){
                String[] split = contents2.get(i).split(";");
                Topic t = topics.stream().filter(t1->t1.getName().equals(split[0])).findFirst().get();
                t.addRepository(new Repository(split[1],split[2]));
            }

            return topics;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
