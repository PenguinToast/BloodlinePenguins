package com.penguintoast.bloodline.net;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import com.penguintoast.bloodline.actors.GameActor;
import com.penguintoast.bloodline.data.PlayerData;
import com.penguintoast.bloodline.net.objects.ChatMessage;
import com.penguintoast.bloodline.net.objects.InfoRequest;
import com.penguintoast.bloodline.net.objects.InfoResponse;
import com.penguintoast.bloodline.net.objects.JoinResponse;
import com.penguintoast.bloodline.net.objects.game.GameActorCreate;
import com.penguintoast.bloodline.net.objects.game.GameActorDestroy;
import com.penguintoast.bloodline.net.objects.game.GameActorUpdate;
import com.penguintoast.bloodline.net.objects.game.ProcessTCP;
import com.penguintoast.bloodline.net.objects.game.ProcessUDP;
import com.penguintoast.bloodline.net.objects.lobby.PlayerJoined;
import com.penguintoast.bloodline.net.objects.lobby.PlayerLeft;
import com.penguintoast.bloodline.net.objects.lobby.PlayerReady;

public class Network {
	public static final int TCP_PORT = 44443;
	public static final int UDP_PORT = 44444;
	
	public static boolean host = false;
	public static GameClient client;
	public static GameServer server;
	
	public static IntMap<PlayerData> players = new IntMap<PlayerData>();
	public static LongMap<GameActor> actors = new LongMap<GameActor>();
	public static long actorCount;
	
	public static LinkedList<Object> processTCP = new LinkedList<Object>();
	public static LinkedList<Object> processUDP = new LinkedList<Object>();
	
	public static void createActor(GameActorCreate data) {
		data.id = actorCount++;
		processTCP.add(data);
	}
	
	public static void destroyActor(long id) {
		GameActorDestroy data = new GameActorDestroy();
		data.id = id;
		processTCP.add(data);
	}
	
	public static void updateActorTCP(GameActorUpdate data) {
		processTCP.add(data);
	}
	
	public static void updateActorUDP(GameActorUpdate data) {
		processUDP.add(data);
	}

	public static void register(EndPoint ep) {
		Kryo k = ep.getKryo();
		
		registerLibGDX(k);
		
		k.register(byte[].class);
		k.register(LinkedList.class);
		
		k.register(InfoRequest.class);
		k.register(InfoResponse.class);
		k.register(PlayerJoined.class);
		k.register(PlayerLeft.class);
		k.register(PlayerReady.class);
		k.register(JoinResponse.class);
		k.register(ChatMessage.class);
		k.register(ProcessTCP.class);
		k.register(ProcessUDP.class);
		
		k.register(PlayerData.class);
		
		GameActor.register(k);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void registerLibGDX(Kryo k) {
		// Libgdx classes
		k.register(Array.class, new Serializer<Array>() {
			{
				setAcceptsNull(true);
			}

			private Class genericType;

			public void setGenerics (Kryo kryo, Class[] generics) {
				if (kryo.isFinal(generics[0])) genericType = generics[0];
			}

			public void write (Kryo kryo, Output output, Array array) {
				int length = array.size;
				output.writeInt(length, true);
				if (length == 0) return;
				if (genericType != null) {
					Serializer serializer = kryo.getSerializer(genericType);
					genericType = null;
					for (Object element : array)
						kryo.writeObjectOrNull(output, element, serializer);
				} else {
					for (Object element : array)
						kryo.writeClassAndObject(output, element);
				}
			}

			public Array read (Kryo kryo, Input input, Class<Array> type) {
				Array array = new Array();
				kryo.reference(array);
				int length = input.readInt(true);
				array.ensureCapacity(length);
				if (genericType != null) {
					Class elementClass = genericType;
					Serializer serializer = kryo.getSerializer(genericType);
					genericType = null;
					for (int i = 0; i < length; i++)
						array.add(kryo.readObjectOrNull(input, elementClass, serializer));
				} else {
					for (int i = 0; i < length; i++)
						array.add(kryo.readClassAndObject(input));
				}
				return array;
			}
		});

		k.register(IntArray.class, new Serializer<IntArray>() {
			{
				setAcceptsNull(true);
			}

			public void write (Kryo kryo, Output output, IntArray array) {
				int length = array.size;
				output.writeInt(length, true);
				if (length == 0) return;
				for (int i = 0, n = array.size; i < n; i++)
					output.writeInt(array.get(i), true);
			}

			public IntArray read (Kryo kryo, Input input, Class<IntArray> type) {
				IntArray array = new IntArray();
				kryo.reference(array);
				int length = input.readInt(true);
				array.ensureCapacity(length);
				for (int i = 0; i < length; i++)
					array.add(input.readInt(true));
				return array;
			}
		});

		k.register(FloatArray.class, new Serializer<FloatArray>() {
			{
				setAcceptsNull(true);
			}

			public void write (Kryo kryo, Output output, FloatArray array) {
				int length = array.size;
				output.writeInt(length, true);
				if (length == 0) return;
				for (int i = 0, n = array.size; i < n; i++)
					output.writeFloat(array.get(i));
			}

			public FloatArray read (Kryo kryo, Input input, Class<FloatArray> type) {
				FloatArray array = new FloatArray();
				kryo.reference(array);
				int length = input.readInt(true);
				array.ensureCapacity(length);
				for (int i = 0; i < length; i++)
					array.add(input.readFloat());
				return array;
			}
		});

		k.register(Color.class, new Serializer<Color>() {
			public Color read (Kryo kryo, Input input, Class<Color> type) {
				Color color = new Color();
				Color.rgba8888ToColor(color, input.readInt());
				return color;
			}

			public void write (Kryo kryo, Output output, Color color) {
				output.writeInt(Color.rgba8888(color));
			}
		});
	}

}
