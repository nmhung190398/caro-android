package com.nmhung.caro.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;
import com.nmhung.caro.R;
import com.nmhung.caro.constant.Constant;
import com.nmhung.caro.model.ItemModel;
import com.nmhung.caro.model.SocketModel;
import com.nmhung.caro.model.UserModel;
import com.nmhung.caro.retrofit.RetrofitClient;
import com.nmhung.caro.retrofit.service.UserService;
import com.nmhung.caro.service.SocketService;
import com.nmhung.caro.utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nmhung.caro.constant.Constant.USER_LOGIN;

public class MainActivity extends AppCompatActivity {

    View map[][];

    private String roomId;
    boolean isWait = false;
    boolean isStart = false;
    Button btnSanSang;
    CircleImageView user1, user2;
    TableLayout tableLayout;
    TextView play01;
    TextView play02;

    public void initValue() {
        isWait = false;
        isStart = false;

        play01.setText(USER_LOGIN);
        play01.setTag(USER_LOGIN);

//        RetrofitClient.getAPIService(UserService.class).findByUsername(USER_LOGIN).enqueue(new Callback<UserModel>() {
//            @Override
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                setUserView(response.body(), false, findViewById(R.id.txtName1), findViewById(R.id.txtPoint1), findViewById(R.id.imgUser1));
//            }
//
//            @Override
//            public void onFailure(Call<UserModel> call, Throwable t) {
//
//            }
//        });
        addDataInUser(USER_LOGIN, false);

        btnSanSang.setText("sẵn sàng");
        btnSanSang.setTag(false);
    }

