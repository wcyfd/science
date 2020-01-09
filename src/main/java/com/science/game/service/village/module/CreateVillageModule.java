package com.science.game.service.village.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Village;
import com.science.game.service.job.JobInternal;

@Component
public class CreateVillageModule {

	@Autowired
	private JobInternal jobInternal;

	public void createVillage() {
		Village v = new Village();
		Data.villages.put(v.getId(), v);
		
		jobInternal.stopAndReturnRemainTime(v.getId());
	}
}
