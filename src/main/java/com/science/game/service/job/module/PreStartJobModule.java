package com.science.game.service.job.module;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.JobData;
import com.science.game.entity.JobType;
import com.science.game.entity.Place;
import com.science.game.entity.PlaceType;
import com.science.game.entity.Village;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PreStartJobModule {
	/**
	 * 开始工作前的前期处理
	 * 
	 * @param vid
	 * @param type
	 * @param id
	 * @param jobId
	 */
	public void preStartJob(int vid, PlaceType type, int id, JobType jobType) {
		Village village = Data.villages.get(vid);
		JobData jobData = village.getJobData();
		jobData.setPlaceId(id);
		jobData.setPlaceType(type);
		jobData.setJobType(jobType);

		Map<Integer, Place> placeMap = null;
		if (type == PlaceType.ITEM) {
			placeMap = Data.itemPlace;
		} else if (type == PlaceType.PLACE) {
			placeMap = Data.resPlace;
		}

		if (placeMap == null) {
			log.error("地点不应该是空的 村民id={} 工作类型={},工作地id={}", vid, jobType, id);
		} else {
			List<Integer> villages = placeMap.get(id).getVillages();
			if (!villages.contains((Integer) vid)) {
				villages.add(vid);
			}
		}
	}
}
