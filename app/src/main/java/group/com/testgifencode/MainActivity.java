package group.com.testgifencode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.gifencoder.Color;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.Image;
import com.squareup.gifencoder.ImageOptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button create;
    private Button download;
    private Button start;
    private Button stop;
    private GifImageView gifImageView;
    private GifDecoder gifDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create = (Button) findViewById(R.id.button0);
        download = (Button) findViewById(R.id.button1);
        start = (Button) findViewById(R.id.button2);
        stop = (Button) findViewById(R.id.button3);
        gifImageView = (GifImageView) findViewById(R.id.gif_imageview);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getCacheDir(),"test.gif");
                try {
                    if(file.exists()){
                        file.delete();
                    }
                    file.createNewFile();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    ImageOptions imageOptions = new ImageOptions();
                    imageOptions.setDelay(200, TimeUnit.MILLISECONDS);
                    int size = 200;
                    int image1[][] = new int[size][size];
                    int image2[][] = new int[size][size];
                    int image3[][] = new int[size][size];
                    int image4[][] = new int[size][size];
                    setColor(image1,size);
                    setColor(image2,size);
                    setColor(image3,size);
                    setColor(image4,size);
                    GifEncoder gifEncoder = new GifEncoder(bufferedOutputStream, getResources().getDisplayMetrics().widthPixels,
                            getResources().getDisplayMetrics().heightPixels, 0);
                    gifEncoder.addImage(image1, imageOptions);
                    gifEncoder.addImage(image2, imageOptions);
                    gifEncoder.addImage(image3, imageOptions);
                    gifEncoder.addImage(image4, imageOptions);
                    gifEncoder.finishEncoding();
                    bufferedOutputStream.close();

                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    gifDecoder = new GifDecoder();
                    if (0 == gifDecoder.read(bufferedInputStream, (int) file.length())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "gif下载完成！", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://upload-images.jianshu.io/upload_images/2406298-759c803c0ee295d7.gif?imageMogr2/auto-orient/strip");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setConnectTimeout(30000);
                            httpURLConnection.setReadTimeout(30000);
                            int statusCode = httpURLConnection.getResponseCode();
                            if (200 <= statusCode && statusCode <= 299) {
                                InputStream inputStream = httpURLConnection.getInputStream();
                                gifDecoder = new GifDecoder();
                                if (0 == gifDecoder.read(inputStream, httpURLConnection.getContentLength())) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "gif下载完成！", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != gifDecoder) {
                    gifImageView.start(gifDecoder);
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != gifDecoder) {
                    gifImageView.stop();
                }
            }
        });
    }

    private void setColor(int [][]image,int size){
        for (int i = 0; i < size; ++i) {
            for(int j = 0;j < size;++j){
                int rand = (int) (10 * Math.random());
                if(rand > 7) {
                    image[i][j] = Color.BLACK.getRgbInt();
                }else if(rand > 4){
                    image[i][j] = Color.RED.getRgbInt();
                }else{
                    image[i][j] = Color.GREEN.getRgbInt();
                }
            }
        }
    }

}
