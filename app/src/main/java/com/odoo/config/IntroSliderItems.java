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
 * Created on 13/2/15 4:16 PM
 */
package com.odoo.config;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odoo.R;
import com.odoo.core.utils.OControls;
import com.odoo.widgets.slider.SliderItem;
import com.odoo.widgets.slider.SliderPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroSliderItems implements SliderPagerAdapter.SliderBuilderListener {
    public static final String TAG = IntroSliderItems.class.getSimpleName();
    private Context mContext;

    public List<SliderItem> getItems() {
        List<SliderItem> items = new ArrayList<>();
        items.add(new SliderItem("Agenda Diaria", "Odoo CRM lo mantiene organizado, enfocado y más productivo.",
                R.drawable.slide_1, this)
                .putExtra("sub_title", "Administra todo en un solo lugar"));
        items.add(new SliderItem("ID de llamada en vivo", "Obtenga información sobre el cliente y las oportunidades recientes antes de que descuelgue el teléfono.",
        R.drawable.slide_3, this)
        .putExtra("sub_title", "Ver quién llama"));
        items.add(new SliderItem("Recordatorios", "Use recordatorios para asegurarse de que no se olviden llamadas telefónicas, reuniones u oportunidades.",
                R.drawable.slide_4, this)
                .putExtra("sub_title", "Recordatorio con acciones rápidas"));
        items.add(new SliderItem("Acciones fáciles", "Odoo CRM ofrece acciones simples, rápidas y fáciles a su alcance.",
                R.drawable.slide_5, this)
                .putExtra("sub_title", "Hacer las cosas rápidamente"));
        items.add(new SliderItem("Administrar citas", "Crear/Administrar citas y gestionar líneas de pedido fácilmente.",
                R.drawable.slide_6, this)
                .putExtra("sub_title", "Administre fácilmente las líneas de pedido"));
        items.add(new SliderItem("", "Todos los datos se sincronizan automáticamente con el servidor cuando te vuelves a conectar a Internet.",
                R.drawable.no_network, this)
                .putExtra("sub_title", "Funciona sin conexión"));
        items.add(new SliderItem("Empecemos", "",
                R.drawable.odoo_shaded, this)
                .putExtra("sub_title", "Comience a explorar Odoo CRM"));
        return items;
    }

    @Override
    public View getCustomView(Context context, SliderItem item, ViewGroup parent) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.base_intro_slider_view, parent, false);
        OControls.setText(view, R.id.big_title, item.getTitle());
        OControls.setImage(view, R.id.slider_image, item.getImagePath());
        OControls.setText(view, R.id.sub_title, item.getExtras().get("sub_title").toString());
        OControls.setText(view, R.id.description, item.getContent());
        return view;
    }
}
