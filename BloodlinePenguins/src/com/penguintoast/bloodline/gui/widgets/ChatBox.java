package com.penguintoast.bloodline.gui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.GameClient;
import com.penguintoast.bloodline.net.GameServer;
import com.penguintoast.bloodline.net.Network;

public class ChatBox extends Table {
	private TextField input;
	private ScrollPane scroll;
	private Label textArea;
	private GameClient client;
	private GameServer server;

	public ChatBox() {
		super(Global.skin);
		setBackground("btnWhite");
		
		textArea = new Label("", Global.skin, "textArea");
		textArea.setWrap(true);
		
		scroll = new ScrollPane(textArea, Global.skin, "textArea");
		scroll.setFlickScroll(false);
		scroll.setSmoothScrolling(false);
		add(scroll).expand().fillX().top();
		row();
		
		input = new TextField("", Global.skin);
		input.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char key) {
				if(key == '\r' || key == '\n') {
					send();
				}
			}
		});
		add(input).expandX().bottom().fillX();
	}
	
	public void setServer(GameServer server) {
		this.server = server;
	}
	
	public void setClient(GameClient client) {
		this.client = client;
	}
	
	public void send() {
		if(!input.getText().equals("")) {
			if(Network.host) {
				server.chat(PlayerData.getInstance().name + ": " + input.getText());
			} else {
				client.chat(PlayerData.getInstance().name + ": " + input.getText());
			}
			input.setText("");
		}
	}
	
	public void append(String text) {
		textArea.setText(textArea.getText() + text + "\r\n");
		scroll.setScrollPercentY(100.0f);
	}

}
