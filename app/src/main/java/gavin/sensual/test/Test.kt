package gavin.sensual.test

import android.content.Context
import android.widget.TextView
import android.widget.Toast

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/22
 */
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

var TextView.text: CharSequence
    get() = getText()
    set(v) = setText(v)