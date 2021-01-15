package com.app.backend.models;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movies {
	String movieName;
	String genre;
	int yearReleased;
	int totalRatings;
	int criticScore;
	int viewerScore;
}
