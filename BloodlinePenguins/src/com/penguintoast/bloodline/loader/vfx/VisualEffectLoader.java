package com.penguintoast.bloodline.loader.vfx;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.penguintoast.bloodline.Global;
import com.penguintoast.bloodline.actors.vfx.AnimationVisualEffect;
import com.penguintoast.bloodline.actors.vfx.CombinedVisualEffect;
import com.penguintoast.bloodline.actors.vfx.ParticleVisualEffect;
import com.penguintoast.bloodline.actors.vfx.StaticVisualEffect;
import com.penguintoast.bloodline.actors.vfx.TweenVisualEffect;
import com.penguintoast.bloodline.actors.vfx.VisualEffect;
import com.penguintoast.bloodline.actors.vfx.VisualEffectAccessor;
import com.penguintoast.bloodline.loader.DataFile;

public class VisualEffectLoader extends SynchronousAssetLoader<VisualEffect, VisualEffectLoader.VisualEffectParameter> {
	
	public VisualEffectLoader() {
		this(new InternalFileHandleResolver());
	}

	public VisualEffectLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public VisualEffect load(AssetManager assetManager, String fileName, FileHandle file, VisualEffectParameter parameter) {
		VisualEffect out = null;
		
		DataFile data = new DataFile(file);
		String type = data.get("type");
		if (type.equalsIgnoreCase("Animation")) {
			out = loadAnimation(data);
		} else if (type.equalsIgnoreCase("Tween")) {
			out = loadTween(data, assetManager);
		} else if (type.equalsIgnoreCase("Static")) {
			out = loadStatic(data);
		} else if (type.equalsIgnoreCase("Combined")) {
			out = loadCombined(data, assetManager);
		} else if (type.equalsIgnoreCase("Particle")) {
			out = loadParticle(data, assetManager);
		} else {
			Gdx.app.error("Error", "Unknown visual effect type in " + fileName + ": " + type);
		}
		
		loadVisual(out, data);
		
		return out;
	}
	
	private VisualEffect loadParticle(DataFile data, AssetManager manager) {
		String path = data.get("path");
		return new ParticleVisualEffect(manager.get(path, ParticleEffect.class));
	}
	
	private VisualEffect loadTween(DataFile data, AssetManager manager) {
		// Get the target visual first
		String vString = data.get("visual");
		VisualEffect visual = manager.get(vString.trim());
		TweenVisualEffect out = new TweenVisualEffect(visual);
		
		// Build the tween
		String tString = data.get("tween");
		Timeline timeline = Timeline.createSequence();
		String[] tweens = tString.split(";"); // Get individual commands
		Tween current = null; // Current tween
		for(String tween : tweens) {
			String[] parts = tween.split(":"); // Seperate method and arguments
			String methodName = parts[0].trim();
			String[] args = parts[1].split(",");
			float[] args2;
			
			if(methodName.equalsIgnoreCase("to")) {
				if(current != null) {
					timeline.push(current);
					current = null;
				}
				int tweenType = VisualEffectAccessor.getTweenType(args[0].trim());
				float duration = Float.parseFloat(args[1].trim());
				args2 = new float[args.length - 2];
				for(int i = 2; i < args.length; i++) {
					args2[i - 2] = Float.parseFloat(args[i].trim());
				}
				current = Tween.to(out, tweenType, duration).targetRelative(args2);
				
			} else if(methodName.equalsIgnoreCase("from")) {
				if(current != null) {
					timeline.push(current);
					current = null;
				}
				int tweenType = VisualEffectAccessor.getTweenType(args[0].trim());
				float duration = Float.parseFloat(args[1].trim());
				args2 = new float[args.length - 2];
				for(int i = 2; i < args.length; i++) {
					args2[i - 2] = Float.parseFloat(args[i].trim());
				}
				current = Tween.from(out, tweenType, duration).targetRelative(args2);
				
			} else if(methodName.equalsIgnoreCase("set")) {
				if(current != null) {
					timeline.push(current);
					current = null;
				}
				int tweenType = VisualEffectAccessor.getTweenType(args[0].trim());
				args2 = new float[args.length - 1];
				for(int i = 1; i < args.length; i++) {
					args2[i - 1] = Float.parseFloat(args[i].trim());
				}
				current = Tween.set(out, tweenType).target(args2);
				
			} else if(methodName.equalsIgnoreCase("delay")) {
				if(current != null) {
					current.delay(Float.parseFloat(args[0].trim()));
				} else {
					timeline.pushPause(Float.parseFloat(args[0].trim()));
				}
			} else if(methodName.equalsIgnoreCase("ease")) {
				current.ease(TweenUtils.parseEasing(args[0].trim()));
			} else if(methodName.equalsIgnoreCase("repeat")) {
				int count = Integer.parseInt(args[0].trim());
				float delay = Float.parseFloat(args[1].trim());
				if(current != null) {
					current.repeat(count, delay);
				} else {
					timeline.repeat(count, delay);
				}
			} else if(methodName.equalsIgnoreCase("repeatYoyo")) {
				int count = Integer.parseInt(args[0].trim());
				float delay = Float.parseFloat(args[1].trim());
				if(current != null) {
					current.repeatYoyo(count, delay);
				} else {
					timeline.repeatYoyo(count, delay);
				}
			}else if(methodName.equalsIgnoreCase("end")) {
				timeline.push(current);
				current = null;
			}
		}
		
		// Flush tween
		if(current != null) {
			timeline.push(current);
		}
		
		out.setTween(timeline);
		
		return out;
	}
	
