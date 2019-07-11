package cc.aoeiuv020.open;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by AoEiuV020 on 2019.07.11-22:24:49.
 */
public class IntentUtils {
    @SuppressWarnings("ConstantConditions")
    private static Uri parseFilePath(Uri data) {
        if ("content".equalsIgnoreCase(data.getScheme())) {
            if ("com.tencent.mobileqq.fileprovider".equals(data.getHost())) {
                String filePath = data.getPath().substring("/external_files".length());
                return Uri.fromFile(new File(filePath));
            }
        }
        return data;
    }

    public static Intent parseFilePath(Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return intent;
        }
        intent.setData(parseFilePath(data));
        return intent;
    }
}
