package com.rightit.taxibook.util;

import java.util.concurrent.CompletableFuture;

public class FailedCompletableFutureBuilder<T> {
	
	public CompletableFuture<T> build(Exception ex) {
		final CompletableFuture<T> failedFuture = new CompletableFuture<T>();
		failedFuture.completeExceptionally(ex);
		return failedFuture;
	}
}
