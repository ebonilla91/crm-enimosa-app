/**
 * Odoo, Open Source Management Solution
 * Copyright (C) 2012-today Odoo SA (<http:www.odoo.com>)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * <p/>
 * Created on 13/1/15 11:20 AM
 */
package com.odoo.addons.phonecall.models;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.odoo.addons.crm.models.CRMLead;
import com.odoo.base.addons.res.ResPartner;
import com.odoo.base.addons.res.ResUsers;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.annotation.Odoo;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.orm.fields.types.OText;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;
import com.odoo.core.utils.ODateUtils;
import com.odoo.core.utils.reminder.ReminderUtils;

import org.json.JSONArray;

import java.util.Date;

import odoo.helper.ODomain;

public class CRMPhoneCalls extends OModel {
    public static final String TAG = CRMPhoneCalls.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.crm.provider.content.sync.crm_phonecall";
    private Context mContext;
    OColumn user_id = new OColumn("Responsable", ResUsers.class,
            OColumn.RelationType.ManyToOne).setRequired();
    OColumn partner_id = new OColumn("Contacto", ResPartner.class,
            OColumn.RelationType.ManyToOne).setRequired();
    OColumn description = new OColumn("Descripción", OText.class);
    OColumn state = new OColumn("estado", OSelection.class)
            .addSelection("open", "Confirmado")
            .addSelection("cancel", "Cancelado")
            .addSelection("pending", "Pendiente")
            .addSelection("done", "Retenida");
    OColumn name = new OColumn("Resumen de llamadas", OVarchar.class).setRequired();
    OColumn duration = new OColumn("Duración", OFloat.class);
    OColumn categ_id = new OColumn("Categoría", CRMPhoneCallsCategory.class,
            OColumn.RelationType.ManyToOne);
    OColumn date = new OColumn("Fecha", ODateTime.class);
    OColumn opportunity_id = new OColumn("Iniciativa/Oportunidad", CRMLead.class,
            OColumn.RelationType.ManyToOne);
    OColumn call_audio_file = new OColumn("archivo de audio grabado",
            OVarchar.class).setSize(200).setLocalColumn();
    OColumn data_type = new OColumn("Tipo de datos ", OVarchar.class).setSize(34)
            .setLocalColumn().setDefaultValue("phone_call");
    OColumn is_done = new OColumn("Marcar como hecho", OInteger.class)
            .setLocalColumn().setDefaultValue("0");
    OColumn partner_phone = new OColumn("Teléfono asociado", OVarchar.class).setSize(20);
    @Odoo.Functional(depends = {"opportunity_id"}, store = true, method = "storeLeadName")
    OColumn lead_name = new OColumn("Lead", OVarchar.class).setSize(100)
            .setLocalColumn();
    @Odoo.Functional(depends = {"user_id"}, store = true, method = "storeUserName")
    OColumn user_name = new OColumn("Username", OVarchar.class).setSize(100)
            .setLocalColumn();
    @Odoo.Functional(depends = {"partner_id"}, store = true, method = "storeCustomerName")
    OColumn customer_name = new OColumn("Username", OVarchar.class).setSize(100)
            .setLocalColumn();
    @Odoo.Functional(depends = {"categ_id"}, store = true, method = "storeCallType")
    OColumn call_type = new OColumn("Call Type", OVarchar.class).setSize(100)
            .setLocalColumn();
    OColumn has_reminder = new OColumn("Has reminder", OBoolean.class).setLocalColumn()
            .setDefaultValue("false");
    OColumn reminder_datetime = new OColumn("Reminder type", ODateTime.class)
            .setDefaultValue("false").setLocalColumn();

    OColumn color_index = new OColumn("Color index", OInteger.class).setSize(5)
            .setLocalColumn().setDefaultValue(6);

    public CRMPhoneCalls(Context context, OUser user) {
        super(context, "crm.phonecall", user);
        mContext = context;
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

    @Override
    public ODomain defaultDomain() {
        ODomain domain = new ODomain();
        domain.add("user_id", "=", getUser().getUserId());
        return domain;
    }

    public String storeLeadName(OValues values) {
        try {
            if (!values.getString("opportunity_id").equals("false")) {
                JSONArray opportunity_id = new JSONArray(values.getString("opportunity_id"));
                return opportunity_id.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String storeUserName(OValues values) {
        try {
            if (!values.getString("user_id").equals("false")) {
                JSONArray user_id = new JSONArray(values.getString("user_id"));
                return user_id.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String storeCustomerName(OValues values) {
        try {
            if (!values.getString("partner_id").equals("false")) {
                JSONArray partner_id = new JSONArray(values.getString("partner_id"));
                return partner_id.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String storeCallType(OValues values) {
        try {
            if (!values.getString("categ_id").equals("false")) {
                JSONArray categ_id = new JSONArray(values.getString("categ_id"));
                return categ_id.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    public void setReminder(int row_id) {
        ODataRow row = browse(row_id);
        Date start_date = ODateUtils.createDateObject(row.getString("date"),
                ODateUtils.DEFAULT_FORMAT, false);
        Date now = new Date();
        if (now.compareTo(start_date) < 0) {
            Bundle extra = row.getPrimaryBundleData();
            extra.putString(ReminderUtils.KEY_REMINDER_TYPE, "phonecall");
            if (ReminderUtils.get(mContext).resetReminder(start_date, extra)) {
                OValues values = new OValues();
                values.put("_is_dirty", "false");
                values.put("has_reminder", "true");
                update(row_id, values);
            }
        }
    }
}
