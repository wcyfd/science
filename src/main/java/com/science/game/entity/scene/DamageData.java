package com.science.game.entity.scene;

import java.util.HashSet;
import java.util.Set;

import com.science.game.service.damage.IDamage;

import lombok.Data;

@Data
public class DamageData {
	private boolean shutdown;
	@SuppressWarnings("rawtypes")
	private Set<IDamage> damages = new HashSet<>();
}
