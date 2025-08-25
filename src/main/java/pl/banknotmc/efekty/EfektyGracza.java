package pl.banknotmc.efekty;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class EfektyGracza extends JavaPlugin implements Listener {

    private final Map<Player, Particle> playerEffects = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("EfektyGracza włączony! Coded by _d4presja_");
    }

    @Override
    public void onDisable() {
        getLogger().info("EfektyGracza wyłączony!");
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("efektygracza")) {
            player.sendMessage("§a§lBanknot§fMC.§a§lpl §7– Coded by _d4presja_");
            openGUI(player);
            return true;
        }

        return false;
    }

    private void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Efekty Gracza");

        inv.setItem(0, createItem(Material.REDSTONE, "Serce", "Ta animacja to kolo z czasteczkami serca"));
        inv.setItem(1, createItem(Material.FLINT_AND_STEEL, "Ogień", "Ta animacja to kolo z czasteczkami ognia"));
        inv.setItem(2, createItem(Material.CLOUD, "Chmura", "Ta animacja to kolo z czasteczkami chmury"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(java.util.Collections.singletonList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals("Efekty Gracza")) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String name = clicked.getItemMeta().getDisplayName();
        Particle particle = Particle.HEART;
        if (name.equals("Serce")) particle = Particle.HEART;
        if (name.equals("Ogień")) particle = Particle.FLAME;
        if (name.equals("Chmura")) particle = Particle.CLOUD;

        playerEffects.put(player, particle);
        player.sendMessage("§a§lBanknot§fMC.§a§lpl §7– Wybrano efekt: " + name + " (Coded by _d4presja_)");

        startEffect(player, particle);
        player.closeInventory();
    }

    private void startEffect(Player player, Particle particle) {
        new BukkitRunnable() {
            double t = 0;
            @Override
            public void run() {
                if (!player.isOnline() || !playerEffects.containsKey(player)) {
                    cancel();
                    return;
                }

                double radius = 1.0;
                double x = radius * Math.cos(t);
                double z = radius * Math.sin(t);
                player.getWorld().spawnParticle(playerEffects.get(player),
                        player.getLocation().add(x, 1, z),
                        0);
                t += Math.PI / 8;
            }
        }.runTaskTimer(this, 0, 2);
    }
}