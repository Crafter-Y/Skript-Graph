/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.inventory.ItemStack;

@Name("Is Flammable")
@Description("Checks whether an item is flammable")
@Examples({"if wood is flammable: # true", "if player's tool is flammable:"})
@Since("INSERT VERSION")
public class CondIsFlammable extends PropertyCondition<ItemStack> {

	static {
		PropertyCondition.register(CondIsFlammable.class, "flammable", "itemstacks");
	}

	@Override
	public boolean check(ItemStack i) {
		return i.getType().isFlammable();
	}

	@Override
	protected String getPropertyName() {
		return "flammable";
	}

}
