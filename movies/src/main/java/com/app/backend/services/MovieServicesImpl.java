package com.app.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.backend.Utils.Commands;
import com.app.backend.models.Movies;
import com.app.backend.models.Review;
import com.app.backend.models.User;

public class MovieServicesImpl implements MovieServices {
	
	private static Comparator<Movies> comparatorForViewer = (first,second) -> {
		return first.getViewerScore() > second.getViewerScore() ? -1:1;
	};
	private static Comparator<Movies> comparatorForCritics = (first,second) -> {
		return first.getCriticScore() > second.getCriticScore() ? -1:1;
	};

	@Override
	public String getTopMoviesByGenre(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result = "";
		try {
			int topOrderMovies = Integer.parseInt(input[1]);
			String genre = input[2];
			String role = input[3];
			List<Movies> moviesByTopReviews = new ArrayList<Movies>();
			for (Review review : reviews) {
				Movies movieReviewed = movies.get(review.getMovie());
				if(!moviesByTopReviews.contains(movieReviewed)) {
					String userRoleWhenReviewed = review.getUserRoleWhenReviewed();
					String movieGenre[] = movieReviewed.getGenre().split("&");
					if(role.equalsIgnoreCase(userRoleWhenReviewed)) {
						for(String g : movieGenre) {
							if(g.equalsIgnoreCase(genre)) {
								moviesByTopReviews.add(movieReviewed);
							}
						}
					}
				}
			}
			if(moviesByTopReviews.size() == 0) {
				System.out.println("Sorry there are no reviews by the " + role);
				result = Commands.NONE_FOUND;
			}
			else {
				if(role.equalsIgnoreCase(Commands.VIEWER)) {
					Collections.sort(moviesByTopReviews, comparatorForViewer);
				} else {
					Collections.sort(moviesByTopReviews,comparatorForCritics);
				}
				
				
				for(int i = 0;i < topOrderMovies;i++) {
					Movies topMovie = moviesByTopReviews.get(i);
					result += topMovie.getMovieName() + " ";
					System.out.println(topMovie.getMovieName() +
							" Viewer Ratings:" + topMovie.getViewerScore() + " Critic Ratings:" +
							topMovie.getCriticScore() + " in year " + topMovie.getYearReleased());
				}
			}

		} catch (Exception e) {
			result += Commands.FAILED;
			System.out.println("Proper command needed : top_movie_genre N GENRE USER_ROLE");
		}
		return result;
	}

	@Override
	public String getTopMoviesByYear(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result = "";
		try {
			int topOrderMovies = Integer.parseInt(input[1]);
			int releaseYear = Integer.parseInt(input[2]);
			String role = input[3];
			List<Movies> moviesByTopReviews = new ArrayList<Movies>();
			for (Review review : reviews) {
				Movies movieReviewed = movies.get(review.getMovie());
				if(!moviesByTopReviews.contains(movieReviewed)) {
					String userRoleWhenReviewed = review.getUserRoleWhenReviewed();
					if(movieReviewed.getYearReleased() == releaseYear &&
							role.equalsIgnoreCase(userRoleWhenReviewed)) {
						moviesByTopReviews.add(movieReviewed);
					}
				}	
			}
			if(moviesByTopReviews.size() == 0) {
				System.out.println("Sorry there are no reviews by the" + role);
				result += Commands.NONE_FOUND;
			} else {
				
				
				if(role.equalsIgnoreCase(Commands.VIEWER)) {
					Collections.sort(moviesByTopReviews, comparatorForViewer);
				} else {
					Collections.sort(moviesByTopReviews,comparatorForCritics);
				}
				
				for(int i = 0;i < topOrderMovies;i++) {
					Movies topMovie = moviesByTopReviews.get(i);
					result += topMovie.getMovieName() + " ";
					System.out.println(topMovie.getMovieName() +
							" Viewer Ratings:" + topMovie.getViewerScore() + " Critic Ratings:" +
							topMovie.getCriticScore());
				}
			}
		} catch (Exception e) {
			result += Commands.FAILED;
			System.out.println("Proper command needed :top_movie_year N RELEASE_YEAR USER_ROLE");
		}
		return result;
	}


