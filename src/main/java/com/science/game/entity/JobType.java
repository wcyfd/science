package com.science.game.entity;

public enum JobType {
	NULL(-1), ASSART(1), DIG(2), CHOP(3), DEVELOP(4), PRODUCT(5);

	JobType(int jobId) {
		this.jobId = jobId;
	}

	private int jobId;

	public int getJobId() {
		return jobId;
	}
}
