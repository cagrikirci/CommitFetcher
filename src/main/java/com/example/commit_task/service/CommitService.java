package com.example.commit_task.service;

import com.example.commit_task.dto.DeveloperDTO;
import com.example.commit_task.entity.Commit;
import com.example.commit_task.repository.CommitRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommitService {

    private final CommitRepository commitRepository;
    private final GitHubClient gitHubClient;
    private final GitLabClient gitLabClient;

    public CommitService(CommitRepository commitRepository, GitHubClient gitHubClient, GitLabClient gitLabClient) {
        this.commitRepository = commitRepository;
        this.gitHubClient = gitHubClient;
        this.gitLabClient = gitLabClient;
    }

    public void fetchAndSaveGitHubCommits(String owner, String repo) {
        saveCommits(gitHubClient.getCommits(owner, repo), "GitHub", repo);
    }

    public void fetchAndSaveGitLabCommits(String projectId) {
        saveCommits(gitLabClient.getCommits(projectId), "GitLab", projectId);
    }

    private void saveCommits(String jsonResponse, String platform, String repo) {
        JSONArray commitsArray = new JSONArray(jsonResponse);

        for (int i = 0; i < commitsArray.length(); i++) {
           JSONObject commitObj = commitsArray.getJSONObject(i);

           String commitHash = commitObj.optString("sha",commitObj.optString("id", ""));
           String commitMessage = commitObj.optJSONObject("commit") != null
                   ? commitObj.getJSONObject("commit").optString("message", "")
                   : commitObj.optString("message", "");

           String commitDate = commitObj.optJSONObject("commit") != null
                   ? commitObj.getJSONObject("commit").getJSONObject("author").optString("date", "")
                   : commitObj.optString("created_at", "");

           LocalDateTime commitDateTime;
           if(!commitDate.isEmpty()) {
               commitDateTime = LocalDateTime.parse(commitDate, DateTimeFormatter.ISO_DATE_TIME);
           }
           else{
               commitDateTime = LocalDateTime.now();
           }

           String authorName = commitObj.optJSONObject("commit") != null
                   ? commitObj.getJSONObject("commit").getJSONObject("author").optString("name", "")
                   : commitObj.optString("author_name", "");

           String authorEmail = commitObj.optJSONObject("commit") != null
                   ? commitObj.getJSONObject("commit").getJSONObject("author").optString("email", "")
                   : commitObj.optString("author_email", "");

           Commit commit = new Commit();
           commit.setRepoName(repo);
           commit.setCommitHash(commitHash);
           commit.setCommitMessage(commitMessage);
           commit.setCommitDate(commitDateTime);
           commit.setAuthorName(authorName);
           commit.setAuthorEmail(authorEmail);
           commit.setPlatform(platform);

           commitRepository.save(commit);
        }
    }

    public List<DeveloperDTO> getDevelopersWithCommits(){
        List<Commit> commits = commitRepository.findAll();
        Map<String, DeveloperDTO> developersMap = new HashMap<>();

        for(Commit commit : commits) {
            String email = commit.getAuthorEmail();
            DeveloperDTO developerDTO = developersMap.get(email);

            if (developerDTO == null) {
                developerDTO = new DeveloperDTO();
                developerDTO.setEmail(email);
                developerDTO.setUserName(commit.getAuthorName());
                developerDTO.setCommitList(new ArrayList<>());
                developersMap.put(email, developerDTO);
            }
            developerDTO.getCommitList().add(commit);
        }
        return new ArrayList<>(developersMap.values());
    }
}
