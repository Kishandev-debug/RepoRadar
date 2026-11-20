
package com.kishan.reporadar.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubApiResponse {

    @JsonProperty("total_count")
    private Integer totalCount;

    private List<Item> items;

    public GitHubApiResponse() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        private Long id;
        private String name;
        private String description;
        private Owner owner;
        private String language;

        @JsonProperty("stargazers_count")
        private Integer stars;

        @JsonProperty("forks_count")
        private Integer forks;

        @JsonProperty("updated_at")
        private Instant lastUpdated;

        @JsonProperty("html_url")
        private String htmlUrl;

        public Item() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Integer getStars() {
            return stars;
        }

        public void setStars(Integer stars) {
            this.stars = stars;
        }

        public Integer getForks() {
            return forks;
        }

        public void setForks(Integer forks) {
            this.forks = forks;
        }

        public Instant getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Instant lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {

        private String login;

        public Owner() {
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }
}
