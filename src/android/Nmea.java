package cordova.plugin.nmea;

import cordova.plugin.nmea.model.*;
import android.content.pm.PackageManager;
import android.location.OnNmeaMessageListener;
import android.location.GpsStatus.NmeaListener;
import android.location.LocationListener;
import android.Manifest;
import android.os.Build;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;

import javax.security.auth.callback.Callback;
import android.location.LocationManager;
import org.apache.cordova.*;
import android.content.Context;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationProvider;

public class Nmea extends CordovaPlugin {

    String TAG = "GeolocationPlugin";
    CallbackContext context;
    String[] permissions = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
    String nmea = "";
    boolean nmeaStarted = false;
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private NmeaListener nmeaListnereInstance = null;

    private OnNmeaMessageListener onNmeaMessageListeneInstance = null;
    private NmeaGgaModel nmeaGgaObj = new NmeaGgaModel(0, "");
    private NmeaGllModel nmeaGllObj = new NmeaGllModel(0, "");
    private NmeaGsaModel nmeaGsaObj = new NmeaGsaModel(0, "");
    private NmeaGsvModel nmeaGsvObj = new NmeaGsvModel(0, "");
    private NmeaGrsModel nmeaGrsObj = new NmeaGrsModel(0, "");
    private NmeaGstModel nmeaGstObj = new NmeaGstModel(0, "");
    private NmeaRmcModel nmeaRmcObj = new NmeaRmcModel(0, "");
    private NmeaVtgModel nmeaVtgObj = new NmeaVtgModel(0, "");
    private NmeaZdaModel nmeaZdaObj = new NmeaZdaModel(0, "");
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.d(TAG, "Belépünk a végrehajtáshoz");
        /**
         * Test function
         */
        if (action.equals("testNmea")) {
            String nmeaTest = args.getString(0);
            callbackContext.success(nmeaTest);
            return true;
        }

        /**
         * Start NMEA Observer
         */
        if (action.equals("startNmea")) {
            String result = "Start Nmea Observer";
            if (this.hasPermisssion()) {
                if (this.nmeaStarted) {
                    LOG.d("NIK-1", result);
                    callbackContext.success(result);
                } else {
                    this.nmeaStart();
                    LOG.d("NIK-2", result);
                    callbackContext.success(result);
                }
                return true;
            } else {
                PermissionHelper.requestPermissions(this, 0, permissions);
                LOG.d("NIK-3", result);
                callbackContext.success(result);
            }
            return true;
        }

        /**
         * Stop NMEA Observer
         */
        if (action.equals("stopNmea")) {
            this.nmeaStop();
            callbackContext.success("Stop Nmea Observer");
            return true;
        }