    public void setUserView(UserModel user, boolean isSanSang, TextView name, TextView point, CircleImageView imageView) {
        runOnUiThread(() -> {
            name.setText(user.getUsername());
            point.setText(user.getTotalWin() + " trận thắng");
            if (isSanSang) {
                imageView.setBorderColor(Color.parseColor("#5fcf80"));
            } else {
                imageView.setBorderColor(Color.parseColor("#2d2d2d"));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (USER_LOGIN == null) {
            onBackPressed();
            return;
        }

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        btnSanSang = findViewById(R.id.btnSanSang);
        user1 = findViewById(R.id.imgUser1);
        user2 = findViewById(R.id.imgUser2);
        createMap();
        tableLayout = this.findViewById(R.id.table);
        drawMap(tableLayout, map);
        socket = SocketService.socket();
//        socket.on("new_client", onNewClient);
//        socket.on("chat_message", onChatMessage)
        socket
                .on("clients", onClients)
                .on("attack", onAttack);
//        socket.connect();

        socket.on("socketInRoom", onSocketInRoom);
        socket.emit("joinRoomDone", roomId);
        socket.on("batDau", onBatDau);
        socket.on("endGame", onEndGame);
        findViews();
        setEvent();
        initValue();
    }

    void findViews() {
        play01 = findViewById(R.id.txtStatus1);

        play02 = findViewById(R.id.txtStatus2);
    }


    Socket socket;
    private Emitter.Listener onAttack = (Object... args) -> {
        System.out.println(args[0]);
        ItemModel itemModel = BaseActivity.gson.fromJson((String) args[0], ItemModel.class);
        if (itemModel.getRoomId().equalsIgnoreCase(roomId)) {
            View view = findByXY(itemModel.getX(), itemModel.getY());
            if (view != null) {
                view.setTag(itemModel);
                ViewUtils.of(this).setBackground(view, itemModel.getOwned().equals(USER_LOGIN) ? R.drawable.item_my_click : R.drawable.item_other_click);
                TextView textView = (TextView) view;
            }
        }


        this.isWait = itemModel.getOwned().equalsIgnoreCase(USER_LOGIN);
        if (isWait) {
            if (isWin(itemModel)) {
                runOnUiThread(() -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("roomId", roomId);
                    jsonObject.addProperty("winer", USER_LOGIN);
                    socket.emit("winer", jsonObject);
                });
                return;
            }
        }
        runOnUiThread(() -> {
            if (!isWait) {
                play01.setText("lượt bạn");
                play02.setText("");
            } else {
                play02.setText("đợi thằng này đi đã");
                play01.setText("");
            }
        });

    };

    private Emitter.Listener onEndGame = (Object... args) -> {
        JSONObject jsonObject = (JSONObject) args[0];
        String winner = null;
        try {
            winner = jsonObject.get("winer").toString();
            String finalWinner = winner;
            runOnUiThread(() -> {
                String msg = "BẠN THUA";
                if (finalWinner.equalsIgnoreCase(USER_LOGIN)) {
                    msg = "BẠN THẮNG RỒI NHÉ! AHIHI";
                }
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                initValue();
                JsonObject emitSanSang = new JsonObject();
                emitSanSang.addProperty("roomId", roomId);
                emitSanSang.addProperty("username", USER_LOGIN);
                emitSanSang.addProperty("isSanSang", false);
                socket.emit("sanSang", emitSanSang);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //reset game;


    };
    private Emitter.Listener onBatDau = (Object... args) -> {
        isStart = true;
        SocketModel socketModel = BaseActivity.gson.fromJson(args[0].toString(), SocketModel.class);
        runOnUiThread(() -> {
            ViewUtils.of(this).resetBackground(map, R.drawable.item_none_click);
            btnSanSang.setText("Chơi game đi bạn ơi");
            if (socketModel.getUsername().equalsIgnoreCase(USER_LOGIN)) {
                play01.setText("lượt bạn");
                play02.setText("");
                isWait = false;
            } else {
                play02.setText("đợi thằng này đi đã");
                play01.setText("");
                isWait = true;
            }

        });
    };

    private Emitter.Listener onSocketInRoom = (Object... args) -> {
        runOnUiThread(() -> {
            SocketModel[] socketModels = BaseActivity.gson.fromJson(args[0].toString(), SocketModel[].class);
            Map<String, SocketModel> socketModelMap = new HashMap<>();
            for (SocketModel socketModel : socketModels) {
                String username = socketModel.getUsername();
                addDataInUser(username, false);
                if (username.equalsIgnoreCase(USER_LOGIN)) {
                    if (socketModel.isSanSang()) {
                        play01.setText(" Sãn sàng");
                        btnSanSang.setText("Đợi thằng kia bạn nhé");
                        user1.setBorderColor(Color.parseColor("#5fcf80"));
                    } else {
                        play01.setText("");
                        btnSanSang.setText("sẵn sàng");
                        user1.setBorderColor(Color.parseColor("#ffffff"));
                    }
                    btnSanSang.setTag(socketModel.isSanSang());
                } else {
                    play02.setTag(username);
                    if (socketModel.isSanSang()) {
                        play02.setText("Sãn sàng");
                        user2.setBorderColor(Color.parseColor("#ffffff"));
                    } else {
                        play02.setText("");
                        user2.setBorderColor(Color.parseColor("#5fcf80"));

                    }
                }
            }
        });
    };

    private void addDataInUser(String username, boolean isSanSang) {
        RetrofitClient.getAPIService(UserService.class).findByUsername(username).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (username.equalsIgnoreCase(USER_LOGIN)) {
                    setUserView(response.body(), isSanSang, findViewById(R.id.txtName1), findViewById(R.id.txtPoint1), findViewById(R.id.imgUser1));
                } else {
                    setUserView(response.body(), isSanSang, findViewById(R.id.txtName2), findViewById(R.id.txtPoint2), findViewById(R.id.imgUser1));
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private Emitter.Listener onClients = (Object... args) -> {


        System.out.println();
    };


    private View findByXY(int x, int y) {
        try {
            return map[x][y];
        } catch (Exception e) {
            return null;
        }
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
        if (isStart) {
            ItemModel itemModel = (ItemModel) view.getTag();
            itemModel.setRoomId(roomId);
            Log.i("HDZ", itemModel.getX() + ":" + itemModel.getX());

            if (!itemModel.existOwned() && !isWait) {
                itemModel.setOwned(USER_LOGIN);
                socket.emit("attack", BaseActivity.gson.toJson(itemModel));
//                ViewUtils.of(this).setBackground(view, ahihi ? R.drawable.item_my_click : R.drawable.item_other_click);
            }
        }
    };


    void setEvent() {
        btnSanSang.setOnClickListener(view -> {
            if (isStart) {
                return;
            }
            boolean isSanSang = !(Boolean) view.getTag();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("roomId", roomId);
            jsonObject.addProperty("username", USER_LOGIN);
            jsonObject.addProperty("isSanSang", isSanSang);
            socket.emit("sanSang", jsonObject);
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Nếu thoát sẽ tính thua")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> {
                    socket.emit("leaveRoom", roomId);
                    if (isStart && play02.getTag() != null) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("roomId", roomId);
                        jsonObject.addProperty("winer", play02.getTag().toString());
                        socket.emit("winer", jsonObject);
                    }
                    Intent intent = new Intent(getBaseContext(), RoomActivity.class);
                    startActivity(intent);

                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

//        super.onBackPressed();
    }


    boolean isWin(ItemModel itemModel) {
        return isWinTrucX(itemModel) || isWinTrucY(itemModel) || isWinTrucCheo01(itemModel) || isWinTrucCheo02(itemModel);
    }

    boolean isWinTrucX(ItemModel itemModel) {
        int x = itemModel.getX();
        int y = itemModel.getY();
        View view = findByXY(x, y);
        int total = 1;
        int i = 1;
        while (isOwnerInView(findByXY(x + i, y), USER_LOGIN)) {
            total++;
            i++;
        }
        while (isOwnerInView(findByXY(x - i, y), USER_LOGIN)) {
            total++;
            i++;
        }
        if (total >= 5) {
            return true;
        }
        return false;
    }

    boolean isOwnerInView(View view, String username) {
        if (view == null) {
            return false;
        }
        ItemModel itemModel = getItemModelInView(view);
        if (itemModel == null) {
            return false;
        }
        String owner = itemModel.getOwned();
        if (owner == null) {
            return false;
        }
        return username.equalsIgnoreCase(owner);
    }

    boolean isWinTrucY(ItemModel itemModel) {
        int x = itemModel.getX();
        int y = itemModel.getY();
        View view = findByXY(x, y);
        int total = 1;
        int i = 1;
        while (isOwnerInView(findByXY(x, y + i), USER_LOGIN)) {
            total++;
            i++;
        }
        while (isOwnerInView(findByXY(x, y - i), USER_LOGIN)) {
            total++;
            i++;
        }
        if (total >= 5) {
            return true;
        }
        return false;
    }

    boolean isWinTrucCheo01(ItemModel itemModel) {
        int x = itemModel.getX();
        int y = itemModel.getY();
        View view = findByXY(x, y);
        int total = 1;
        int i = 1;
        while (isOwnerInView(findByXY(x + i, y + i), USER_LOGIN)) {
            total++;
            i++;
        }
        while (isOwnerInView(findByXY(x - i, y - i), USER_LOGIN)) {
            total++;
            i++;
        }
        if (total >= 5) {
            return true;
        }
        return false;
    }

    boolean isWinTrucCheo02(ItemModel itemModel) {
        int x = itemModel.getX();
        int y = itemModel.getY();
        View view = findByXY(x, y);
        int total = 1;
        int i = 1;
        while (isOwnerInView(findByXY(x - i, y + i), USER_LOGIN)) {
            total++;
            i++;
        }
        while (isOwnerInView(findByXY(x + i, y - i), USER_LOGIN)) {
            total++;
            i++;
        }
        if (total >= 5) {
            return true;
        }
        return false;
    }

    ItemModel getItemModelInView(View view) {
        if (view.getTag() instanceof ItemModel) {
            return (ItemModel) view.getTag();
        }
        return null;
    }
}
