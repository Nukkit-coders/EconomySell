package me.onebone.economysell.provider;

/*
 * EconomySell: A plugin which allows your server to create sell centers
 * Copyright (C) 2016  onebone <jyc00410@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.onebone.economysell.EconomySell;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;

public class YamlProvider implements Provider{
	private EconomySell plugin;
	private Config config;
	
	public YamlProvider(EconomySell plugin){
		this.plugin = plugin;
		
		config = new Config(new File(plugin.getDataFolder(), "Sell.yml"), Config.YAML);
	}
	
	public void addSell(Position pos, Item item, float price, int side){
		this.addSell((int)pos.x, (int)pos.y, (int)pos.z, pos.level, item, price, side);
	}

	public void addSell(int x, int y, int z, Level level, Item item, float price, int side){
		this.addSell(x, y, z, level.getFolderName(), item, price, side);
	}

	@SuppressWarnings("serial")
	public void addSell(final int x, final int y, final int z, final String level, final Item item, final float price, final int side){
		String key = x + ":" + y + ":" + z + ":" + level.toLowerCase();
		
		this.config.set(key, new ArrayList<Object>(){{
			add(x); add(y); add(z); add(level.toLowerCase());
			add(item.getId()); add(item.getDamage()); add(item.getName());
			add(item.getCount()); add(price); add(side);
		}});
		
		if(this.plugin.getConfig().get("data.save-on-change", true)){
			this.save();
		}
	}

	public void removeSell(Position pos){
		this.removeSell((int)pos.x, (int)pos.y, (int)pos.z, pos.level);
	}

	public void removeSell(int x, int y, int z, Level level){
		this.removeSell(x, y, z, level.getFolderName());
	}

	public void removeSell(int x, int y, int z, String level){
		String key = x + ":" + y + ":" + z + ":" + level.toLowerCase();
		
		this.config.remove(key);
		
		if(this.plugin.getConfig().get("data.save-on-change", true)){
			this.save();
		}
	}

	public List<Object> getSell(Position pos){
		return this.getSell((int)pos.x, (int)pos.y, (int)pos.z, pos.level);
	}

	public List<Object> getSell(int x, int y, int z, Level level){
		return this.getSell(x, y, z, level.getFolderName());
	}

	public List<Object> getSell(int x, int y, int z, String level){
		String key = x + ":" + y + ":" + z + ":" + level.toLowerCase();
		
		return this.config.get(key, null);
	}
	
	public Map<String, Object> getAll(){
		return this.config.getAll();
	}

	public void save(){
		this.config.save();
	}

	public void close(){
		this.save();
	}
}
