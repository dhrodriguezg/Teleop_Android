package rosedmonton.teleop;

import android.os.Bundle;

import org.ros.address.InetAddressFactory;
import org.ros.android.NodeMainExecutorService;
import org.ros.android.RosActivity;
import org.ros.android.view.VirtualJoystickView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.net.URI;

/**
 * Created by dhrodriguezg on 11/12/15.
 */
public class JoyActivity extends RosActivity {

    private static final String TAG = "JoyActivity";
    private VirtualJoystickView joystick;
    private NodeMainExecutorService nodeMain;

    public JoyActivity() {
        super(TAG, TAG, URI.create(Teleop.ROS_MASTER));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick_teleop);

        joystick = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        joystick.setHolonomic(false);
        joystick.setTopicName("/android/joystick");
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
    }
}
