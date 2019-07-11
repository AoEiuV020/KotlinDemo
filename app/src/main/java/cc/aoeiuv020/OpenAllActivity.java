package cc.aoeiuv020;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cc.aoeiuv020.open.IntentUtils;

/**
 * Created by AoEiuV020 on 2019.07.11-22:09:28.
 */
@SuppressWarnings("ConstantConditions")
public class OpenAllActivity extends AppCompatActivity {
    private static final String TAG = "OpenAllActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_all);

        IntentUtils.parseFilePath(getIntent());

        LogUtils.INSTANCE.i(TAG, getIntent());

        File fData = new File((getIntent().getData().getPath()));
        Log.i(TAG, "exists: " + fData.exists());

        finish();
    }
}
