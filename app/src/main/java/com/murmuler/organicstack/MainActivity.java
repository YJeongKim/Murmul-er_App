package com.murmuler.organicstack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.murmuler.organicstack.com.murmuler.organicstack.vo.RoomSummaryViewVO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String ROOT_CONTEXT = "http://www.murmul-er.com";
    private static RequestQueue requestQueue;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500;
    private static final int PLACE_PICKER_REQUEST=1;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Location mCurrentLocation;
    private LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    @BindView(R.id.layout_main)
    View mLayout;
    @BindView(R.id.editText)
    EditText editSearch;
    @BindView(R.id.btnSearch)
    ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);


        // 주기적으로 위치값을 받아서 현재 위치를 Setting 해주어야 할 경우 주석을 풀 것
        locationRequest = new LocationRequest();
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(FASTEST_UPDATE_INTERVAL_MS)
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    btnSearch.callOnClick();
                    return true;
                }
                return false;
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Geocoder geocoder = new Geocoder(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setDefaultLocation();

        // 위치 퍼미션을 가지고 있는지 Check
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // 사용자에게 퍼미션을 요청
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {
                // 사용자가 퍼미션 거부를 한적이 없는 경우에는 퍼미션 요청을 바로 함
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                showRoomList(mMap.getProjection().getVisibleRegion().latLngBounds);
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도 : " + location.getLatitude() + ", 경도 : " + location.getLongitude();

                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;

            }
        }
    };

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if(checkPermission()) {
                mMap.setMyLocationEnabled(true);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public void searchLocation(View view) {
        String keyword = editSearch.getText().toString();
        LatLng searchLatLng = getAddressCoordinate(keyword);
        if (searchLatLng != null) {
            if (currentMarker != null) currentMarker.remove();
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(searchLatLng);

//            String markerTitle = getCurrentAddress(searchLatLng);
//            String markerSnippet = "위도 : " + searchLatLng.latitude + ", 경도 : " + searchLatLng.longitude;

//            markerOptions.title(markerTitle);
//            markerOptions.snippet(markerSnippet);
//            markerOptions.draggable(true);

//            currentMarker = mMap.addMarker(markerOptions);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(searchLatLng);
            mMap.moveCamera(cameraUpdate);

            showRoomList(mMap.getProjection().getVisibleRegion().latLngBounds);
        }
    }

    public String getCurrentAddress(LatLng latlng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1
            );
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public LatLng getAddressCoordinate(String strAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        LatLng latLng = null;

        try {
            addresses = geocoder.getFromLocationName(
                    strAddress,
                    1
            );
        } catch (IOException ioException) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
        } else {
            Address address = addresses.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
        }
        return latLng;
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(false);

        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
    }

    public void setDefaultLocation() {
        LatLng DEFAULT_LOCATION =  new LatLng(37.4839778342191, 126.955578840377);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부를 확인하세요.";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }

    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE &&
             grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean checkResult = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false;
                    break;
                }
            }

            if (checkResult) {
                startLocationUpdates();
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                    Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }else {
                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                    Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }

        }
    }

    // GPS 활성화를 위한 메서드
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

    public void showRoomList (LatLngBounds latLngBounds) {
        String[] southWestSpl = latLngBounds.southwest.toString().substring(9).split(",");
        String southWest = southWestSpl[0] + ", " + southWestSpl[1];
        String[] northEastSpl = latLngBounds.northeast.toString().substring(9).split(",");
        String northEast = northEastSpl[0] + ", " + northEastSpl[1];

        String url = ROOT_CONTEXT + "/searchRoom/search?southWest=" + southWest + "&northEast=" + northEast;


        StringRequest request = new Utf8StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<RoomSummaryViewVO> roomList = getRoomList(response);
                        if (roomList == null) {
                            Toast.makeText(getApplicationContext(), "검색 된 매물이 없습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (currentMarker != null) currentMarker.remove();
                        for (RoomSummaryViewVO room : roomList) {
                            if (room.getPostType().equals("게시중")) {
                                LatLng position = new LatLng(room.getLatitude().doubleValue(), room.getLongitude().doubleValue());

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(position);
                                markerOptions.title(room.getSido() + " " + room.getSigungu() + " " + room.getRoadname());
                                markerOptions.snippet("[" + room.getRoomType() + "] " + room.getTitle());
                                markerOptions.draggable(false);

                                switch (room.getRoomType()) {
                                    case "아파트":
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.mk_ap)));
                                        break;
                                    case "오피스텔":
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.mk_of)));
                                        break;
                                    case "원룸":
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.mk_or)));
                                        break;
                                    case "투룸":
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.mk_tr)));
                                        break;
                                    case "빌라":
                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.mk_vi)));
                                        break;
                                }
                                currentMarker = mMap.addMarker(markerOptions);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public Bitmap resizeMarker(int id) {
        Bitmap bitmap = null;
        Bitmap smallMarker = null;
        bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
        return Bitmap.createScaledBitmap(bitmap, 100, 120, false);
    }

    class Utf8StringRequest extends StringRequest {
        public Utf8StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public Utf8StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String utf8String = new String(response.data, "UTF-8");
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                // log error
                return Response.error(new ParseError(e));
            } catch (Exception e) {
                // log error
                return Response.error(new ParseError(e));
            }
        }
    }

    public List<RoomSummaryViewVO> getRoomList(String response) {
        List<RoomSummaryViewVO> roomList = new ArrayList<>();
        JsonParser jsonParser =  new JsonParser();

        Object obj = jsonParser.parse(response);
        JsonObject jsonObj = (JsonObject) obj;

        for (int i = 0; i < jsonObj.size(); i++) {
            String temp = jsonObj.get("item" + i).toString();
            Object object = jsonParser.parse(temp.substring(3, temp.length() - 3).replace("\\\"", "\""));

            JsonObject jsonObject = (JsonObject) object;

            RoomSummaryViewVO roomSummaryViewVO = new RoomSummaryViewVO();
            List<String> roomOptions = new ArrayList<>();

            roomSummaryViewVO.setRoomId(Integer.parseInt(jsonObject.get("roomId").getAsString()));
            roomSummaryViewVO.setLatitude(jsonObject.get("latitude").getAsBigDecimal());
            roomSummaryViewVO.setLongitude(jsonObject.get("longitude").getAsBigDecimal());
            roomSummaryViewVO.setPostType(jsonObject.get("postType").getAsString());
            roomSummaryViewVO.setTitle(jsonObject.get("title").getAsString());
            String[] address = (jsonObject.get("address").getAsString().split(" "));
            roomSummaryViewVO.setSido(address[0]);
            roomSummaryViewVO.setSigungu(address[1]);
            roomSummaryViewVO.setRoadname(address[2]);
            roomSummaryViewVO.setPeriodNum(Integer.parseInt(jsonObject.get("period").getAsString().replaceAll("[^0-9]", "")));
            roomSummaryViewVO.setPeriodUnit(jsonObject.get("period").getAsString().replaceAll("[0-9]", ""));
            roomSummaryViewVO.setPostType(jsonObject.get("postType").getAsString());
            roomSummaryViewVO.setRoomType(jsonObject.get("roomType").getAsString());
            roomSummaryViewVO.setRentType(jsonObject.get("rentType").getAsString());
            roomSummaryViewVO.setArea(jsonObject.get("area").getAsDouble());
            roomSummaryViewVO.setDeposit(jsonObject.get("deposit").getAsInt());
            roomSummaryViewVO.setMonthlyCost(jsonObject.get("monthlyCost").getAsInt());
            roomSummaryViewVO.setManageCost(jsonObject.get("manageCost").getAsInt());
            roomSummaryViewVO.setWriteDate(Date.valueOf(jsonObject.get("writeDate").getAsString()));
            roomSummaryViewVO.setViews(jsonObject.get("views").getAsInt());
            roomSummaryViewVO.setRoomImg(jsonObject.get("roomImg").getAsString());
            for (JsonElement e : jsonObject.get("roomOptions").getAsJsonArray()) {
                roomOptions.add(e.getAsString());
            }
            roomSummaryViewVO.setRoomOptions(roomOptions);
            roomList.add(roomSummaryViewVO);
        }
        return roomList;
    }
}
