package com.penguintoast.bloodline.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;

public class PlayerList extends Table {
	private boolean host;
	private ButtonGroup buttons;
	private ObjectMap<PlayerData, PlayerListItem> items;
	
	public PlayerList(boolean host) {
		super(Global.skin);
		this.host = host;
		setBackground("btnWhite");
		pad(4).top();
		items = new ObjectMap<PlayerData, PlayerListItem>();
		buttons = new ButtonGroup();
	}
	
	public void addPlayer(PlayerData data) {
		PlayerListItem item = new PlayerListItem(data);
		items.put(data, item);
		buttons.add(item);
		add(item).expandX().fill().space(2);
	}
	
	public void removePlayer(PlayerData data) {
		PlayerListItem item = items.remove(data);
		removeActor(item);
		buttons.remove(item);
	}
}
