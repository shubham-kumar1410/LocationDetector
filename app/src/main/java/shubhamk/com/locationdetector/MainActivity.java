package shubhamk.com.locationdetector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;


public class MainActivity extends AppCompatActivity {
    TextView lat, lon;
    Button getLocation, openMap;
    Double latitude = null, longitude = null;
    GoogleApiClient apiClient;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lat=(TextView)findViewById(R.id.getlat);
        lon=(TextView)findViewById(R.id.getlon);
        getLocation=(Button)findViewById(R.id.getlocation);
        openMap = (Button) findViewById(R.id.openMap);
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .build();
        apiClient.connect();
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        MainActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            12345
                    );
                }
                Awareness.SnapshotApi.getLocation(apiClient)
                        .setResultCallback(new ResultCallback<LocationResult>() {
                            @Override
                            public void onResult(@NonNull LocationResult locationResult) {
                                if (!locationResult.getStatus().isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "Could not get location.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Location location = locationResult.getLocation();

                                // Log.i("TAG", "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude());
                                lat.setText(String.valueOf(location.getLatitude()));
                                lon.setText(String.valueOf(location.getLongitude()));
                                latitude = location.getLatitude();
                                longitude=location.getLongitude();
                            }
                        });

            }
        });

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (longitude != null && latitude != null) {
                    Intent intent = new Intent(getApplicationContext(), Maps.class);
                    intent.putExtra("Lat", latitude);
                    intent.putExtra("Lon", longitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Click the get location button", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
