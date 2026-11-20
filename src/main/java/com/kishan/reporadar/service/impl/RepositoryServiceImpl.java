
package com.kishan.reporadar.service.impl;

import com.kishan.reporadar.client.GitHubApiClient;
import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.dto.response.GitHubApiResponse;
import com.kishan.reporadar.entity.RepositoryEntity;
import com.kishan.reporadar.mapper.RepositoryMapper;
import com.kishan.reporadar.repository.RepositoryJpaRepository;
import com.kishan.reporadar.repository.RepositorySpecifications;
import com.kishan.reporadar.service.RepositoryService;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    private final GitHubApiClient gitHubApiClient;
    private final RepositoryMapper repositoryMapper;
    private final RepositoryJpaRepository repositoryJpaRepository;

    public RepositoryServiceImpl(
            GitHubApiClient gitHubApiClient,
            RepositoryMapper repositoryMapper,
            RepositoryJpaRepository repositoryJpaRepository) {

        this.gitHubApiClient = gitHubApiClient;
        this.repositoryMapper = repositoryMapper;
        this.repositoryJpaRepository = repositoryJpaRepository;
    }

    // Search GitHub and save repositories in PostgreSQL
    @Override
    @Transactional
    public List<RepositoryEntity> searchAndSaveRepositories(
            SearchRequest request) {

        GitHubApiResponse response =
                gitHubApiClient.searchRepositories(request);

        List<RepositoryEntity> repositories = new ArrayList<>();

        if (response.getItems() == null || response.getItems().isEmpty()) {
            return repositories;
        }

        for (GitHubApiResponse.Item item : response.getItems()) {

            RepositoryEntity newEntity =
                    repositoryMapper.toEntity(item);

            RepositoryEntity existingEntity =
                    repositoryJpaRepository.findById(item.getId())
                            .orElse(null);

            if (existingEntity != null) {
                newEntity.setGithubId(existingEntity.getGithubId());
            }

            repositories.add(newEntity);
        }

        return repositoryJpaRepository.saveAll(repositories);
    }

    // Retrieve stored repositories with filters and sorting
    @Override
    @Transactional(readOnly = true)
    public List<RepositoryEntity> getStoredRepositories(
            String language,
            Integer minStars,
            String sort) {

        Specification<RepositoryEntity> specification =
                RepositorySpecifications.hasLanguage(language)
                        .and(RepositorySpecifications.hasMinimumStars(minStars));

        String sortField = (sort == null || sort.isBlank())
                ? "stars"
                : sort.trim().toLowerCase();

        Sort sorting = switch (sortField) {

            case "forks" ->
                    Sort.by(Sort.Direction.DESC, "forks");

            case "updated" ->
                    Sort.by(Sort.Direction.DESC, "lastUpdated");

            default ->
                    Sort.by(Sort.Direction.DESC, "stars");
        };

        return repositoryJpaRepository.findAll(
                specification,
                sorting
        );
    }
}
