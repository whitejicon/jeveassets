/*
 * Copyright 2009-2017 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package net.nikr.eve.jeveasset.gui.tabs.transaction;

import com.beimin.eveapi.model.shared.WalletTransaction;
import java.util.Date;
import net.nikr.eve.jeveasset.data.Item;
import net.nikr.eve.jeveasset.data.MyLocation;
import net.nikr.eve.jeveasset.data.api.OwnerType;
import net.nikr.eve.jeveasset.data.types.EditableLocationType;
import net.nikr.eve.jeveasset.data.types.ItemType;
import net.nikr.eve.jeveasset.i18n.TabsTransaction;

public class MyTransaction extends WalletTransaction implements EditableLocationType, ItemType {

	private final Item item;
	private final OwnerType owner;
	private final int accountKey;
	private MyLocation location;

	public MyTransaction(final WalletTransaction apiTransaction, final Item item, final MyLocation location, final OwnerType owner, final int accountKey) {		
		this.setTransactionDateTime(apiTransaction.getTransactionDateTime());
		this.setTransactionID(apiTransaction.getTransactionID());
		this.setQuantity(apiTransaction.getQuantity());
		this.setTypeName(apiTransaction.getTypeName());
		this.setTypeID(apiTransaction.getTypeID());
		this.setPrice(apiTransaction.getPrice());
		this.setClientID(apiTransaction.getClientID());
		this.setClientName(apiTransaction.getClientName());
		this.setCharacterID(apiTransaction.getCharacterID());
		this.setCharacterName(apiTransaction.getCharacterName());
		this.setStationID(apiTransaction.getStationID());
		this.setStationName(apiTransaction.getStationName());
		this.setTransactionType(apiTransaction.getTransactionType());
		this.setTransactionFor(apiTransaction.getTransactionFor());
		this.setJournalTransactionID(apiTransaction.getJournalTransactionID());
		this.setClientTypeID(apiTransaction.getClientTypeID());

		this.item = item;
		this.location = location;
		this.owner = owner;
		this.accountKey = accountKey;
	}

	public int getAccountKey() {
		return accountKey;
	}

	public int getAccountKeyFormated() {
		return accountKey - 999;
	}

	public String getTransactionTypeFormatted() {
		if (isSell()) {
			return TabsTransaction.get().sell();
		}
		if (isBuy()) {
			return TabsTransaction.get().buy();
		}
		return getTransactionType();
	}

	
	public String getTransactionForFormatted() {
		if (isForPersonal()) {
			return TabsTransaction.get().personal();
		}
		if (isForCorporation()) {
			return TabsTransaction.get().corporation();
		}
		return getTransactionFor();
	}

	@Override
	public MyLocation getLocation() {
		return location;
	}

	@Override
	public void setLocation(MyLocation location) {
		this.location = location;
	}

	@Override
	public Item getItem() {
		return item;
	}

	public String getOwnerName() {
		return owner.getOwnerName();
	}

	public double getValue() {
		if (isSell()) {
			return getQuantity() * getPrice();
		} else {
			return getQuantity() * -getPrice();
		}
	}

	public boolean isAfterAssets() {
		Date date = owner.getAssetLastUpdate();
		if (date != null) {
			return getTransactionDateTime().after(date);
		} else {
			return false;
		}
	}

	public boolean isSell() {
		return getTransactionType().equals("sell");
	}

	public boolean isBuy() {
		return getTransactionType().equals("buy");
	}

	public boolean isForPersonal() {
		return getTransactionFor().equals("personal");
	}

	public boolean isForCorporation() {
		return getTransactionFor().equals("corporation");
	}

	@Override
	public int compareTo(final WalletTransaction o) {
		int compared = o.getTransactionDateTime().compareTo(this.getTransactionDateTime());
		if (compared != 0) {
			return compared;
		} else {
			return Double.compare(o.getPrice(), this.getPrice());
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + this.accountKey;
		hash = 11 * hash + (int) (this.getTransactionID() ^ (this.getTransactionID() >>> 32));
		hash = 11 * hash + (int) (this.owner.getOwnerID() ^ (this.owner.getOwnerID() >>> 32));
		hash = 11 * hash + (int) (Double.doubleToLongBits(this.getPrice()) ^ (Double.doubleToLongBits(this.getPrice()) >>> 32));
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MyTransaction other = (MyTransaction) obj;
		if (this.accountKey != other.accountKey) {
			return false;
		}
		if (this.getTransactionID() != other.getTransactionID()) {
			return false;
		}
		if (this.owner.getOwnerID() != other.owner.getOwnerID()) {
			return false;
		}
		if (Double.doubleToLongBits(this.getPrice()) != Double.doubleToLongBits(other.getPrice())) {
			return false;
		}
		return true;
	}
 }