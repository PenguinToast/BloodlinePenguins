package com.penguintoast.bloodline.gui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;

public class PlayerListItem extends Button {
	private Image ready;

	public PlayerListItem(PlayerData data) {
		super(Global.skin, "playerBtn");
		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

			}
		});
		left();
		ready = new Image(Global.assets.get("game.atlas", TextureAtlas.class).findRegion("ready"));
		add(ready).pad(4);
		add(data.name).expand();

		if (data.ready) {
			ready.setColor(Color.GREEN);
		} else {
			ready.setColor(Color.RED);
		}
	}

	public void ready() {
		ready.setColor(Color.GREEN);
	}

}