        /**
         * Get NMEA-1083 ALL string
         */
        if (action.equals("getNmea")) {
            if (this.nmeaStarted == true) {
                System.out.println(nmea);
                callbackContext.success(nmea);
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GNGGA string
         */
        if (action.equals("getNmeaGga")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGgaObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPGLL string
         */
        if (action.equals("getNmeaGll")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGllObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPGRS string
         */
        if (action.equals("getNmeaGrs")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGrsObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPGSA string
         */
        if (action.equals("getNmeaGsa")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGsaObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPGST string
         */
        if (action.equals("getNmeaGst")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGstObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPGSV string
         */
        if (action.equals("getNmeaGsv")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaGsvObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GNRMC string
         */
        if (action.equals("getNmeaRmc")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaRmcObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPZDA string
         */
        if (action.equals("getNmeaZda")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaZdaObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        /**
         * Get NMEA-1083 GPVTG string
         */
        if (action.equals("getNmeaVtg")) {
            if (this.nmeaStarted == true) {
                callbackContext.success(nmeaVtgObj.getNmea());
            } else {
                callbackContext.success("");
            }
            return true;
        }

        else {
            return false;
        }
    }

    private void nmeaStart() {
        locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    LOG.d(TAG, location.getLatitude() + " - " + location.getLongitude());
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
                case LocationProvider.AVAILABLE:
                    break;
                }

            }

        };
        /*
        nmeaListnereInstance = new NmeaListener()  {
            @Override
            public void onNmeaReceived(long timestamp, String message) {*/
        onNmeaMessageListeneInstance = new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String message, long timestamp) {
                /*
                if (message.contains("GP")) {
                    if (nmea != message) {
                        nmea = message;
                    }
                }
                */
                if (message.contains("GPGGA")) {
                    nmea = message;
                    if (nmeaGgaObj.getNmea() != message) {
                        nmeaGgaObj.setTimestamp(timestamp);
                        nmeaGgaObj.setNmea(message);
                    }
                }

                if (message.contains("GPGLL")) {
                    nmea = message;
                    if (nmeaGllObj.getNmea() != message) {
                        nmeaGllObj.setTimestamp(timestamp);
                        nmeaGllObj.setNmea(message);
                    }
                }

                if (message.contains("GPGSA")) {
                    nmea = message;
                    if (nmeaGsaObj.getNmea() != message) {
                        nmeaGsaObj.setTimestamp(timestamp);
                        nmeaGsaObj.setNmea(message);
                    }
                }

                if (message.contains("GPGSV")) {
                    nmea = message;
                    if (nmeaGsvObj.getNmea() != message) {
                        nmeaGsvObj.setTimestamp(timestamp);
                        nmeaGsvObj.setNmea(message);
                    }
                }

                if (message.contains("GPGRS")) {
                    nmea = message;
                    if (nmeaGrsObj.getNmea() != message) {
                        nmeaGrsObj.setTimestamp(timestamp);
                        nmeaGrsObj.setNmea(message);
                    }
                }

                if (message.contains("GPGST")) {
                    nmea = message;
                    if (nmeaGstObj.getNmea() != message) {
                        nmeaGstObj.setTimestamp(timestamp);
                        nmeaGstObj.setNmea(message);
                    }
                }


                if (message.contains("GPRMC")) {
                    nmea = message;
                    if (nmeaRmcObj.getNmea() != message) {
                        nmeaRmcObj.setTimestamp(timestamp);
                        nmeaRmcObj.setNmea(message);
                    }
                }

                if (message.contains("GPZDA")) {
                    nmea = message;
                    if (nmeaZdaObj.getNmea() != message) {
                        nmeaZdaObj.setTimestamp(timestamp);
                        nmeaZdaObj.setNmea(message);
                    }
                }

                if (message.contains("GPVTG")) {
                    nmea = message;
                    if (nmeaVtgObj.getNmea() != message) {
                        nmeaVtgObj.setTimestamp(timestamp);
                        nmeaVtgObj.setNmea(message);
                    }
                }
            }
        };
        /*
        onNmeaMessageListeneInstance = new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String message, long timestamp) {
                if (message.contains("$")) {
                    if (nmea != message) {
                        nmea = message;
                    }
                }
            }
        };*/

        
       
        /*proba resz kezdete
        onNmeaMessageListeneInstance = new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String message, long timestamp) {

                if (nmeaAllObj.getNmea() != message) {
                    nmeaAllObj.setTimestamp(timestamp);
                    nmeaAllObj.setNmea(message);
                }
  
            }

        };*/
        // proba resz vege
        long time = 2000;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, 0.0f, locationListener);
        //locationManager.addNmeaListener(nmeaListnereInstance);
        locationManager.addNmeaListener(onNmeaMessageListeneInstance);
        nmeaStarted = true;
    }

    public void nmeaStop() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            //locationManager.removeNmeaListener(nmeaListnereInstance);
            locationManager.removeNmeaListener(onNmeaMessageListeneInstance);
        }
        nmeaStarted = false;
        nmea = "";
        nmeaGgaObj.setTimestamp(0);
        nmeaGgaObj.setNmea("");
        nmeaGllObj.setTimestamp(0);
        nmeaGllObj.setNmea("");
        nmeaGsaObj.setTimestamp(0);
        nmeaGsaObj.setNmea("");
        nmeaGsvObj.setTimestamp(0);
        nmeaGsvObj.setNmea("");
        nmeaGrsObj.setTimestamp(0);
        nmeaGrsObj.setNmea("");
        nmeaGstObj.setTimestamp(0);
        nmeaGstObj.setNmea("");
        nmeaRmcObj.setTimestamp(0);
        nmeaRmcObj.setNmea("");
        nmeaVtgObj.setTimestamp(0);
        nmeaVtgObj.setNmea("");
        nmeaZdaObj.setTimestamp(0);
        nmeaZdaObj.setNmea("");
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
            throws JSONException {
        if (context != null) {
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_DENIED) {
                    LOG.d(TAG, "Hozzáférés megtagadva!");
                }
            }
            LOG.d(TAG, "Hozzáférés Ok");
        }
    }

    @Override
    public boolean hasPermisssion() {
        for (String p : permissions) {
            if (!PermissionHelper.hasPermission(this, p)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestPermissions(int requestCode) {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }
}
