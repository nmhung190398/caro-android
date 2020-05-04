package com.nmhung.caro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.nmhung.constant.Constant;
import com.nmhung.model.ItemModel;
import com.nmhung.utils.ViewUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    View map[][];

    boolean ahihi = false;

    TableLayout tableLayout;

    public MainActivity() {

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }

    void createMap() {
        map = new View[Constant.WIDTH_LENGTH][Constant.HEIGHT_LENGTH];
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                map[i][j] = createItem(i, j);
            }
        }
    }


    public TableRow createRow(View[] item) {
        TableRow row = new TableRow(this);
        for (int i = 0; i < item.length; ++i) {
            row.addView(item[i]);
            row.invalidate();
        }
        return row;
    }

    public void drawMap(TableLayout table, View[][] map) {
        table.removeAllViews();
        for (int i = 0; i < map.length; ++i) {
            TableRow row = createRow(map[i]);
            table.addView(row);
        }
        table.invalidate();
    }

    public View createItem(int i, int j) {
        TextView view = new TextView(this);
        view.setWidth(Constant.WIDTH_BOX);
        view.setHeight(Constant.HEIGHT_BOX);
        ViewUtils.of(this).setBackground(view, R.drawable.item_none_click);
        ItemModel itemModel = new ItemModel(null, i, j);
        view.setTag(itemModel);
        view.setOnClickListener(itemClick);
        return view;
    }


    public View.OnClickListener itemClick = (View view) -> {
        ItemModel itemModel = (ItemModel) view.getTag();
        Log.i("HDZ", itemModel.x + ":" + itemModel.y);
        if (!itemModel.existOwned()) {
            ViewUtils.of(this).setBackground(view, ahihi ? R.drawable.item_my_click : R.drawable.item_other_click);
            itemModel.setOwned("hdz");
            ahihi = !ahihi;
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createMap();
        tableLayout = this.findViewById(R.id.table);
        drawMap(tableLayout, map);
    }


}
