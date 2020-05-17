package com.nmhung.caro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.On;
import com.github.nkzawa.socketio.client.Socket;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.model.ItemModel;
import com.nmhung.caro.service.SocketService;
import com.nmhung.caro.utils.ViewUtils;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import static com.nmhung.caro.constant.Constant.BASE_URL;
import static com.nmhung.caro.constant.Constant.USER_LOGIN;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    View map[][];


    boolean ahihi = false;
    private Timer timer;
    boolean isWait = false;
    NumberProgressBar numberProgressBar;

    TableLayout tableLayout;

    Socket socket;
    private Emitter.Listener onAttack = (Object... args) -> {
        System.out.println(args[0]);
        ItemModel itemModel = BaseActivity.gson.fromJson((String) args[0], ItemModel.class);
        View view = findByXY(itemModel.getX(), itemModel.getY());
        if (view != null) {
            view.setTag(itemModel);
            ViewUtils.of(this).setBackground(view, itemModel.getOwned().equals(USER_LOGIN) ? R.drawable.item_my_click : R.drawable.item_other_click);
            TextView textView = (TextView) view;
            textView.setText("X");
        }
    };

    private Emitter.Listener onClients = (Object... args) -> {


        System.out.println();
    };


    private View findByXY(int x, int y) {
        return map[x][y];
    }


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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    numberProgressBar.incrementProgressBy(1);
                });
            }
        }, 1000, 100);
        ItemModel itemModel = (ItemModel) view.getTag();
        Log.i("HDZ", itemModel.getX() + ":" + itemModel.getX());

        if (!itemModel.existOwned() && !isWait) {
            itemModel.setOwned(USER_LOGIN);
            socket.emit("attack", BaseActivity.gson.toJson(itemModel));
//                ViewUtils.of(this).setBackground(view, ahihi ? R.drawable.item_my_click : R.drawable.item_other_click);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createMap();
        timer = new Timer();
        tableLayout = this.findViewById(R.id.table);
        drawMap(tableLayout, map);
        numberProgressBar = findViewById(R.id.number_progress_bar);

        numberProgressBar.setMax(100);
        socket = SocketService.socket();
//        socket.on("new_client", onNewClient);
//        socket.on("chat_message", onChatMessage)

        socket
                .on("clients", onClients)
                .on("attack", onAttack);
//        socket.connect();
    }


}
