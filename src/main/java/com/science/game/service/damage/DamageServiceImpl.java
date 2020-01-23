package com.science.game.service.damage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.scene.DamageData;
import com.science.game.service.AbstractService;

import game.quick.window.Task;

@Service
public class DamageServiceImpl extends AbstractService implements DamageService, DamageInternal, Task {

	@Autowired
	private DataCenter dataCenter;

	@Override
	protected void dispatch(String cmd, ParamReader i) {
		switch (cmd) {
		case "start":
			start();
			break;
		case "stop":
			stop();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		DamageData damageData = dataCenter.getScene().getDamageData();
		if (damageData.isShutdown())
			return;

		damageData.getDamages().forEach(damage -> damage.damageLoop(null));
	}

	@Override
	public void afterExecute() {
		DamageData damageData = dataCenter.getScene().getDamageData();
		if (damageData.isShutdown())
			return;

		nextLoop();
	}

	private void nextLoop() {
		this.delay(this);
	}

	@Override
	public void regist(Object obj, @SuppressWarnings("rawtypes") IDamage damage) {
		IDamage<?> data = new DamageWrapper(obj, damage);

		dataCenter.getScene().getDamageData().getDamages().add(data);
	}

	@Override
	public void unregist(Object obj, @SuppressWarnings("rawtypes") IDamage damage) {
		dataCenter.getScene().getDamageData().getDamages().remove(damage);
	}

	@Override
	public void start() {
		dataCenter.getScene().getDamageData().setShutdown(false);
		nextLoop();
	}

	@Override
	public void stop() {
		dataCenter.getScene().getDamageData().setShutdown(true);
	}

	@SuppressWarnings("rawtypes")
	class DamageWrapper implements IDamage {
		private IDamage damage;
		private Object obj;

		public DamageWrapper(Object obj, IDamage damage) {
			this.obj = obj;
			this.damage = damage;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void damageLoop(Object data) {
			damage.damageLoop(obj);
		}
	}

}
