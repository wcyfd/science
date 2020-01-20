package com.science.game;

import java.io.BufferedReader;
import java.io.FileReader;
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

	private static final ListReader i = new ListReader();

	public static void load(String file, IConfigCache cache) {

		log.info("Load config {}", file);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();// 列名跳过
			while ((line = br.readLine()) != null) {
				List<String> list = i.getParams();
				i.cleanParams();
				StringTokenizer token = new StringTokenizer(line, ",");
				while (token.hasMoreTokens()) {
					list.add(token.nextToken());
				}
				cache.load(i);
				i.cleanParams();
			}
		} catch (Exception e) {
			log.error("配置表加载报错" + file, e);
			System.exit(0);
		} finally {
			i.cleanParams();
		}
	}
}
