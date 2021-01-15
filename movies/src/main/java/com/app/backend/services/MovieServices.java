package com.app.backend.services;

import java.util.Map;
import java.util.Set;

import com.app.backend.models.Movies;
import com.app.backend.models.Review;
import com.app.backend.models.User;

public interface MovieServices {
	public String getTopMoviesByGenre(String [] input,
			Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews);
	public String getTopMoviesByYear(String [] input,
			Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews);
	public String getGenreReviewByAverage(String [] input,
			Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews);
	public String getYearReviewByAverage(String [] input,
			Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews);
	public String getMovieAverage(String [] input,
			Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews);
}
