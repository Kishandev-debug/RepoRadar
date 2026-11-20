
package com.kishan.reporadar.service;

import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.entity.RepositoryEntity;

import java.util.List;

public interface RepositoryService {

    List<RepositoryEntity> searchAndSaveRepositories(SearchRequest request);

    List<RepositoryEntity> getStoredRepositories(
            String language,
            Integer minStars,
            String sort
    );
}
