package com.app.backend.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.app.backend.Utils.Commands;

public class Adder {
	
	private static final int initialTotalUserReviews = 0;
	private static final int initialTotalMovieReviews = 0;
	private static final int initialCriticScore = 0;
	private static final int initialViewerScore = 0;
	private static final int reviewsToGiveToBecomeCritic = 4;
	private static final int viewerWeightage = 1;
	private static final int criticWeightage = 2;
	private static final int currentYear = 2021;
	private static final int minRating = 1;
	private static final int maxRating = 10;
	
	public static String addUser(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result;
		try {
			String userName = input[1];
			if(users.containsKey(userName)) {
				result = Commands.PRESENT;
				System.out.println("User already exist");
			} else {
				User newUser = User.builder().name(userName).
						reviewedMovies(new HashSet<String>()).
						totalReviews(initialTotalUserReviews).
						roles(Commands.VIEWER).
						build();
				users.put(userName, newUser);
				result = Commands.ADDED;
			}
			
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println("Invalid input, format:add_user UserName");
		}
		return result;
	}

	public static String addMovie(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result;
		try {
			String movieName = input[1];
			int year = Integer.parseInt(input[2]);
			String genre = input[3];
			if(movies.containsKey(movieName)) {
				System.out.println
				("Movie already present,if this is second part then  use _ as seperator");
				result = Commands.PRESENT;
			} else {
				Movies newMovie = Movies.builder().movieName(movieName).
						genre(genre).yearReleased(year).criticScore(initialCriticScore).
						viewerScore(initialViewerScore).
						totalRatings(initialTotalMovieReviews).build();
				movies.put(movieName, newMovie);
				result = Commands.ADDED;
			}
			
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println
			("Invalid input, the format is :add_movie MovieName ReleaseYear Genre");
		}
		return result;
	}
	
	public static String addReview(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result;
		try {
			String userName = input[1];
			String movieName = input[2];
			int ratings = Integer.parseInt(input[3]);
			
			if(!movies.containsKey(movieName)) {
				System.out.println("Movie for review is not present");
				result = Commands.NONE_FOUND;
			} else if(!users.containsKey(userName)) {
				System.out.println("Reviewer not present,please add reviewer and then review");
				result = Commands.NONE_FOUND;
			} else if(ratings < minRating || ratings > maxRating) {
				result = Commands.EXCEEDED;
				System.out.println("Ratings should be between 1-10");
			} else {
				Movies movieForReview = movies.get(movieName);
				User userToReview = users.get(userName);
				Set<String> usersRated = userToReview.getReviewedMovies();
				boolean hasUserAlreadyRatedTheMovie = usersRated.contains(movieName);
				boolean isTheMovieYetToRelease = movieForReview.getYearReleased() > currentYear;
				
				if(hasUserAlreadyRatedTheMovie) {
					System.out.println("Exception Multiple reviews not allowed,already present");
					result = Commands.PRESENT;
				} else if(isTheMovieYetToRelease) {
					System.out.println("NOT RELEASED YET");
					result = Commands.NOT_RELEASED;
				} else {
					int userOverallReviewsGiven = userToReview.getTotalReviews();
					int movieOverallReviewsGiven = movieForReview.getTotalRatings();
					
					int totalViewerScore  = movieForReview.getViewerScore();
					int totalCriticScore = movieForReview.getCriticScore();
					
					++movieOverallReviewsGiven;
					++userOverallReviewsGiven;
					
					userToReview.setTotalReviews(userOverallReviewsGiven);
					int reviewGivenByUser;
					
					if(userOverallReviewsGiven >= reviewsToGiveToBecomeCritic) {
						boolean isCriticRoleAlreadyAdded = userToReview.
								getRoles().equalsIgnoreCase(Commands.CRITIC);
						if(!isCriticRoleAlreadyAdded) {
							userToReview.setRoles(Commands.CRITIC);
						}
						reviewGivenByUser = criticWeightage*ratings;
						totalCriticScore += reviewGivenByUser;
						movieForReview.setCriticScore(totalCriticScore);
						
					} else {
						reviewGivenByUser = viewerWeightage*ratings;
						totalViewerScore += reviewGivenByUser;
						movieForReview.setViewerScore(totalViewerScore);
					}
					
					Review newReview = Review.builder().
							movie(movieForReview.getMovieName()).user(userToReview.getName()).
							userRoleWhenReviewed(userToReview.getRoles()).
							score(ratings).build();
					reviews.add(newReview);
					
					movieForReview.setTotalRatings(movieOverallReviewsGiven);
					
					//Add the review to user as well
					usersRated.add(movieName);
					userToReview.setReviewedMovies(usersRated);
					result = Commands.ADDED;
				}
			}
			
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println("Invalid input, format is:add_review UserName MovieName Ratings");
		}
		return result;
	}
}
