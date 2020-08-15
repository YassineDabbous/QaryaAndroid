package app.qarya.Chat;

import app.qarya.presentation.base.MyActivity;

import org.json.JSONObject;

import io.socket.emitter.Emitter;
import tn.core.util.Utilities;

public abstract class SocketListener implements Emitter.Listener {
    @Override
    public void call(Object... args) {
        String txt = ((JSONObject) args[0]).toString();
        MyActivity.log("SocketResponse join: => "+txt);
        SocketResponse r = Utilities.getGsonParser().fromJson(txt, SocketResponse.class);
        onResponse(r);
    }

    public abstract void onResponse(SocketResponse data);
}
