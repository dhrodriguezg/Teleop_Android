package rosedmonton.teleop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Teleop extends Activity {
    public static String ROS_MASTER = "";
    private EditText rosIP;
    private EditText rosPort;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rosIP = (EditText) findViewById(R.id.editIP);
        rosPort = (EditText) findViewById(R.id.editPort);

        Button controllerButton = (Button) findViewById(R.id.launchButton);
        controllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMasterValid()){
                    Intent myIntent = new Intent(Teleop.this, JoyActivity.class);
                    Teleop.this.startActivity(myIntent);
                }
            }
        });
    }

    private boolean isMasterValid(){
        ROS_MASTER = "http://" + rosIP.getText().toString() + ":" + rosPort.getText().toString();
        int exit = pingHost(rosIP.getText().toString());
        if (exit!=0){
            Toast.makeText(getApplicationContext(), rosIP.getText().toString() + " is not reachable!!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private static int pingHost(String host){
        int exit = -1;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("ping -c 1 " + host);
            proc.waitFor();
            exit = proc.exitValue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return exit;
    }
}
