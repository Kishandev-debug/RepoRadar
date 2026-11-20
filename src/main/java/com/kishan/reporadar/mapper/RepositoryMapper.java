
package com.kishan.reporadar.mapper;

import com.kishan.reporadar.dto.response.GitHubApiResponse;
import com.kishan.reporadar.entity.RepositoryEntity;

import org.springframework.stereotype.Component;

@Component
public class RepositoryMapper {

    public RepositoryEntity toEntity(GitHubApiResponse.Item item) {

        RepositoryEntity entity = new RepositoryEntity();

        entity.setGithubId(item.getId());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());

        if (item.getOwner() != null) {
            entity.setOwnerName(item.getOwner().getLogin());
        }

        entity.setLanguage(item.getLanguage());

        entity.setStars(
                item.getStars() != null ? item.getStars() : 0
        );

        entity.setForks(
                item.getForks() != null ? item.getForks() : 0
        );

        entity.setLastUpdated(item.getLastUpdated());
        entity.setHtmlUrl(item.getHtmlUrl());

        return entity;
    }
}
