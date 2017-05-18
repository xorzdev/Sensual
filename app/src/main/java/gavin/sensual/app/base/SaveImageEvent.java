package gavin.sensual.app.base;

import android.net.Uri;

public class SaveImageEvent {

    public Uri uri;

    public SaveImageEvent(Uri uri) {
        this.uri = uri;
    }

}