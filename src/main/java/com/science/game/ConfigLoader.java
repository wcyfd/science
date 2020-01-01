package com.science.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.science.game.cache.config.IConfigCache;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置表加载
 * 
 * @author aimfd
 *
 */
@Slf4j
public class ConfigLoader {

	public static void load(String file, IConfigCache cache)
			throws IOException, InstantiationException, IllegalAccessException {

		log.info("Load config {}", file);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();// 列名跳过
			while ((line = br.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, ",");
				List<String> list = new LinkedList<>();
				while (token.hasMoreTokens()) {
					list.add(token.nextToken());
				}
				cache.load(list);
			}
		}
		cache.afterLoad();
	}
}
