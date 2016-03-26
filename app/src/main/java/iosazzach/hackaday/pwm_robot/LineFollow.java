package iosazzach.hackaday.pwm_robot;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

//public class LineFollow extends AppCompatActivity {
public class LineFollow extends Activity {

    private CameraBridgeViewBase openCVCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Success", "OpenCV loaded successfully");
                    openCVCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_line_follow);

        openCVCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraView);

        openCVCameraView.setVisibility(SurfaceView.VISIBLE);

        fixLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    /**
     * This fixes the linear layout in this activity.
     *
     * JavaCameraView doesn't work properly in portrait mode so I lo
     */
    private void fixLayout() {
        LinearLayout rotatedLayout = (LinearLayout) findViewById(R.id.rotatedLayout);

        rotatedLayout.setTranslationY(-1);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams lp = rotatedLayout.getLayoutParams();
        lp.height = size.x;
        lp.width = size.y;
        rotatedLayout.requestLayout();
    }
}
