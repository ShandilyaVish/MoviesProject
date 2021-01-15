package com.app.backend.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	String movie;
	String user;
	int score;
	String userRoleWhenReviewed;
}
