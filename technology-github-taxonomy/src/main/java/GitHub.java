import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import models.Repository;
import models.Topic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GitHub {

    public GitHub(){}

    public List<Topic> getTopicsFromGitHubAPI(){
        Unirest.setTimeouts(0, 0);

        int page=0;
        List<Topic> topicList = new ArrayList<>();
        while (true) {
            page = page + 1;
            System.out.println("page: " + page);
            try {
                //make request to GitHub for topics
                HttpResponse<String> response = Unirest.get("https://api.github.com/search/topics?q=is:featured&per_page=100&page=" + page)
                        .header("Authorization", "Bearer "+Main.API_TOKEN)
                        .asString();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("HttpResponseCode: " + response.getStatus());
                }

                //get result
                Scanner sc = new Scanner(response.getRawBody());
                String inline = "";
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                sc.close();

                //parse, create and add topics
                try {
                    JSONParser parse = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parse.parse(inline);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                    if (jsonArray.size() == 0) {
                        break;
                    }
                    for(int i=0; i<jsonArray.size(); i++){
                        JSONObject jsonObjectItem = (JSONObject)jsonArray.get(i);
                        String display_name = jsonObjectItem.get("display_name").toString();
                        String name = jsonObjectItem.get("name").toString();
                        String short_description = jsonObjectItem.get("short_description").toString();

                        //get top repos of topic
                        List<Repository> repositories = getReposOfTopicFromGitHubAPI(name);

                        topicList.add( new Topic(display_name,name,short_description,repositories) );
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

        //write topics
        writeTopicsToFile(topicList);

        return topicList;
    }

    private List<Repository> getReposOfTopicFromGitHubAPI(String topicName) {
        Unirest.setTimeouts(0, 0);

        List<Repository> repositoryList = new ArrayList<>();
        try {
            //make request to GitHub for repositories of a given topic
            HttpResponse<String> response = Unirest.get("https://api.github.com/search/repositories?q=topic:" +topicName+ "&sort=stars&per_page=100&page=1")
                    .header("Authorization", "Bearer "+Main.API_TOKEN)
                    .asString();
            if (response.getStatus() != 200) {
                throw new RuntimeException("HttpResponseCode: " + response.getStatus());
            }

            //get result
            Scanner sc = new Scanner(response.getRawBody());
            String inline = "";
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();

            //parse, create and add repositories
            try {
                JSONParser parse = new JSONParser();
                JSONObject jsonObject = (JSONObject) parse.parse(inline);
                JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                for(int i=0; i<jsonArray.size(); i++) {
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    String name = jsonObjectItem.get("name").toString();
                    String full_name = jsonObjectItem.get("full_name").toString();

                    repositoryList.add( new Repository(name,full_name) );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        //wait between requests..
        try {
            Thread.sleep(5000); //5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return repositoryList;
    }

    private void writeTopicsToFile(List<Topic> topicList) {
        try {
            FileWriter writer = new FileWriter(new File("github-topics.csv"));
            writer.write("displayName;name;shortDescription" + System.lineSeparator());
            for(Topic topic: topicList){
                writer.append(topic.getDisplayName() +";"+ topic.getName() +";"+ topic.getShortDescription() + System.lineSeparator());
            }
            writer.close();

            writer = new FileWriter(new File("github-topics-repositories.csv"));
            writer.write("topicName;repositoryName;repositoryFullName" + System.lineSeparator());
            for(Topic topic: topicList){
                for (Repository repository: topic.getRepositoryList()){
                    writer.append(topic.getName() +";"+ repository.getName() +";"+ repository.getFullName() + System.lineSeparator() );
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
