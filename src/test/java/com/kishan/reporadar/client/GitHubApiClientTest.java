
package com.kishan.reporadar.client;

import com.kishan.reporadar.dto.request.SearchRequest;
import com.kishan.reporadar.dto.response.GitHubApiResponse;
import com.kishan.reporadar.exception.GitHubApiException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.hamcrest.Matchers.containsString;

class GitHubApiClientTest {

    private GitHubApiClient githubApiClient;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.github.com");

        mockServer = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder.build();

        githubApiClient = new GitHubApiClient(restClient);
    }

    private SearchRequest createSearchRequest() {

        SearchRequest request = new SearchRequest();

        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        return request;
    }

    @Test
    void shouldReturnRepositoriesWhenGitHubApiSucceeds() {

        mockServer.expect(requestTo(
                        containsString("/search/repositories")))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "total_count": 1,
                                  "items": []
                                }
                                """));

        GitHubApiResponse response =
                githubApiClient.searchRepositories(createSearchRequest());

        assertNotNull(response);
        assertEquals(1, response.getTotalCount());

        mockServer.verify();
    }

    @Test
    void shouldThrowExceptionWhenGitHubApiFails() {

        mockServer.expect(requestTo(
                        containsString("/search/repositories")))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "message": "Internal Server Error"
                                }
                                """));

        GitHubApiException exception = assertThrows(
                GitHubApiException.class,
                () -> githubApiClient.searchRepositories(createSearchRequest())
        );

        assertEquals(502, exception.getStatusCode());

        mockServer.verify();
    }

    @Test
    void shouldReturn503WhenGitHubRateLimitExceeded() {

        mockServer.expect(requestTo(
                        containsString("/search/repositories")))
                .andRespond(withStatus(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "message": "API rate limit exceeded"
                                }
                                """)
                        .header("X-RateLimit-Remaining", "0"));

        GitHubApiException exception = assertThrows(
                GitHubApiException.class,
                () -> githubApiClient.searchRepositories(createSearchRequest())
        );

        assertEquals(503, exception.getStatusCode());

        mockServer.verify();
    }
}
