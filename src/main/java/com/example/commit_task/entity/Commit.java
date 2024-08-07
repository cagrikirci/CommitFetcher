package com.example.commit_task.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="commits")
@Getter
@Setter
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repo_name")
    private String repoName;

    @Column(name = "commit_hash")
    private String commitHash;

    @Column(name = "commit_message")
    private String commitMessage;

    @Column(name = "commit_date")
    private LocalDateTime commitDate;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_email")
    private String authorEmail;

    @Column(name = "platform")
    private String platform;
}
