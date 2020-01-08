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

	public static JobType getByJobId(int jobId) {
		if (jobId == -1) {
			return NULL;
		} else if (jobId == 1) {
			return ASSART;
		} else if (jobId == 2) {
			return DIG;
		} else if (jobId == 3) {
			return CHOP;
		} else if (jobId == 4) {
			return DEVELOP;
		} else if (jobId == 5) {
			return PRODUCT;
		} else {
			return null;
		}
	}
}
