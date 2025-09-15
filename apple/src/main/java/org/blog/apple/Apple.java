package org.blog.apple;

import org.bukkit.plugin.java.JavaPlugin;

public final class Apple extends JavaPlugin {

    @Override
    public void onEnable() {
        // 기본 설정 생성
        getConfig().addDefault("cooldown-seconds", 5);
        getConfig().addDefault("message-on-cooldown", "아직 먹을 수 없습니다.");
        getConfig().options().copyDefaults(true);
        saveConfig();

        int cooldownSeconds = getConfig().getInt("cooldown-seconds", 5);
        String msg = getConfig().getString("message-on-cooldown", "아직 먹을 수 없습니다.");

        // 리스너 등록
        getServer().getPluginManager().registerEvents(
                new GoldenAppleListener(cooldownSeconds, msg), this);

        getLogger().info("GoldenAppleCooldown enabled (" + cooldownSeconds + "s)");
    }

    @Override
    public void onDisable() { }
}
