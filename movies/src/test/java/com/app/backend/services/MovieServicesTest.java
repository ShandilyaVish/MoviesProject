package com.app.backend.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.app.backend.Utils.Commands;
import com.app.backend.models.Adder;
import com.app.backend.models.Movies;
import com.app.backend.models.Review;
import com.app.backend.models.User;

public class MovieServicesTest {
	private static MovieServices movieServices;
	static Map<String, Movies> movies;
	static Map<String, User> users;
	static Set<Review> reviews;
	
	@BeforeAll
	public static void init() {
		movieServices = new MovieServicesImpl();
		users = new HashMap<>();
		movies = new HashMap<>();
		reviews = new HashSet<>();
		
		populate();
	}

	@Test
	public void testForMissingField() {
		//missing the viewer_role
		String input = "top_movie_genre 1 Action";
		String result = movieServices.
				getTopMoviesByGenre(input.split(" "), users, movies, reviews);
		Assertions.assertEquals("FAILED",result);	
	}
	
	@Test
	public void testForTopMovieInEmptyYear() {
		String input = "top_movie_year 2 2019 viewer";
		String result = movieServices.
				getTopMoviesByYear(input.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.NONE_FOUND, result);
	}
	
	@Test
	public void testForMovieAverageWhichIsNotRated() {
		String input = "add_movie FIGHT_CLUB 1999 Action&Drama";
		Adder.addMovie(input.split(" "), users, movies, reviews);
		String inputForAverage = "avg_review_for_movie FIGHT_CLUB";
		String result = movieServices.
				getMovieAverage(inputForAverage.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.NOT_RATED, result);
	}
	
	@Test
	public void testForTopMoviesInYearForMultipleGenreMovies() {
		String input = "top_movie_genre 1 Drama viewer";
		String result = movieServices.
				getTopMoviesByGenre(input.split(" "), users, movies, reviews);
		Assertions.assertEquals("3Idiots ", result);
	}
	
	private static void populate() {
		String strUserOne = "add_user SRK";
		String strUserTwo = "add_user SAL";
		String strUserThree = "add_user DEEP";
		Adder.addUser(strUserOne.split(" "), users, movies, reviews);
		Adder.addUser(strUserTwo.split(" "), users, movies, reviews);
		Adder.addUser(strUserThree.split(" "), users, movies, reviews);
		
		String movieOne = "add_movie KGF 2018 Action";
		String movieTwo = "add_movie 3Idiots 2010 Comedy&Drama";
		String movieThree = "add_movie Titanic 2010 Drama&Romance";
		String movieFour = "add_movie AbeLincoln 2010 Drama&Action";
		Adder.addMovie(movieOne.split(" "), users, movies, reviews);
		Adder.addMovie(movieTwo.split(" "), users, movies, reviews);
		Adder.addMovie(movieThree.split(" "), users, movies, reviews);
		Adder.addMovie(movieFour.split(" "), users, movies, reviews);
		
		String reviewOne = "add_review SRK KGF 9";
		String reviewTwo = "add_review SRK 3Idiots 9";
		String reviewThree = "add_review SRK Titanic 10";
		String reviewFour = "add_review SRK AbeLincoln 10";
		String reviewFive = "add_review SAL 3Idiots 10";
		String reviewSix = "add_review DEEP AbeLincoln 7";
		Adder.addReview(reviewOne.split(" "), users, movies, reviews);
		Adder.addReview(reviewTwo.split(" "), users, movies, reviews);
		Adder.addReview(reviewThree.split(" "), users, movies, reviews);
		Adder.addReview(reviewFour.split(" "), users, movies, reviews);
		Adder.addReview(reviewFive.split(" "), users, movies, reviews);
		Adder.addReview(reviewSix.split(" "), users, movies, reviews);
		
	}
	
}
