package pl.banknotmc.efekty;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
import java.util.UUID;

public class EfektyGracza extends JavaPlugin implements Listener {

    private final Map<UUID, Particle> playerParticles = new HashMap<>();
    private final Map<UUID, Double> angleOffset = new HashMap<>();
    private final double radius = 3;
    private final double height = 2;
    private final int points = 40;
    private final int refresh = 2; // ticks
    private final double rotationSpeed = 5;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        startParticleTask();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("efektygracza") && sender instanceof Player player) {
            openGUI(player);
            return true;
        }
        return false;
    }

    private void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Wybierz efekt particle");