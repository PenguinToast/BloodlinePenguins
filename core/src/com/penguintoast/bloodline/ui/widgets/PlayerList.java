package com.penguintoast.bloodline.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntMap;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;

public class PlayerList extends Table {
	private ButtonGroup buttons;
	private IntMap<PlayerListItem> items;
	
	public PlayerList() {
		super(Global.skin);
		setBackground("btnWhite");
		pad(4).top();
		items = new IntMap<PlayerListItem>();
		buttons = new ButtonGroup();
	}
	
	public void addPlayer(PlayerData data) {
		PlayerListItem item = new PlayerListItem(data);
		items.put(data.id, item);
		buttons.add(item);
		add(item).expandX().fill().space(2);
		row();
	}
	
	public void removePlayer(int id) {
		PlayerListItem item = items.remove(id);
	
		removeActor(item);
		getCells().removeValue(getCell(item), true);
		invalidate();
		buttons.remove(item);
	}
	
	public void ready(int id) {
		items.get(id).ready();
	}
}
