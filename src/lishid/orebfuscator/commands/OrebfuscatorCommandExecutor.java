package lishid.orebfuscator.commands;

import java.io.File;

import lishid.orebfuscator.Orebfuscator;
import lishid.orebfuscator.cache.ObfuscatedHashCache;
import lishid.orebfuscator.cache.ObfuscatedRegionFileCache;
import lishid.orebfuscator.threading.OrebfuscatorThreadCalculation;
import lishid.orebfuscator.utils.OrebfuscatorConfig;
import lishid.orebfuscator.utils.PermissionRelay;

import net.minecraft.server.ChunkCoordIntPair;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class OrebfuscatorCommandExecutor {
    @SuppressWarnings("unchecked")
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (command.getName().equalsIgnoreCase("chunk"))
    	{
    	    int r = 2;
    	    if (args.length > 0)
    	    {
    	        try
    	        {
    	            r = new Integer(args[0]);
    	        }
    	        catch (NumberFormatException e)
    	        {
    	            Orebfuscator.message(sender, args[0] + " is not a number!");
    	            return true;
    	        }
    	        if (r < 1) r = 1;
    	        if (r > 10) r = 10;
    	    }

    	    Player player = (Player) sender;
    	    Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
    	    int x = chunk.getX();
    	    int z = chunk.getZ();
    	    for (int i = x - r; i <= x + r; i++)
    	    {
    	        for (int j = z - r; j <= z + r; j++)
    	        {
    	            ChunkCoordIntPair location = new ChunkCoordIntPair(i, j);
    	            ((CraftPlayer) player).getHandle().chunkCoordIntPairQueue.add(location);
    	        }
    	    }
    	    Orebfuscator.message(sender, "Chunks within a radius of " + r + " have been sent to you.");
    	    return true;
    	}
    	
    	if ((sender instanceof Player) && !PermissionRelay.hasPermission((Player) sender, "Orebfuscator.admin")) {
            Orebfuscator.message(sender, "You do not have permissions.");
            return true;
        }
    	
    	if(args.length <= 0)
    	{
    		return false;
    	}
    	
    	if(args[0].equalsIgnoreCase("engine") && args.length > 1)
    	{
    		int engine = OrebfuscatorConfig.getEngineMode();
			try 
			{
				engine = new Integer(args[1]);
			}
			catch (NumberFormatException e) 
			{
	    		Orebfuscator.message(sender, args[1] + " is not a number!");
	    		return true;
			}
			if(engine != 1 && engine != 2)
			{
	    		Orebfuscator.message(sender, args[1] + " is not a valid EngineMode!");
	    		return true;
			}
			else
			{
	    		OrebfuscatorConfig.setEngineMode(engine);
	    		Orebfuscator.message(sender, "Engine set to: " + engine);
	    		return true;
			}
    	}
    	
    	if(args[0].equalsIgnoreCase("updateradius") && args.length > 1)
    	{
    		int radius = OrebfuscatorConfig.getUpdateRadius();
			try 
			{
				radius = new Integer(args[1]);
			}
			catch (NumberFormatException e) 
			{
	    		Orebfuscator.message(sender, args[1] + " is not a number!");
	    		return true;
			}
    		OrebfuscatorConfig.setUpdateRadius(radius);
    		Orebfuscator.message(sender, "UpdateRadius set to: " + OrebfuscatorConfig.getUpdateRadius());
    		return true;
    	}
    	
    	if(args[0].equalsIgnoreCase("initialradius") && args.length > 1)
    	{
    		int radius = OrebfuscatorConfig.getInitialRadius();
			try 
			{
				radius = new Integer(args[1]);
			}
			catch (NumberFormatException e) 
			{
	    		Orebfuscator.message(sender, args[1] + " is not a number!");
	    		return true;
			}
    		OrebfuscatorConfig.setInitialRadius(radius);
    		Orebfuscator.message(sender, "InitialRadius set to: " + radius);
    		return true;
    	}
    	
    	if(args[0].equalsIgnoreCase("threads") && args.length > 1)
    	{
    		int threads = OrebfuscatorConfig.getProcessingThreads();
			try 
			{
				threads = new Integer(args[1]);
			}
			catch (NumberFormatException e) 
			{
	    		Orebfuscator.message(sender, args[1] + " is not a number!");
	    		return true;
			}
    		OrebfuscatorConfig.setProcessingThreads(threads);
			if(OrebfuscatorThreadCalculation.CheckThreads())
			{
				OrebfuscatorThreadCalculation.SyncThreads();
			}
    		Orebfuscator.message(sender, "Processing Threads set to: " + threads);
    		return true;
    	}
    	
    	if(args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable"))
    	{
    		boolean data = args[0].equalsIgnoreCase("enable");
    		
	    	if(args[0].equalsIgnoreCase("enable") && args.length == 1)
	    	{
	    		OrebfuscatorConfig.setEnabled(true);
	    		Orebfuscator.message(sender, "Enabled.");
	    	}
	    	
	    	if(args[0].equalsIgnoreCase("disable") && args.length == 1)
	    	{
	    		OrebfuscatorConfig.setEnabled(false);
	    		Orebfuscator.message(sender, "Disabled.");
	    	}

			if(args.length > 1)
			{
				if(args[1].equalsIgnoreCase("updatebreak"))
				{
					OrebfuscatorConfig.setUpdateOnBreak(data);
		    		Orebfuscator.message(sender, "OnBlockBreak update "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("updatedamage"))
				{
					OrebfuscatorConfig.setUpdateOnDamage(data);
		    		Orebfuscator.message(sender, "OnBlockDamage update "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("updatephysics"))
				{
					OrebfuscatorConfig.setUpdateOnPhysics(data);
		    		Orebfuscator.message(sender, "OnBlockPhysics update "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("updateexplosion"))
				{
					OrebfuscatorConfig.setUpdateOnExplosion(data);
		    		Orebfuscator.message(sender, "OnCreeperExplosion update "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("updatethread"))
				{
					OrebfuscatorConfig.setUpdateThread(data);
		    		Orebfuscator.message(sender, "Extra update thread "+(data?"enabled":"disabled. Updates will be processed in the main thread instead")+".");
				}
				else if(args[1].equalsIgnoreCase("darknesshide"))
				{
					OrebfuscatorConfig.setDarknessHideBlocks(data);
		    		Orebfuscator.message(sender, "Darkness obfuscation "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("op"))
				{
					OrebfuscatorConfig.setNoObfuscationForOps(data);
		    		Orebfuscator.message(sender, "Ops No-Obfuscation "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("perms") || args[1].equalsIgnoreCase("permissions"))
				{
					OrebfuscatorConfig.setNoObfuscationForPermission(data);
		    		Orebfuscator.message(sender, "Permissions No-Obfuscation "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("cache"))
				{
					OrebfuscatorConfig.setUseCache(data);
		    		Orebfuscator.message(sender, "Cache "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("verbose"))
				{
					OrebfuscatorConfig.setVerboseMode(data);
					Orebfuscator.Verbose();
		    		Orebfuscator.message(sender, "Verbose mode "+(data?"enabled":"disabled")+".");
				}
				else if(args[1].equalsIgnoreCase("world") && args.length > 2)
				{
					OrebfuscatorConfig.setDisabledWorlds(args[2], !data);
		    		Orebfuscator.message(sender, "World \""+args[2]+"\" obfuscation "+(data?"enabled":"disabled")+".");
				}
			}
    	}
    	
    	if(args[0].equalsIgnoreCase("reload"))
    	{
    		OrebfuscatorConfig.reload();
    		Orebfuscator.message(sender, "Reload complete.");
    	}
    	
    	if(args[0].equalsIgnoreCase("status"))
    	{
    		Orebfuscator.message(sender, "Orebfuscator " + Orebfuscator.instance.getDescription().getVersion() + " is: " + (OrebfuscatorConfig.getEnabled()?"Enabled":"Disabled"));
    		Orebfuscator.message(sender, "EngineMode: " + OrebfuscatorConfig.getEngineMode());
    		
    		if(Orebfuscator.usingSpout)
        		Orebfuscator.message(sender, "Executing Threads: Using Spout");
    		else
    			Orebfuscator.message(sender, "Executing Threads: " + OrebfuscatorThreadCalculation.getThreads());
    		Orebfuscator.message(sender, "Processing Threads Max: " + OrebfuscatorConfig.getProcessingThreads());
    		Orebfuscator.message(sender, "Extra Update Thread: " + (OrebfuscatorConfig.getUpdateThread()?"Enabled":"Disabled"));

    		Orebfuscator.message(sender, "Caching: " + (OrebfuscatorConfig.getUseCache()?"Enabled":"Disabled"));

    		Orebfuscator.message(sender, "Initial Obfuscation Radius: " + OrebfuscatorConfig.getInitialRadius());
    		Orebfuscator.message(sender, "Update Radius: " + OrebfuscatorConfig.getUpdateRadius());
    		
    		String disabledWorlds = OrebfuscatorConfig.getDisabledWorlds();
    		Orebfuscator.message(sender, "Disabled worlds: " + (disabledWorlds.equals("")?"None":disabledWorlds));
    	}
    	
    	if(args[0].equalsIgnoreCase("clearcache"))
    	{
    		ObfuscatedRegionFileCache.clearCache();
    		ObfuscatedHashCache.clearCache();
    		File dir = new File(Bukkit.getServer().getWorldContainer(), "orebfuscator_cache");
    		try {
    			DeleteDir(dir);
			}
    		catch (Exception e) { Orebfuscator.log(e); }
    		Orebfuscator.message(sender, "Cache cleared.");
    	}
    	
    	return true;
    }
    
    private static void DeleteDir(File dir)
    {
    	try{
	    	if (!dir.exists())
	    		return;
	    	
	        if (dir.isDirectory())
	            for (File f : dir.listFiles())
	                DeleteDir(f);
	        
	        dir.delete();
        }
    	catch (Exception e) { Orebfuscator.log(e); }
    }
}