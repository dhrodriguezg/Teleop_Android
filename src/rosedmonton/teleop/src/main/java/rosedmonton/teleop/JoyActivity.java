package rosedmonton.teleop;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.NodeMainExecutorService;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.VirtualJoystickView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

import sensor_msgs.CompressedImage;

/**
 * Created by dhrodriguezg on 11/12/15.
 */
public class JoyActivity extends RosActivity {

    private static final String TAG = "JoyActivity";
    private VirtualJoystickView joystick;
    private RosImageView<CompressedImage> imageStreamNodeMain;
    private NodeMainExecutorService nodeMain;

    public JoyActivity() {
        super(TAG, TAG, URI.create(Teleop.ROS_MASTER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.joystick_teleop);

        joystick = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        joystick.setHolonomic(false);
        joystick.setTopicName("/android/joystick");

        imageStreamNodeMain = (RosImageView<CompressedImage>) findViewById(R.id.streamingView);
        imageStreamNodeMain.setMessageToBitmapCallable(new BitmapFromCompressedImage());
        imageStreamNodeMain.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageStreamNodeMain.setTopicName("/camera/image/compressed");
        imageStreamNodeMain.setMessageType("sensor_msgs/CompressedImage");
    }

    @Override
    public void onDestroy() {
        nodeMain.forceShutdown();
        super.onDestroy();
    }

    @Override
    public void init(NodeMainExecutor nodeMainExecutor) {
        nodeMain=(NodeMainExecutorService)nodeMainExecutor;
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(), getMasterUri());
        nodeMainExecutor.execute(joystick, nodeConfiguration.setNodeName("/android/teleop_node"));
        nodeMainExecutor.execute(imageStreamNodeMain, nodeConfiguration.setNodeName("/android/camera_receiver"));
    }
}
