package cc.aoeiuv020;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by AoEiuV020 on 2019.07.11-22:09:28.
 */
public class OpenAllActivity extends AppCompatActivity {
    private static final String TAG = "OpenAllActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_all);

        LogUtils.INSTANCE.i(TAG, getIntent());

        finish();
    }
}
