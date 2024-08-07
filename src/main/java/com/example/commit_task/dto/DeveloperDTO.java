package com.example.commit_task.dto;

import com.example.commit_task.entity.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDTO {

    private String userName;
    private String email;
    private List<Commit> commitList = new ArrayList<>();
}
