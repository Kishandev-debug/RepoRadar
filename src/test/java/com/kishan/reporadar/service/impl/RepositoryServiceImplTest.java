
package com.kishan.reporadar.service.impl;

import com.kishan.reporadar.client.GitHubApiClient;
import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.dto.response.GitHubApiResponse;
import com.kishan.reporadar.entity.RepositoryEntity;
import com.kishan.reporadar.mapper.RepositoryMapper;
import com.kishan.reporadar.repository.RepositoryJpaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceImplTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private RepositoryMapper repositoryMapper;

    @Mock
    private RepositoryJpaRepository repositoryJpaRepository;

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @Test
    void searchAndSaveRepositories_shouldSaveRepositories() {

        SearchRequest request = new SearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubApiResponse.Item item = new GitHubApiResponse.Item();
        item.setId(123L);
        item.setName("spring-boot");

        GitHubApiResponse response = new GitHubApiResponse();
        response.setItems(List.of(item));

        RepositoryEntity entity = new RepositoryEntity();
        entity.setGithubId(123L);
        entity.setName("spring-boot");

        when(gitHubApiClient.searchRepositories(request))
                .thenReturn(response);

        when(repositoryMapper.toEntity(item))
                .thenReturn(entity);

        when(repositoryJpaRepository.findById(123L))
                .thenReturn(Optional.empty());

        when(repositoryJpaRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<RepositoryEntity> result =
                repositoryService.searchAndSaveRepositories(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("spring-boot", result.get(0).getName());

        verify(gitHubApiClient).searchRepositories(request);
        verify(repositoryMapper).toEntity(item);
        verify(repositoryJpaRepository).saveAll(anyList());
    }

    @Test
    void searchAndSaveRepositories_shouldHandleExistingRepository() {

        SearchRequest request = new SearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubApiResponse.Item item = new GitHubApiResponse.Item();
        item.setId(123L);
        item.setName("spring-boot-updated");

        GitHubApiResponse response = new GitHubApiResponse();
        response.setItems(List.of(item));

        RepositoryEntity updatedEntity = new RepositoryEntity();
        updatedEntity.setGithubId(123L);
        updatedEntity.setName("spring-boot-updated");

        RepositoryEntity existingEntity = new RepositoryEntity();
        existingEntity.setGithubId(123L);
        existingEntity.setName("spring-boot-old");

        when(gitHubApiClient.searchRepositories(request))
                .thenReturn(response);

        when(repositoryMapper.toEntity(item))
                .thenReturn(updatedEntity);

        when(repositoryJpaRepository.findById(123L))
                .thenReturn(Optional.of(existingEntity));

        when(repositoryJpaRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<RepositoryEntity> result =
                repositoryService.searchAndSaveRepositories(request);

        assertEquals(1, result.size());
        assertEquals(123L, result.get(0).getGithubId());
        assertEquals("spring-boot-updated", result.get(0).getName());

        verify(repositoryJpaRepository).findById(123L);
        verify(repositoryJpaRepository).saveAll(anyList());
    }

    @Test
    void searchAndSaveRepositories_shouldReturnEmptyListWhenNoResults() {

        SearchRequest request = new SearchRequest();
        request.setQuery("non-existing-repository");
        request.setLanguage("Java");
        request.setSort("stars");

        GitHubApiResponse response = new GitHubApiResponse();
        response.setItems(List.of());

        when(gitHubApiClient.searchRepositories(request))
                .thenReturn(response);

        List<RepositoryEntity> result =
                repositoryService.searchAndSaveRepositories(request);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(gitHubApiClient).searchRepositories(request);
        verify(repositoryJpaRepository, never()).saveAll(anyList());
    }

    @Test
    void getStoredRepositories_shouldApplyLanguageAndMinimumStarsFilters() {

        RepositoryEntity entity = new RepositoryEntity();
        entity.setGithubId(123L);
        entity.setName("spring-boot");
        entity.setLanguage("Java");
        entity.setStars(50000);

        when(repositoryJpaRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(entity));

        List<RepositoryEntity> result =
                repositoryService.getStoredRepositories("Java", 10000, "stars");

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getLanguage());
        assertTrue(result.get(0).getStars() >= 10000);

        verify(repositoryJpaRepository).findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class));
    }

    @Test
    void getStoredRepositories_shouldReturnRepositoriesSortedByForks() {

        RepositoryEntity entity = new RepositoryEntity();
        entity.setGithubId(123L);
        entity.setName("spring-boot");
        entity.setForks(5000);

        when(repositoryJpaRepository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(entity));

        List<RepositoryEntity> result =
                repositoryService.getStoredRepositories(null, null, "forks");

        assertEquals(1, result.size());
        assertEquals(5000, result.get(0).getForks());

        verify(repositoryJpaRepository).findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Sort.class));
    }


}