	private VisualEffect loadCombined(DataFile data, AssetManager manager) {
		Array<VisualEffect> visuals = new Array<VisualEffect>();
		String vString = data.get("visuals");
		String[] parts = vString.split(",");
		for(String part : parts) {
			VisualEffect visual = manager.get(part.trim());
			visuals.add(visual);
		}
		
		return new CombinedVisualEffect(visuals);
	}
	
	private VisualEffect loadAnimation(DataFile data) {
		String path = data.get("path");
		Array<AtlasRegion> textures = Global.atlas.findRegions(path);
		
		Animation anim = null;
		String type = data.get("playType");
		if(type == null || type.equalsIgnoreCase("NORMAL")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.NORMAL);
		} else if(type.equalsIgnoreCase("LOOP")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.LOOP);
		} else if(type.equalsIgnoreCase("LOOP_PINGPONG")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.LOOP_PINGPONG);
		} else if(type.equalsIgnoreCase("REVERSED")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.REVERSED);
		} else if(type.equalsIgnoreCase("LOOP_REVERSED")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.LOOP_REVERSED);
		} else if(type.equalsIgnoreCase("LOOP_RANDOM")) {
			anim = new Animation(AnimationVisualEffect.FRAME_DURATION, textures, Animation.LOOP_RANDOM);
		}
		return new AnimationVisualEffect(anim, textures);
	}
	
	private VisualEffect loadStatic(DataFile data) {
		String path = data.get("path");
		return new StaticVisualEffect(Global.atlas.findRegion(path));
	}
	
	private void loadVisual(VisualEffect visual, DataFile data) {
		String key = data.get("origin");
		String[] values;
		if(key != null) {
			values = key.split(",");
			visual.getOrigin().set(Float.parseFloat(values[0].trim()), Float.parseFloat(values[1].trim()));
		}
		
		key = data.get("offset");
		if(key != null) {
			values = key.split(",");
			visual.getOffset().set(Float.parseFloat(values[0].trim()), Float.parseFloat(values[1].trim()));
		}
		
		key = data.get("scale");
		if(key != null) {
			values = key.split(",");
			visual.getScale().set(Float.parseFloat(values[0].trim()), Float.parseFloat(values[1].trim()));
		}
		
		key = data.get("rotation");
		if(key != null) {
			visual.setRotation(Float.parseFloat(key.trim()));
		}
		
		key = data.get("color");
		if(key != null) {
			values = key.split(",");
			if(values.length <= 1) { // Hex format
				visual.setColor(Color.valueOf(values[0].trim()));
			} else { // Float format
				visual.setColor(new Color(Float.parseFloat(values[0].trim()), Float.parseFloat(values[1].trim()), Float.parseFloat(values[2].trim()), Float.parseFloat(values[3].trim())));
			}
		}
		
		key = data.get("alpha");
		if(key != null) {
			visual.setAlpha(Float.parseFloat(key.trim()));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, VisualEffectParameter parameter) {
		Array<AssetDescriptor> out = new Array<AssetDescriptor>();
		
		DataFile data = new DataFile(file);
		String type = data.get("type");
		if(type.equalsIgnoreCase("Animation") || type.equalsIgnoreCase("Static")) {
			out.add(new AssetDescriptor<TextureAtlas>("game.atlas", TextureAtlas.class));
		} else if(type.equalsIgnoreCase("Combined")) {
			String vString = data.get("visuals");
			String[] parts = vString.split(",");
			for(String part : parts) {
				out.add(new AssetDescriptor<VisualEffect>(part.trim(), VisualEffect.class));
			}
		} else if(type.equalsIgnoreCase("Tween")) {
			out.add(new AssetDescriptor<VisualEffect>(data.get("visual").trim(), VisualEffect.class));
		} else if(type.equalsIgnoreCase("Particle")) {
			ParticleEffectParameter param = new ParticleEffectParameter();
			param.atlasFile = "game.atlas";
			out.add(new AssetDescriptor<ParticleEffect>(data.get("path").trim(), ParticleEffect.class, param));
		} else {
			Gdx.app.error("Error", "Unknown visual effect type in " + fileName + ": " + type);
		}
		
		return out;
	}

	public static class VisualEffectParameter extends AssetLoaderParameters<VisualEffect> {

	}
}
