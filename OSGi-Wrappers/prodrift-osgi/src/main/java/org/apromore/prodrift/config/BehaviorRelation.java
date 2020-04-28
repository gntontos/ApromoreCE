/*-
 * #%L
 * This file is part of "Apromore Community".
 *
 * Copyright (C) 2017 Alireza Ostovar.
 * %%
 * Copyright (C) 2018 - 2020 The University of Melbourne.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.prodrift.config;

public enum BehaviorRelation {
	// PRIME EVENT STRUCTURES
	D_FOLLOW, // direct follow
	D_INV_FOLLOW, // direct inverse follow
	FOLLOW,
	INV_FOLLOW,
	Causal,
	INV_Causal,
	CONFLICT,
	Independence,
	CONCURRENCY,
	Length_Two_Loop,
	Length_Two_Loop_bi, // There are ABA and BAB
	Length_Two_Loop_ABA, // There are ABA
	Length_Two_Loop_BAB, // There are BAB
	Length_One_Loop,
	// ASYMMETRIC EVENT STRUCTURES
	ASYM_CONFLICT,
	INV_ASYM_CONFLICT
}
