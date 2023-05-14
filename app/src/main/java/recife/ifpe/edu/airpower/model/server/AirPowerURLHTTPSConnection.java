package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import com.github.mikephil.charting.data.BarEntry;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import recife.ifpe.edu.airpower.model.repo.model.DeviceMeasurement;
import recife.ifpe.edu.airpower.util.AirPowerLog;

public class AirPowerURLHTTPSConnection {

    private static final String TAG = AirPowerURLHTTPSConnection.class.getSimpleName();

    private final int mConnectionTimeout;
    private final URL mURL;
    private final String mRequestType;
    private final List<Properties> properties;
    private final String requestBody;
    private HttpsURLConnection mHTTPSConnection = null;

    private AirPowerURLHTTPSConnection(Builder builder) {
        this.mURL = builder.mURL;
        this.mConnectionTimeout = builder.mConnectionTimeout;
        this.mRequestType = builder.mBuilderRequestType;
        this.properties = builder.headers;
        this.requestBody = builder.requestBody;

        buildConnection();
    }

    public void getServerResponse(AirPowerServerInterfaceWrapper.MeasureCallback callback) {
        try {
            int respStatus = mHTTPSConnection.getResponseCode();
            if (respStatus == HttpsURLConnection.HTTP_OK) {
                InputStream inputStream = mHTTPSConnection.getInputStream();
                DeviceMeasurement measurementSet = readServerResponse(inputStream);
                callback.onReceive(measurementSet);
            } else {
                InputStream errorStream = mHTTPSConnection.getErrorStream();
            }

        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Error while getting server response");
        }
    }

    private DeviceMeasurement readServerResponse(InputStream data) {
        DeviceMeasurement measurement;
        try {
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(1, 0));
            entries.add(new BarEntry(2, 0));
            entries.add(new BarEntry(3, 0));
            entries.add(new BarEntry(4, 400));
            entries.add(new BarEntry(5, 500));
            entries.add(new BarEntry(6, 600));
            entries.add(new BarEntry(7, 700));
            entries.add(new BarEntry(8, 700));
            entries.add(new BarEntry(9, 700));
            entries.add(new BarEntry(10, 502));
            entries.add(new BarEntry(11, 300));
            entries.add(new BarEntry(12, 400));
            entries.add(new BarEntry(13, 100));
            entries.add(new BarEntry(14, 800));
            entries.add(new BarEntry(15, 700));
            entries.add(new BarEntry(16, 800));
            entries.add(new BarEntry(17, 600));
            entries.add(new BarEntry(18, 700));
            entries.add(new BarEntry(19, 700));
            entries.add(new BarEntry(20, 200));
            entries.add(new BarEntry(21, 700));
            entries.add(new BarEntry(22, 800));
            entries.add(new BarEntry(23, 300));
            entries.add(new BarEntry(24, 700));
            entries.add(new BarEntry(25, 500));
            entries.add(new BarEntry(26, 700));
            entries.add(new BarEntry(27, 700));
            entries.add(new BarEntry(28, 900));
            entries.add(new BarEntry(29, 700));
            entries.add(new BarEntry(30, 600));
            entries.add(new BarEntry(31, 700));
            measurement = new DeviceMeasurement("token ", entries);
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Error while retrieving measurement set");
            return null;
        }
        return measurement;
    }

    private void buildConnection() {
        try {
            mHTTPSConnection = (HttpsURLConnection) mURL.openConnection();
            mHTTPSConnection.setConnectTimeout(mConnectionTimeout);
            mHTTPSConnection.setInstanceFollowRedirects(true);
            mHTTPSConnection.setDoOutput(true);
            mHTTPSConnection.setDoInput(true);
            mHTTPSConnection.setRequestMethod(mRequestType);
            mHTTPSConnection.setRequestProperty("Content-Type", "application/json");

            for (Properties property : properties) {
                mHTTPSConnection.setRequestProperty(property.getKey(), property.getValue());
            }

        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE)
                AirPowerLog.e(TAG, "Error while creating server connection");
        }
    }

    public static class Builder {
        private final URL mURL;
        private final List<Properties> headers = new ArrayList<>();
        private int mConnectionTimeout = 5000;
        private String mBuilderRequestType = "GET";
        private String requestBody;

        public Builder(URL url) {
            this.mURL = url;
        }

        public Builder setConnectionTimeout(int timeout) {
            this.mConnectionTimeout = timeout;
            return this;
        }

        public Builder setRequestType(String type) {
            this.mBuilderRequestType = type;
            return this;
        }

        public Builder setHeader(String key, String value) {
            this.headers.add(new Properties(key, value));
            return this;
        }

        public Builder setRequestBody(String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public AirPowerURLHTTPSConnection build() {
            return new AirPowerURLHTTPSConnection(this);
        }
    }

    private static class Properties {
        private final String key;
        private final String value;

        public Properties(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
