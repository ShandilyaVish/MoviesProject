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
public class User {
	String name;
	String roles;
	int totalReviews;
	Set<String> reviewedMovies;
}
