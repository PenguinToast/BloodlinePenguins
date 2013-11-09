package com.penguintoast.bloodline.gui.screens;

import java.net.InetAddress;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.net.Discoverer;
import com.penguintoast.bloodline.net.Discoverer.DiscoverListener;



public class JoinScreen extends BaseScreen {
	private List list;
	private String[] items;

	public JoinScreen() {
		Table table = new Table(Global.skin);
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground("background");
		
		items = new String[0];
		list = new List(items, Global.skin);
		ScrollPane pane = new ScrollPane(list, Global.skin);
		pane.setFlickScroll(false);
		pane.setSmoothScrolling(false);

		table.add(pane).size(300, 300);
		pane.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(getTapCount() >= 2) {
					System.out.println(list.getSelection());
				}
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		Discoverer.discoverHosts(0, 10000, null, new DiscoverListener() {
			@Override
			public void hostDiscovered(InetAddress addr) {
				String[] temp = new String[items.length + 1];
				System.arraycopy(items, 0, temp, 0, items.length);
				items = temp;
				items[items.length - 1] = addr.toString();
				list.setItems(items);
			}
		});
	}

}