	@Override
	public String getMovieAverage(String[] input, Map<String, User> users,
			Map<String, Movies> movies, Set<Review> reviews) {
		String result;
		try {
			String movieName = input[1];
			boolean isMoviePresent = movies.containsKey(movieName);
			if(isMoviePresent) {
				Movies avgToBeCalculatedMovie = movies.get(movieName);
				int totalReviewForTheMovie = avgToBeCalculatedMovie.getTotalRatings();
				if(totalReviewForTheMovie == 0) {
					System.out.println("The movies has not been rated yet");
					result = Commands.NOT_RATED;
				}
				else {
					int criticScore = avgToBeCalculatedMovie.getCriticScore();
					int viewerScore = avgToBeCalculatedMovie.getViewerScore();
					double score = criticScore + viewerScore;
					double totalRating = score/totalReviewForTheMovie;
					System.out.println("Movie " + movieName + " rated " + totalRating);
					result = Commands.RATED;
				}
 			} else {
 				result = Commands.NOT_RATED;
				System.out.println("Movie " + movieName + " not present");
			}
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println("avg_review_for_movie MOVIE_NAME");
		}
		return result;
	}

	//average review for genre is average of movie in that genre and again averaged
	@Override
	public String getGenreReviewByAverage(String[] input, Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews) {
		String result;
		try {
			String genre = input[1];
			Set<Movies> moviesAlreadyCheckedForReview = new HashSet<>();
			double genreRating = 0.0;
			int overallMoviesForGenre = 0;
			for(Review review : reviews) {
				Movies movieReviewed = movies.get(review.getMovie());
				if(!moviesAlreadyCheckedForReview.contains(movieReviewed)) {
					String genreOfMovieReviewed[] = movieReviewed.getGenre().split("&");
					for(String g : genreOfMovieReviewed) {
						if(g.equalsIgnoreCase(genre)) {
							int totalReviewForTheMovie = movieReviewed.getTotalRatings();
							int criticScore = movieReviewed.getCriticScore();
							int viewerScore = movieReviewed.getViewerScore();
							double score = criticScore + viewerScore;
							double totalMovieRating = score/totalReviewForTheMovie;
							genreRating += totalMovieRating;
							++overallMoviesForGenre;
							moviesAlreadyCheckedForReview.add(movieReviewed);
						}
					}
				}
			}
			genreRating /= overallMoviesForGenre;
			if(genreRating == 0.0) {
				result = Commands.NOT_RATED;
			} else {
				result = Commands.RATED;
			}
			System.out.println("Average " + genre + " " + genreRating);
			
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println("Proper command needed:avg_review_for_genre GENRE");
		}
		return result;
	}

	@Override
	public String getYearReviewByAverage(String[] input, Map<String, User> users, Map<String, Movies> movies,
			Set<Review> reviews) {
		String result;
		try {
			int yearOfRelease = Integer.parseInt(input[1]);
			Set<Movies> moviesAlreadyCheckedForReview = new HashSet<>();
			double yearRating = 0.0;
			for(Review review : reviews) {
				Movies movieReviewed = movies.get(review.getMovie());
				int yearOfMovieReviewed = movieReviewed.getYearReleased();
				if(!moviesAlreadyCheckedForReview.contains(movieReviewed)) {
					if(yearOfMovieReviewed == yearOfRelease) {
						int totalReviewForTheMovie = movieReviewed.getTotalRatings();
						int criticScore = movieReviewed.getCriticScore();
						int viewerScore = movieReviewed.getViewerScore();
						double score = criticScore + viewerScore;
						double totalMovieRating = score/totalReviewForTheMovie;
						yearRating += totalMovieRating;
						moviesAlreadyCheckedForReview.add(movieReviewed);
					}
				}
			}
			if(yearRating == 0.0) {
				result = Commands.NOT_RATED;
			} else {
				result = Commands.RATED;
			}
			System.out.println("Average " + yearOfRelease + " is" + yearRating);
			
		} catch (Exception e) {
			result = Commands.FAILED;
			System.out.println("Proper command needed:avg_review_for_year YEAR");
		}
		return result;
	}


}
