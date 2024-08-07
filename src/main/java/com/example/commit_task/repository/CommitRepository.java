package com.example.commit_task.repository;

import com.example.commit_task.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {

    List<Commit> findByAuthorEmail(String email);

    List<Commit> findAllByCommitHash(String hash);
}
