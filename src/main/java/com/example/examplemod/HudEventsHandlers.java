package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Events used for the info screen. */
public class HudEventsHandlers {

  private static List<Long> clicks = new ArrayList<>();
  private static Field KEYBIND_ARRAY = null;
  private static Map<String, KeyBinding> binds;

  /**
   * This feature has a purpose to display real life time on the screen of a Minecraft player.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   */
  @SubscribeEvent
  public static void showRealTime(RenderGameOverlayEvent.Pre event) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    fontRenderer.drawString("[Time] " + dtf.format(now), 10, 20, -1);
  }

  /**
   * This feature has a purpose to display current FPS(frames per second) on the screen of a
   * Minecraft player.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   */
  @SubscribeEvent
  public static void showFPS(RenderGameOverlayEvent.Pre event) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    fontRenderer.drawString("[FPS] " + Minecraft.getDebugFPS(), 10, 10, -1);
  }

  /**
   * This feature has a purpose to display current Minecraft Biome on the screen of a Minecraft
   * player.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   */
  @SubscribeEvent
  public static void showBiome(RenderGameOverlayEvent.Pre event) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    EntityPlayer player = Minecraft.getMinecraft().player;
    BlockPos pos = new BlockPos(player.getPositionVector());
    World world = Minecraft.getMinecraft().world;
    String biome = world.getBiome(pos).getBiomeName();
    fontRenderer.drawString("[Biome] " + biome, 10, 30, -1);
  }

  /**
   * This feature has a purpose to display current XYZ on the screen of a Minecraft player.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   */
  @SubscribeEvent
  public static void showXYZ(RenderGameOverlayEvent.Pre event) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    EntityPlayer player = Minecraft.getMinecraft().player;

    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(3);

    fontRenderer.drawString(
        "[XYZ] "
            + df.format(player.getPositionVector().x)
            + " "
            + df.format(player.getPositionVector().y)
            + " "
            + df.format(player.getPositionVector().z),
        10,
        40,
        -1);
  }

  /**
   * This feature has a purpose to display RAM on the screen of a Minecraft player.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   */
  @SubscribeEvent
  public static void showRAM(RenderGameOverlayEvent.Pre event) {
    long maxMemory = Runtime.getRuntime().maxMemory();
    long totalMemory = Runtime.getRuntime().totalMemory();
    long freeMemory = Runtime.getRuntime().freeMemory();
    long usedMemory = totalMemory - freeMemory;

    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    long usedMemoryMB = usedMemory / 1024 / 1024;
    long maxMemoryMB = maxMemory / 1024 / 1024;

    fontRenderer.drawString("[RAM Usage] " + usedMemoryMB + "/" + maxMemoryMB + "MB", 10, 50, -1);
  }

  /**
   * Get key stokes array.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   * @throws Exception
   */
  @SubscribeEvent
  public static void getKeyStrokesArray(TickEvent.ClientTickEvent event) throws Exception {

    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    if (KEYBIND_ARRAY == null) {
      KEYBIND_ARRAY = KeyBinding.class.getDeclaredField("KEYBIND_ARRAY");
      KEYBIND_ARRAY.setAccessible(true);
    }
    if (event.phase.equals(TickEvent.Phase.END)) {
      binds = (Map<String, KeyBinding>) KEYBIND_ARRAY.get(null);
    }
  }

  /**
   * Show key strokes on the display.
   *
   * @param event {@link RenderGameOverlayEvent.Pre}
   * @throws Exception
   */
  @SubscribeEvent
  public static void showKeyStokes(RenderGameOverlayEvent.Pre event) throws Exception {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    if (binds != null) {
      for (String bind : binds.keySet()) {
        if (binds.get(bind).isKeyDown()) {
          if (binds.get(bind).getDisplayName().toUpperCase().equals("W")) {
            fontRenderer.drawString("W", 20, 70, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("A")) {
            fontRenderer.drawString("A", 10, 85, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("S")) {
            fontRenderer.drawString("S", 20, 85, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("D")) {
            fontRenderer.drawString("D", 30, 85, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("LEFT CLICK")) {
            fontRenderer.drawString("Left Click", 10, 100, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("RIGHT CLICK")) {
            fontRenderer.drawString("Right Click", 30, 100, -1);
          }
          if (binds.get(bind).getDisplayName().toUpperCase().equals("SPACE")) {
            fontRenderer.drawString("SPACE", 15, 115, -1);
          }
          break;
        }
      }
    }
  }

  /**
   * Showing the CPS(clicks per second) for the left click on the display.
   *
   * @param event
   */
  @SubscribeEvent
  public static void showCPS(RenderGameOverlayEvent.Pre event) {
    FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    if (binds != null) {
      for (String bind : binds.keySet()) {
        if (binds.get(bind).isKeyDown()) {
          if (binds.get(bind).getDisplayName().toUpperCase().equals("LEFT CLICK")) {

            clicks.add(System.currentTimeMillis());
          }
          break;
        }
      }
    }
    fontRenderer.drawString("[CPS] " + getCurrentCPS() / 36, 10, 130, -1);
  }

  /**
   * Calculates the current CPS.
   *
   * @return
   */
  private static int getCurrentCPS() {
    final long time = System.currentTimeMillis();
    clicks.removeIf(listItem -> listItem + 1000 < time);
    return clicks.size();
  }
}
