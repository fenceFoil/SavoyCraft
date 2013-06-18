package com.savoycraft;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;

import com.fencefoil.signWatcher.Finder;
import com.fencefoil.signWatcher.SignChangedEvent;
import com.fencefoil.signWatcher.SignWatcher;
import com.fencefoil.signWatcher.interfaces.SignChangedListener;
import com.savoycraft.bot.BotManager;
import com.savoycraft.conductor.Conductor;
import com.savoycraft.keyboard.KeypressManager;
import com.savoycraft.opera.Operas;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "savoycraft", name = "SavoyCraft", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class SavoyCraft implements IScheduledTickHandler {

	// The instance of your mod that Forge uses.
	@Instance("savoycraft")
	public static SavoyCraft instance;

	public static KeypressManager keys = new KeypressManager();

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.savoycraft.client.ClientProxy", serverSide = "com.savoycraft.CommonProxy")
	public static CommonProxy proxy;

	private static BotManager botManager = new BotManager();

	private static Conductor conductor = new Conductor();

	private static Random rand = new Random();

	private static LinkedList<String> chatQueue = new LinkedList<String>();

	public static BotManager getBotManager() {
		return botManager;
	}

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		proxy.registerTickHandlers();

		// Set up key bindings
		KeyBindingRegistry.registerKeyBinding(new SavoyCraftKeyHandler());

		// Set up SignWatcher
		SignWatcher.init(true);
		SignWatcher.addSignChangedListener(new SignChangedListener() {

			@Override
			public void signChanged(SignChangedEvent event) {
				// TODO Auto-generated method stub

			}
		});

		// Load operas
		Operas.loadOperas();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}

	public static void chat(String message) {
		try {
			Minecraft.getMinecraft().thePlayer.addChatMessage(message);
		} catch (NullPointerException e) {
			chatQueue.add(message);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// Load settings (does nothing if already loaded)
		// Must happen when world is there to show stuff on the chat
		TDConfig.loadAndUpdateSettings();

		// Update SignWatcher
		try {
			SignWatcher.scanWorldForSignsManually();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Update sign editor watcher
		try {
			updateSignEditorWatcher();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			keys.update();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			botManager.update();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		updateChatQueue();
	}

	private void updateSignEditorWatcher() {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiEditSign) {
			TileEntitySign signEntity = null;
			try {
				signEntity = (TileEntitySign) Finder
						.getUniqueTypedFieldFromClass(
								GuiEditSign.class,
								TileEntitySign.class,
								(GuiEditSign) (Minecraft.getMinecraft().currentScreen));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (isStringTheaterTag(signEntity.signText[0])) {
				createTheater(
						(GuiEditSign) (Minecraft.getMinecraft().currentScreen),
						signEntity);
			}

		}
	}

	private void createTheater(GuiEditSign editor, TileEntitySign sign) {
		String theatreCode = generateTheatreCode();

		sign.signText[0] = "[Theatre]";
		sign.signText[3] = theatreCode;

		Minecraft.getMinecraft().displayGuiScreen(null);

		chat("New theatre created!");
	}

	/**
	 * Generates a new theatre code. While mostly unimportant in everything
	 * except randomness, it could be used to get the time a theater was
	 * created.
	 * 
	 * @return
	 */
	private String generateTheatreCode() {
		String datePart = Long.toHexString(System.currentTimeMillis()) + "x";
		int remainingChars = 15 - datePart.length();
		while (datePart.length() < 15) {
			// Random non-hex letters
			datePart += (char) (110 + rand.nextInt(12));
		}
		return datePart;
	}

	/**
	 * Null-safe.
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isStringTheaterTag(String line) {
		if (line == null) {
			return false;
		}

		if (line.equalsIgnoreCase("[Theater]")
				|| line.equalsIgnoreCase("[Theatre]")) {
			return true;
		} else {
			return false;
		}
	}

	private void updateChatQueue() {
		LinkedList<String> queue = (LinkedList<String>) chatQueue.clone();
		for (String s : queue) {
			chat(s);
		}
	}

	@Override
	public String getLabel() {
		return "SavoyCraft";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}

	public static Conductor getConductor() {
		return conductor;
	}
}