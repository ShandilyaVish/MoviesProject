package com.app.backend.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.app.backend.Utils.Commands;

public class AdderTest {
	private static Map<String, Movies> movies;
	private static Map<String, User> users;
	private static Set<Review> reviews;
	
	@BeforeAll
	private static void init() {
		users = new HashMap<>();
		movies = new HashMap<>();
		reviews = new HashSet<>();
		String strUserOne = "add_user SRK";
		String strUserTwo = "add_user SAL";
		Adder.addUser(strUserOne.split(" "), users, movies, reviews);
		Adder.addUser(strUserTwo.split(" "), users, movies, reviews);
		
		String movieOne = "add_movie KGF 2018 Action";
		String movieTwo = "add_movie 3Idiots 2010 Comedy&Drama";
		Adder.addMovie(movieOne.split(" "), users, movies, reviews);
		Adder.addMovie(movieTwo.split(" "), users, movies, reviews);
		
		String reviewOne = "add_review SRK KGF 9";
		Adder.addReview(reviewOne.split(" "), users, movies, reviews);
	}
	
	@Test
	public void testForMultipeAdditionOfUser() {
		String userInput = "add_user SRK";
		String result = Adder.addUser(userInput.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.PRESENT, result);
	}
	
	@Test
	public void testForMultipleReviewBySameUser() {
		String userReview = "add_review SRK KGF 8";
		String result = Adder.addReview(userReview.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.PRESENT, result);
	}
	
	@Test
	public void testForRatingsByUser() {
		String userReview = "add_review SRK 3Idiots 11";
		String result = Adder.addReview(userReview.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.EXCEEDED, result);
	}
	
	@Test
	public void testForInvalidMovieCommand() {
		String movieInput = "add_movie JBond 1965";
		String result  = Adder.addMovie(movieInput.split(" "), users, movies, reviews);
		Assertions.assertEquals(Commands.FAILED, result);
	}
}
