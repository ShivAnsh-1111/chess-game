package com.online_chess.chess_game.config;

import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.context.SecurityContextHolder;

import feign.Logger;
//import feign.RequestInterceptor;

public class ChessConfig {
	
	@Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // FULL logs the entire request and response, including URLs.
    }


    //while calling service is secuired with jwt
    //  @Bean
    // public RequestInterceptor jwtRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) { // Inject OAuth2AuthorizedClientManager (Spring Security 5.x and later)
    //     return template -> {
    //         OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("your-client-registration-id") // The client registration ID from your security config
    //                 .principal(SecurityContextHolder.getContext().getAuthentication()) // Get the currently authenticated user
    //                 .build();

    //         OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
    //         String accessToken = authorizedClient.getAccessToken().getTokenValue();

    //         template.header("Authorization", "Bearer " + accessToken);
    //     };
    // }

}
