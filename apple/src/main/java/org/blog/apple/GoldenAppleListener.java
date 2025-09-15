package org.blog.apple;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class GoldenAppleListener implements Listener {

    private final int cooldownTicks;       // 20 ticks = 1 second
    private final String cooldownMessage;

    public GoldenAppleListener(int cooldownSeconds, String cooldownMessage) {
        this.cooldownTicks = Math.max(0, cooldownSeconds) * 20;
        this.cooldownMessage = cooldownMessage == null ? "" : cooldownMessage;
    }

    private static boolean isGoldenApple(ItemStack item) {
        if (item == null) return false;
        Material t = item.getType();
        return t == Material.GOLDEN_APPLE || t == Material.ENCHANTED_GOLDEN_APPLE;
    }

    /** 쿨타임 중이면 먹기 시도를 막음 */
    @EventHandler(ignoreCancelled = true)
    public void onRightClick(PlayerInteractEvent e) {
        Action a = e.getAction();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return; // 보조손 중복 방지

        if (!isGoldenApple(e.getItem())) return;

        Player p = e.getPlayer();
        if (p.hasCooldown(Material.GOLDEN_APPLE) || p.hasCooldown(Material.ENCHANTED_GOLDEN_APPLE)) {
            e.setCancelled(true);
            if (!cooldownMessage.isEmpty()) {
                p.sendMessage(Component.text(cooldownMessage).color(NamedTextColor.RED));
            }
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.8f, 1.2f);
        }
    }

    /** 실제로 먹었을 때 쿨타임 부여 */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (!isGoldenApple(e.getItem())) return;

        Player p = e.getPlayer();
        p.setCooldown(Material.GOLDEN_APPLE, cooldownTicks);
        p.setCooldown(Material.ENCHANTED_GOLDEN_APPLE, cooldownTicks);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.0f);
    }
}
