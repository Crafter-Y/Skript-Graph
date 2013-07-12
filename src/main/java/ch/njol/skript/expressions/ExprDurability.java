/*
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
 * Copyright 2011-2013 Peter Güttinger
 * 
 */

package ch.njol.skript.expressions;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Slot;
import ch.njol.util.CollectionUtils;
import ch.njol.util.Math2;

/**
 * @author Peter Güttinger
 */
@SuppressWarnings("serial")
@Name("Data Value")
@Description({"The data value of an item.",
		"You usually don't need this expression as you can check and set items with aliases easily, " +
				"but this expression can e.g. be used to \"add 1 to data of &lt;item&gt;\", e.g. for cycling through all wool colours."})
@Examples({"add 1 to the data value of the clicked block"})
@Since("1.2")
public class ExprDurability extends SimplePropertyExpression<Object, Short> {
	
	static {
		register(ExprDurability.class, Short.class, "((data|damage)[s] [value[s]]|durabilit(y|ies))", "itemstacks/slots");
	}
	
	@Override
	public Short convert(final Object o) {
		if (o instanceof Slot) {
			final ItemStack i = ((Slot) o).getItem();
			return i == null ? null : i.getDurability();
		} else {
			return ((ItemStack) o).getDurability();
		}
	}
	
	@Override
	public String getPropertyName() {
		return "data";
	}
	
	@Override
	public Class<Short> getReturnType() {
		return Short.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<?>[] acceptChange(final ChangeMode mode) {
		if (mode == ChangeMode.REMOVE_ALL)
			return null;
		if (Slot.class.isAssignableFrom(getExpr().getReturnType()) || getExpr().isSingle() && CollectionUtils.contains(getExpr().acceptChange(ChangeMode.SET), ItemStack.class))
			return CollectionUtils.array(Number.class);
		return null;
	}
	
	@Override
	public void change(final Event e, final Object delta, final ChangeMode mode) {
		int a = 0;
		if (mode != ChangeMode.DELETE)
			a = ((Number) delta).intValue();
		final Object[] os = getExpr().getArray(e);
		for (final Object o : os) {
			final ItemStack i = o instanceof Slot ? ((Slot) o).getItem() : (ItemStack) o;
			if (i == null)
				continue;
			switch (mode) {
				case REMOVE:
					a = -a;
					//$FALL-THROUGH$
				case ADD:
					i.setDurability((short) Math2.fit(0, i.getDurability() + a, i.getType().getMaxDurability()));
					break;
				case SET:
					i.setDurability((short) Math2.fit(0, a, i.getType().getMaxDurability()));
					break;
				case DELETE:
				case RESET:
					a = 0;
					i.setDurability((short) 0);
					break;
				case REMOVE_ALL:
					assert false;
			}
			if (o instanceof Slot)
				((Slot) o).setItem(i);
			else
				getExpr().change(e, i, ChangeMode.SET);
		}
	}
	
}