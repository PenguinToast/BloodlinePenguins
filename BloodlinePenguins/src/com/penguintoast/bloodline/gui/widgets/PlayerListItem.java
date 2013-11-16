package com.penguintoast.bloodline.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;

public class PlayerListItem extends Button {

	public PlayerListItem(PlayerData data) {
		super(Global.skin, "playerBtn");
		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
			}
		});
		add(data.name);
	}

}
