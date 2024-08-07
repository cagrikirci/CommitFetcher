package com.example.commit_task.controller;

import com.example.commit_task.dto.DeveloperDTO;
import com.example.commit_task.entity.Commit;
import com.example.commit_task.repository.CommitRepository;
import com.example.commit_task.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommitController {

    @Autowired
    private final CommitService commitService;

    @Autowired
    private final CommitRepository commitRepository;

    public CommitController(CommitService commitService, CommitRepository commitRepository, CommitRepository commitRepository1) {
        this.commitService = commitService;
        this.commitRepository = commitRepository;
    }

    @GetMapping("fetch-github-commits")
    public String fetchGitHubCommits(@RequestParam String owner, @RequestParam String repo){
        commitService.fetchAndSaveGitHubCommits(owner, repo);
        return "success";
    }

    @GetMapping("fetch-gitlab-commits")
    public String fetchGitLabCommits(@RequestParam String projectId){
        commitService.fetchAndSaveGitLabCommits(projectId);
        return "success";
    }

    @GetMapping("/developers")
    public String getDevelopers(Model model) {
        List<DeveloperDTO> developers = commitService.getDevelopersWithCommits();
        model.addAttribute("developers", developers);
        return "developers";
    }

    @GetMapping("developers/{email}")
    public String getDeveloperCommits(@PathVariable String email, Model model) {
        List<Commit> commitList = commitRepository.findByAuthorEmail(email);
        model.addAttribute("commitList", commitList);
        model.addAttribute("email", email);
        return "commitList";
    }

    @GetMapping("/commits/{hash}")
    public String getCommitDetails(@PathVariable String hash, Model model) {
        List<Commit> commits = commitRepository.findAllByCommitHash(hash);
        model.addAttribute("commits", commits);
        return "commitDetails";
    }
}
