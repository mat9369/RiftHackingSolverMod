package com.mat9369.rifthackingsolvermod;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = RiftHackingSolverMod.MODID, version = RiftHackingSolverMod.VERSION)
public class RiftHackingSolverMod {
	public static final String MODID = "rifthackingsolvermod";
	public static final String VERSION = "1.0";
	public static org.apache.logging.log4j.Logger logger;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static GuiScreen gui = null;
	public static IInventory chest = null;
	public static boolean isHackingGui = false;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		logger = event.getModLog();
	}

	@SubscribeEvent
	public void gui(GuiOpenEvent event) {
		gui = event.gui;
	}

	@SubscribeEvent
	public void tick(PlayerTickEvent event) {
		if (event.side.isClient() && event.phase == TickEvent.Phase.START) {
			Container inventory = mc.thePlayer.openContainer;
			if (inventory instanceof ContainerChest) {
				chest = ((ContainerChest) inventory).getLowerChestInventory();
			} else {
				chest = null;
			}
			isHackingGui = chest != null && ("Hacking".equals(chest.getName()) || "Hacking (As seen on CSI)".equals(chest.getName())) && chest.getSizeInventory() == 54 && gui instanceof GuiChest;
		}
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {

		if (event.phase == Phase.END && isHackingGui) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			ScaledResolution scaledResolution = new ScaledResolution(mc);
			// GuiContainer
			int xSize = 176;
			int guiLeft = (scaledResolution.getScaledWidth() - xSize) / 2;
			// GuiChest
			int i = 222;
			int j = i - 108;
			int inventoryRows = 54 / 9;
			int ySize = j + inventoryRows * 18;
			int guiTop = (scaledResolution.getScaledHeight() - ySize) / 2;
			
			GlStateManager.translate((float) guiLeft + 7, (float) guiTop + 17, 0);
			renderHackable(chest, 2, 11, 10, 17);
			renderHackable(chest, 3, 21, 18, 25);
			renderHackable(chest, 4, 31, 28, 35);
			renderHackable(chest, 5, 41, 36, 43);
			renderHackable(chest, 6, 51, 46, 53);
			int color = new Color(0,255,0,127).hashCode();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.popMatrix();

		}
	}

	public static int getSlotStackSize(IInventory chest, int slotIndex) {
		ItemStack item = chest.getStackInSlot(slotIndex);
		if (item != null) {
			return item.stackSize;
		} else {
			return -1;
		}
	}

	public static void renderHackable(IInventory chest, int targetSlotIndex, int clickSlotIndex, int checkSlotStartIndex, int checkSlotEndIndex) {
		int stackSize = getSlotStackSize(chest, targetSlotIndex);
		
		for (int i = checkSlotStartIndex; i <= checkSlotEndIndex; i++) {
			if (getSlotStackSize(chest, i) == stackSize) {
				if(i == clickSlotIndex) {
					drawSlot(clickSlotIndex,new Color(0, 255, 0, 127).hashCode());
					return;
				} else {
					drawSlot(i,new Color(0, 0, 255, 127).hashCode());
					break;
				}
			}
		}
		drawSlot(clickSlotIndex,new Color(255, 0, 0, 127).hashCode());
	}
	

	
	public static void drawSlot(int slotIndex, int color) {
		int x = slotIndex % 9 * 18;
		int y = slotIndex / 9 * 18;
		Gui.drawRect(x+1, y+1, x+17, y+17, color);
	}
	
	public static void drawSlot(int slotIndex, int red, int blue, int green, int alpha) {
		drawSlot(slotIndex, new Color(red, blue, green, alpha).hashCode());
	}
}
