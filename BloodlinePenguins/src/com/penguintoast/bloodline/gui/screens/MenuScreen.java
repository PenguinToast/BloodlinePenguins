package com.penguintoast.bloodline.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.gui.skin.ButtonPadding;

public class MenuScreen extends BaseScreen {

	public MenuScreen() {
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");

		ButtonPadding pad = Global.skin.get(ButtonPadding.class);

		TextButton createBtn = new TextButton("Create Game", Global.skin);
		createBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Global.game.transition(new HostScreen());
			}
		});

		TextButton joinBtn = new TextButton("Join Game", Global.skin);
		joinBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Global.game.transition(new JoinScreen());
			}
		});

		TextButton quitBtn = new TextButton("Quit", Global.skin);
		quitBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		addWidget(table, createBtn, pad);
		addWidget(table, joinBtn, pad);
		addWidget(table, quitBtn, pad);
	}

	private void addWidget(Table table, Table widget, ButtonPadding pad) {
		table.add(widget.pad(pad.top, pad.left, pad.bottom, pad.right)).space(pad.space).uniform().fill();
		table.row();
	}
}