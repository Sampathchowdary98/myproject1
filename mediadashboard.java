<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.social</groupId>
        <artifactId>spring-social-twitter</artifactId>
    </dependency>
</dependencies>

above is xml file

Obtain Twitter API keys by creating a Twitter Developer account (https://developer.twitter.com/en/apps) and create an application to get the API keys.

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {
    private final String apiKey = "your-api-key";
    private final String apiSecret = "your-api-secret";
    private final String accessToken = "your-access-token";
    private final String accessTokenSecret = "your-access-token-secret";

    @Bean
    public TwitterTemplate twitterTemplate() {
        return new TwitterTemplate(apiKey, apiSecret, accessToken, accessTokenSecret);
    }
}


Create a service class to fetch tweets:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TweetService {
    @Autowired
    private Twitter twitter;

    public SearchResults searchTweets(String searchTerm) {
        return twitter.searchOperations().search(searchTerm);
    }
}


Create a controller to handle web requests:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TwitterController {
    @Autowired
    private TweetService tweetService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/tweets")
    public String getTweets(@RequestParam String searchTerm, Model model) {
        SearchResults searchResults = tweetService.searchTweets(searchTerm);
        model.addAttribute("tweets", searchResults.getTweets());
        return "tweets";
    }
}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TwitterController {
    @Autowired
    private TweetService tweetService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/tweets")
    public String getTweets(@RequestParam String searchTerm, Model model) {
        SearchResults searchResults = tweetService.searchTweets(searchTerm);
        model.addAttribute("tweets", searchResults.getTweets());
        return "tweets";
    }
}
Create Thymeleaf templates (src/main/resources/templates) for the web pages. For example, index.html:

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Twitter Analytics Dashboard</title>
</head>
<body>
    <h1>Welcome to Twitter Analytics Dashboard</h1>
    <form action="/tweets" method="get">
        <label for="searchTerm">Search Tweets:</label>
        <input type="text" id="searchTerm" name="searchTerm" required>
        <button type="submit">Search</button>
    </form>
</body>
</html>


add tweet.html

<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Twitter Analytics Dashboard - Tweets</title>
</head>
<body>
    <h1>Tweets for your search term:</h1>
    <ul>
        <li th:each="tweet : ${tweets}">
            <p th:text="${tweet.text}"></p>
        </li>
    </ul>
</body>
</html>

Run your Spring Boot application and access it at http://localhost:8080/.
