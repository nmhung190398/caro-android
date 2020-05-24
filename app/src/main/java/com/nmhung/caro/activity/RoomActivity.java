package com.nmhung.caro.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.model.RoomModel;
import com.nmhung.caro.model.UserModel;
import com.nmhung.caro.retrofit.RetrofitClient;
import com.nmhung.caro.retrofit.service.UserService;
import com.nmhung.caro.service.SocketService;
import com.nmhung.caro.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        runOnUiThread(() -> {
            Button button = (Button) viewRoom;
            RoomModel roomModel = (RoomModel) button.getTag();
            button.setText(roomModel.getName() + " - " + roomModel.getLength());
        });
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
        String finalMsg = msg;
        runOnUiThread(() -> {
            Toast.makeText(getBaseContext(), finalMsg, Toast.LENGTH_LONG).show();
        });
    };

    private Emitter.Listener onRooms = (Object... args) -> {
        JSONObject jsonObject = (JSONObject) args[0];
        runOnUiThread(() -> {
            for (String roomId : rooms.keySet()) {
                Button button = (Button) rooms.get(roomId);
                RoomModel roomModel = (RoomModel) button.getTag();
                roomModel.setLength(0);
                button.setTag(roomModel);
                button.setText(roomModel.getName() + " - " + roomModel.getLength());
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
                layoutParams.setMargins(5, 5, 5, 5);
                button.setPadding(5, 5, 5, 5);
                layoutParams.weight = 1;
                button.setLayoutParams(layoutParams);
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

        });
    };

    private void setRoom(String idRoom, JSONObject jsonObject) throws JSONException {
        if (!rooms.containsKey(idRoom)) {
            return;
        }
        Button view = (Button) rooms.get(idRoom);
        Integer length = jsonObject.getInt("length");
        if (length == 2) {
            ViewUtils.of(this).setBackground(view, R.drawable.bg_button_room_full);
            view.setTextColor(Color.parseColor("#ffffff"));
        }

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
        socket.emit("newPlayer", Constant.USER_LOGIN);


    }

    protected void setEvents() {

        findViewById(R.id.bangXepHang).setOnClickListener(view -> {

            RetrofitClient.getAPIService(UserService.class).bangXepHang().enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                    Dialog_Hall dialog_hall = new Dialog_Hall(response.body());
                    dialog_hall.show(getSupportFragmentManager(), dialog_hall.getTag());
                }

                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable t) {

                }
            });


        });
    }

    private List<UserModel> createHeroList() {
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));
        list.add(new UserModel("Thor", "", 5));

        return list;
    }

    protected void findViews() {
//        btnCreateRoom = findViewById(R.id.createRoom);
        tableRooms = findViewById(R.id.tableRooms);
        createRoom(tableRooms);
    }

    private void createRoom(TableLayout tableRooms) {
        int n = 36;
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
            row.invalidate();
            tableRooms.addView(row);
        }

        tableRooms.invalidate();
    }

    public TableRow createRow(View[] item) {
        TableRow row = new TableRow(this);
        row.setPadding(10, 0, 10, 0);
        row.setGravity(Gravity.CENTER);
        for (int i = 0; i < item.length; ++i) {
            row.addView(item[i]);
            row.invalidate();
        }
        return row;
    }

    public View createRoomView(RoomModel roomModel) {
        Button view = new Button(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(getResources().getDrawable(R.drawable.bg_button_room));
            view.setTextColor(Color.parseColor("#ffffff"));
        }

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

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn thoát")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

//        super.onBackPressed();
    }


}
