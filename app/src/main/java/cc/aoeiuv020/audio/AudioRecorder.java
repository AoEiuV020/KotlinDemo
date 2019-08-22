package cc.aoeiuv020.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("ALL")
public class AudioRecorder {
    private static final String TAG = "AudioRecorder";
    protected String pcmFileName;
    private int audioInput = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
    private int audioSampleRate = 8000;
    private int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    private int audioEncode = AudioFormat.ENCODING_PCM_16BIT;
    private int bufferSizeInBytes = 0;
    private AudioRecord audioRecord;
    private Status status = Status.STATUS_NO_READY;
    private Timer timer;

    private TimerTask timerTask;
    private int currentPosition = 0;
    private CallBack callBack;
    private int lastVolumn = 0;
    private AudioEncoder encoder;

    public AudioRecorder(CallBack callBack, AudioEncoder encoder) {
        pcmFileName = AudioFileUtils.getPcmFileAbsolutePath(System.currentTimeMillis() + "");
        this.encoder = encoder;

//        encoder.init(audioSampleRate, 16, 1);
        File file = new File(pcmFileName);
        if (file.exists()) {
            file.delete();
        }
        status = Status.STATUS_READY;
        this.callBack = callBack;
    }


    public void setAudioInput(int audioInput) {
        this.audioInput = audioInput;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public void setAudioChannel(int audioChannel) {
        this.audioChannel = audioChannel;
    }


    public void setEncoder(AudioEncoder encoder) {
        this.encoder = encoder;
    }

    private void startTimer() {
        if (timer == null)
            timer = new Timer();
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                currentPosition++;
                if (callBack != null && status == Status.STATUS_START) {
                    callBack.recordProgress(currentPosition);
                    callBack.volumn(lastVolumn);
                }

            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void startRecord() {

        bufferSizeInBytes = AudioRecord.getMinBufferSize(audioSampleRate,
                audioChannel, audioEncode);
        audioRecord = new AudioRecord(audioInput, audioSampleRate, audioChannel, audioEncode, bufferSizeInBytes);
        if (status == Status.STATUS_NO_READY) {
            throw new IllegalStateException("not init");
        }
        if (status == Status.STATUS_START) {
            throw new IllegalStateException("is recording ");
        }
        Log.d("AudioRecorder", "===startRecord===" + audioRecord.getState());
        audioRecord.startRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                recordToFile();
            }
        }).start();
        startTimer();
    }

    public void stop() {
        if (status != Status.STATUS_START) {
            throw new IllegalStateException("not recording");
        } else {
            stopRecorder();
            makeDestFile();
            status = Status.STATUS_READY;
        }
    }

    private void makeDestFile() {
        if (encoder == null)
            return;
        new Thread() {
            @Override
            public void run() {
                encoder.init(audioSampleRate, audioSampleRate * 16 * audioRecord.getChannelCount(), audioRecord.getChannelCount());
                encoder.encode(pcmFileName);
                releaseRecorder();
            }
        }.run();
    }

    public void release() {
        Log.d("AudioRecorder", "===release===");
        stopRecorder();
        releaseRecorder();
        status = Status.STATUS_READY;
        clearFiles();
    }

    private void releaseRecorder() {
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
    }

    private void stopRecorder() {
        stopTimer();
        if (audioRecord != null) {
            try {
                audioRecord.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearFiles() {
        if (encoder != null) {
            File file = new File(encoder.getDestFile());
            if (file.exists())
                file.delete();
        }
        File pcmfile = new File(pcmFileName);
        if (pcmfile.exists())
            pcmfile.delete();
    }

    private void recordToFile() {
        byte[] audiodata = new byte[bufferSizeInBytes];
        FileOutputStream fos = null;
        int readsize = 0;
        try {
            fos = new FileOutputStream(pcmFileName, true);
        } catch (FileNotFoundException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
        status = Status.STATUS_START;
        while (status == Status.STATUS_START && audioRecord != null) {
            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fos != null) {
                try {
                    ShortBuffer shorts = ByteBuffer.wrap(audiodata, 0, readsize)
                            .order(ByteOrder.LITTLE_ENDIAN)
                            .asShortBuffer();

                    int sum = 0;
                    for (int i = 0; i < shorts.limit(); i++) {
                        final short s = shorts.get(i);
                        int as = Math.abs(s);
                        double ds = 1.0 * as / 0x8000;
                        double qs = ds * 4;
                        if (qs > 0.75) {
                            // 超过0.75的部分收缩到0.75-1的范围，
                            qs = (qs - 0.75) / 13 + 0.75;
                        }
                        int an = (int) (qs * 0x8000);
                        int n;
                        if (s >=0) {
                            n = an;
                        } else {
                            n = -an;
                        }
                        shorts.put((short) n);
                        sum += an;
                    }

                    if (readsize > 0) {
                        int raw = sum / shorts.limit();
                        lastVolumn = raw;
                        Log.i(TAG, "writeDataTOFile: volumn -- " + raw + " / lastvolumn -- " + lastVolumn);
                    }


                    if (readsize > 0 && readsize <= audiodata.length)
                        fos.write(audiodata, 0, readsize);
                } catch (IOException e) {
                    Log.e("AudioRecorder", e.getMessage());
                }
            }
        }
        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }


    public Status getStatus() {
        return status;
    }


    public String getVoiceFilePath() {
        return encoder == null ? pcmFileName : encoder.getDestFile();
    }

    public enum Status {
        STATUS_NO_READY,
        STATUS_READY,
        STATUS_START,
        STATUS_PAUSE,
        STATUS_STOP
    }

    public interface CallBack {
        public void recordProgress(int progress);

        public void volumn(int volumn);
    }

}