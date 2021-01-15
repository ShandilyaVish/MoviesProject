package com.app.backend.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.app.backend.Utils.Commands;
import com.app.backend.services.MovieServices;
import com.app.backend.services.MovieServicesImpl;

public class InputReader {
	private Map<String, User> users;
	private Set<Review> reviews; 
	private Map<String,Movies> movies;  
	private MovieServices movieServices;
	
	private void init() {
		users = new HashMap<>();
		reviews = new HashSet<>();
		movies = new HashMap<>();
	}
	
	
	public void processInput() {
		String line;
		
		init();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			while((line = br.readLine()) != null) {
				instructions(line);
			}
			
		} catch (IOException e) {
			System.out.println("Required proper input");
		}
	}


	private void instructions(String line) {
		String input[] = line.split(" ");
		String command = input[0];
		movieServices = new MovieServicesImpl();
		switch (command) {
		case Commands.ADD_MOVIE:
			Adder.addMovie(input,users,movies,reviews);
			break;
		case Commands.ADD_USER:
			Adder.addUser(input,users,movies,reviews);
			break;
		case Commands.ADD_REVIEW:
			Adder.addReview(input,users,movies,reviews);
			break;
		case Commands.TOP_MOVIE_GENRE:
			movieServices.getTopMoviesByGenre(input,users,movies,reviews);
			break;
		case Commands.TOP_MOVIE_YEAR:
			movieServices.getTopMoviesByYear(input,users,movies,reviews);
			break;
		case Commands.AVG_REVIEW_YEAR:
			movieServices.getYearReviewByAverage(input,users,movies,reviews);
			break;
		case Commands.AVG_REVIEW_MOVIE:
			movieServices.getMovieAverage(input,users,movies,reviews);
			break;
		case Commands.AVG_REVIEW_GENRE:
			movieServices.getGenreReviewByAverage(input,users,movies,reviews);
			break;
		case Commands.END_PROCESS:
			break;
		default:
			System.out.println("Please provide a valid command");
		}
	}
}

//add_user SRK
//add_user YBK
//add_movie avatar 2010 action
//add_movie avendator 2011 action
//add_movie titanic 2000 romance
//add_review SRK avatar 7
//add_user JJSK
