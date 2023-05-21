package recife.ifpe.edu.airpower.model.server;

/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: AirPower
 */

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import recife.ifpe.edu.airpower.util.AirPowerLog;

class AirPowerConnection {

    private static final String TAG = AirPowerConnection.class.getSimpleName();

    public static final String TYPE_GET = "GET";
    public static final String TYPE_POST = "POST";
    public static final String TYPE_DELETE = "DELETE";
    public static final String TYPE_UPDATE = "UPDATE";

    private final int mConnectionTimeout;
    private final URL mURL;
    private final String mRequestType;
    private final List<Properties> properties;
    private HttpURLConnection mHTTPConnection = null;

    private AirPowerConnection(Builder builder) {
        this.mURL = builder.url;
        this.mConnectionTimeout = builder.connectionTimeout;
        this.mRequestType = builder.requestType;
        this.properties = builder.headers;

        buildConnection();
    }

    private void buildConnection() {
        try {
            mHTTPConnection = (HttpURLConnection) mURL.openConnection();
            mHTTPConnection.setConnectTimeout(mConnectionTimeout);
            mHTTPConnection.setInstanceFollowRedirects(true);
            mHTTPConnection.setDoInput(true);
            mHTTPConnection.setRequestMethod(mRequestType);
            if (!mRequestType.equals(AirPowerConnection.TYPE_GET)) {
                mHTTPConnection.setRequestProperty("Content-Type", "application/json");
                mHTTPConnection.setDoOutput(true);
            }
            for (Properties property : properties) {
                mHTTPConnection.setRequestProperty(property.getKey(), property.getValue());
            }
        } catch (Exception e) {
            if (AirPowerLog.ISLOGABLE) AirPowerLog.e(TAG, "Error while creating server connection");
            e.printStackTrace();
        }
    }

    HttpURLConnection getHttpURLConnection() {
        return this.mHTTPConnection;
    }

    static class Builder {
        private final URL url;
        private final List<Properties> headers = new ArrayList<>();
        private int connectionTimeout = 5000;
        private String requestType = "GET";

        public Builder(URL url) {
            this.url = url;
        }

        public Builder setConnectionTimeout(int timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        public Builder setRequestType(String type) {
            this.requestType = type;
            return this;
        }

        public Builder setHeader(String key, String value) {
            this.headers.add(new Properties(key, value));
            return this;
        }

        public AirPowerConnection build() {
            return new AirPowerConnection(this);
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
