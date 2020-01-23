package com.science.game.service.damage;

public interface DamageInternal {
	/**
	 * 注册
	 * 
	 * @param damage
	 */
	void regist(Object target, @SuppressWarnings("rawtypes") IDamage damage);

	/**
	 * 注销
	 */
	void unregist(Object target, @SuppressWarnings("rawtypes") IDamage damage);
}
