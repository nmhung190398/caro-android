package com.nmhung.caro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.model.ItemModel;
import com.nmhung.caro.model.RoomModel;
import com.nmhung.caro.service.SocketService;
import com.nmhung.caro.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomActivity extends BaseActivity {
    public Socket socket;
    private Button btnCreateRoom;
    private TableLayout tableRooms;
    private Map<String, View> rooms;


    public RoomActivity() {
        super(R.layout.activity_room);
        rooms = new HashMap<>();
    }

    private void setTextRoom(View viewRoom) {
        Button button = (Button) viewRoom;
        RoomModel roomModel = (RoomModel) button.getTag();
        button.setText(roomModel.getName() + " - " + roomModel.getLength());
    }

    private Emitter.Listener onJoinRoom = (Object... args) -> {
        JSONObject jsonObject = (JSONObject) args[0];
        String msg = "Lỗi hệ thống";
        try {
            Boolean status = jsonObject.getBoolean("status");
            if (status) {
                joinRoom(jsonObject.getString("roomId"));
                return;
            }
            msg = jsonObject.getString("msg");

        } catch (JSONException e) {
            e.printStackTrace();

        }

//        Toast.makeText(this, msg, Toast.LENGTH_LONG);
    };

    private Emitter.Listener onRooms = (Object... args) -> {
        JSONObject jsonObject = (JSONObject) args[0];

        for (String roomId : rooms.keySet()) {
            setTextRoom(rooms.get(roomId));
        }


        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                setRoom(key, jsonObject.getJSONObject(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    };

    private void setRoom(String idRoom, JSONObject jsonObject) throws JSONException {
        if (!rooms.containsKey(idRoom)) {
            return;
        }
        Integer length = jsonObject.getInt("length");

        Button view = (Button) rooms.get(idRoom);
        RoomModel roomModel = (RoomModel) view.getTag();
        roomModel.setLength(length);
        view.setTag(roomModel);
        setTextRoom(view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket = SocketService.socket();
        socket.on("rooms", onRooms);
        socket.on("joinRoom", onJoinRoom);
        socket.connect();
        socket.emit("waitingRoom", Constant.USER_LOGIN);


        findViews();
        eventViews();
    }

    private void eventViews() {
        btnCreateRoom.setOnClickListener((View view) -> {
            socket.emit("createRoom", Constant.USER_LOGIN);
        });
    }

    protected void findViews() {
        btnCreateRoom = findViewById(R.id.createRoom);
        tableRooms = findViewById(R.id.tableRooms);
        createRoom(tableRooms);
    }

    @Override
    protected void setEvents() {

    }

    private void createRoom(TableLayout tableRooms) {
        int n = 6;
        int m = 3;
        int indexRoom = 0;
        for (int i = 0; i < n; ++i) {
            View[] viewRooms = new View[m];
            for (int j = 0; j < m; ++j) {
                String roomId = "" + (++indexRoom);
                RoomModel roomModel = new RoomModel(roomId);
                viewRooms[j] = createRoomView(roomModel);
                rooms.put(roomId, viewRooms[j]);
            }
            TableRow row = createRow(viewRooms);
            tableRooms.addView(row);
        }

        tableRooms.invalidate();
    }

    public TableRow createRow(View[] item) {
        TableRow row = new TableRow(this);
        row.setGravity(Gravity.CENTER);
        for (int i = 0; i < item.length; ++i) {
            row.addView(item[i]);
            row.invalidate();
        }
        return row;
    }

    public View createRoomView(RoomModel roomModel) {
        Button view = new Button(this);
        view.setText(roomModel.getName());
//        view.setWidth(Constant.WIDTH_BOX);
//        view.setHeight(Constant.HEIGHT_BOX);
//        ViewUtils.of(this).setBackground(view, R.drawable.item_none_click);
        view.setTag(roomModel);

        view.setOnClickListener(onClickJoinRoom);
        return view;
    }

    View.OnClickListener onClickJoinRoom = (View view) -> {
        RoomModel roomModel = (RoomModel) view.getTag();
        socket.emit("joinRoom", roomModel.getId());
        Log.i("HDZ", roomModel.getId());
    };


    void joinRoom(String roomId) {
        Intent intent = BaseActivity.newIntent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("roomId", roomId);
        this.startActivity(intent);
    }
}
