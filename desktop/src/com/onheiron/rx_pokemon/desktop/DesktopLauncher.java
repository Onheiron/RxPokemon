package com.onheiron.rx_pokemon.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import com.onheiron.rx_pokemon.game.RxPokemonGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(RxPokemonGame.GAME_IDENTIFIER);
		config.width = 672;
		config.height = 480;
		config.vSyncEnabled = true;
		new DesktopMini2DxGame(new RxPokemonGame(), config);
	}
}
