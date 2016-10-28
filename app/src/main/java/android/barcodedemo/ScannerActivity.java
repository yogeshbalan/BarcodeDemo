package android.barcodedemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Random;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends BaseActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private View loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        loadingLayout = findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.GONE);
        setupToolbar();
        getSupportActionBar().setTitle("Bring the QR code");
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(this, "Contents = " + result.getContents() +
                ", Format = " + result.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        if (result.getBarcodeFormat().getName().equalsIgnoreCase("EAN13")) {
            //Toast.makeText(this, "first", Toast.LENGTH_SHORT).show();
            startLoadingAnimation(1);
        } else if (result.getBarcodeFormat().getName().equalsIgnoreCase("databar")) {
            //Toast.makeText(this, "second", Toast.LENGTH_SHORT).show();
            startLoadingAnimation(2);
        } else if (result.getBarcodeFormat().getName().equalsIgnoreCase("I25")) {
            //Toast.makeText(this, "third", Toast.LENGTH_SHORT).show();
            startLoadingAnimation(3);
        } else if (result.getBarcodeFormat().getName().equalsIgnoreCase("qrcode")) {
            //Toast.makeText(this, "fourth", Toast.LENGTH_SHORT).show();
            startLoadingAnimation(4);
        }

        //startLoadingAnimation();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScannerActivity.this);
            }
        }, 500);
    }

    public void startLoadingAnimation(int index) {
        loadingLayout.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.load_img);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
        //Glide.with(this).load(R.raw.loading_two).into(imageViewTarget);

        if (index == 1) {
            Glide.with(this).load(R.raw.loading_one).into(imageViewTarget);
        } else if (index == 2) {
            Glide.with(this).load(R.raw.loading_two).into(imageViewTarget);
        } else if (index == 3) {
            Glide.with(this).load(R.raw.loading_three).into(imageViewTarget);
        } else if (index == 4){
            Glide.with(this).load(R.raw.loading_four).into(imageViewTarget);
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}
