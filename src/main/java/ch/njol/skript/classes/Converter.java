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

package ch.njol.skript.classes;

import ch.njol.skript.registrations.Converters;

/**
 * used to convert data from one type to another.
 * 
 * @param <F> the accepted type of objects to convert <u>f</u>rom
 * @param <T> the type to convert <u>t</u>o
 * @author Peter Güttinger
 * @see Converters#registerConverter(Class, Class, Converter)
 */
public interface Converter<F, T> {
	
	public final static class ConverterOptions {
		public final static int NO_LEFT_CHAINING = 1;
		public final static int NO_RIGHT_CHAINING = 2;
		public final static int NO_CHAINING = 3;
	}
	
	/**
	 * holds information about a converter
	 * 
	 * @author Peter Güttinger
	 * @param <F> same as in {@link Converter}
	 * @param <T> dito
	 */
	public static final class ConverterInfo<F, T> {
		
		public final Class<F> from;
		public final Class<T> to;
		public final SerializableConverter<F, T> converter;
		public final int options;
		
		public ConverterInfo(final Class<F> from, final Class<T> to, final SerializableConverter<F, T> converter, final int options) {
			this.from = from;
			this.to = to;
			this.converter = converter;
			this.options = options;
		}
		
	}
	
	/**
	 * Converts an object from the given to the desired type.
	 * <p>
	 * Please note that the given object may be null, thus make sure that you test for null first.
	 * 
	 * @param f The object to convert which can be null.
	 * @return the converted object
	 */
	public T convert(F f);
	
	public static final class ConverterUtils {
		
		public final static <F, T> SerializableConverter<?, T> createInstanceofConverter(final ConverterInfo<F, T> conv) {
			return createInstanceofConverter(conv.from, conv.converter);
		}
		
		public final static <F, T> SerializableConverter<?, T> createInstanceofConverter(final Class<F> from, final Converter<F, T> conv) {
			return new SerializableConverter<Object, T>() {
				private static final long serialVersionUID = -9026052502252522895L;
				
				@SuppressWarnings("unchecked")
				@Override
				public T convert(final Object o) {
					if (!from.isInstance(o))
						return null;
					return conv.convert((F) o);
				}
			};
		}
		
		public final static <F, T> SerializableConverter<F, T> createInstanceofConverter(final Converter<F, ?> conv, final Class<T> to) {
			return new SerializableConverter<F, T>() {
				private static final long serialVersionUID = 2408973867196975702L;
				
				@SuppressWarnings("unchecked")
				@Override
				public T convert(final F f) {
					final Object o = conv.convert(f);
					if (to.isInstance(o))
						return (T) o;
					return null;
				}
			};
		}
		
		public final static <F, T> SerializableConverter<?, T> createDoubleInstanceofConverter(final ConverterInfo<F, ?> conv, final Class<T> to) {
			return createDoubleInstanceofConverter(conv.from, conv.converter, to);
		}
		
		public final static <F, T> SerializableConverter<?, T> createDoubleInstanceofConverter(final Class<F> from, final Converter<F, ?> conv, final Class<T> to) {
			return new SerializableConverter<Object, T>() {
				private static final long serialVersionUID = 6009912617490506586L;
				
				@SuppressWarnings("unchecked")
				@Override
				public T convert(final Object o) {
					if (!from.isInstance(o))
						return null;
					final Object o2 = conv.convert((F) o);
					if (to.isInstance(o2))
						return (T) o2;
					return null;
				}
			};
		}
		
	}
	
}