package com.maxwell.kmeth.utilites;

public class Timer {
	private long previousMS;

	public Timer() {
		this.previousMS = 0L;
	}

	public boolean hasReached(double ms) {
		return this.getTime() - this.previousMS >= ms;
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}
	
	public void reset() {
		this.previousMS = this.getTime();
	}
}
