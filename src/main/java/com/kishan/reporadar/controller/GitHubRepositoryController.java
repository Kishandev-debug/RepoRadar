
package com.kishan.reporadar.controller;

import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.entity.RepositoryEntity;
import com.kishan.reporadar.service.RepositoryService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    private final RepositoryService repositoryService;

    public GitHubRepositoryController(
            RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // Search GitHub repositories and save results
    @PostMapping("/search")
    public ResponseEntity<List<RepositoryEntity>> searchRepositories(
            @Valid @RequestBody SearchRequest request) {

        List<RepositoryEntity> repositories =
                repositoryService.searchAndSaveRepositories(request);

        return ResponseEntity.ok(repositories);
    }

    // Retrieve saved repositories with optional filters
    @GetMapping("/repositories")
    public ResponseEntity<List<RepositoryEntity>> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort) {

        List<RepositoryEntity> repositories =
                repositoryService.getStoredRepositories(
                        language,
                        minStars,
                        sort
                );

        return ResponseEntity.ok(repositories);
    }
}
