package bieren.nmct.howest.be.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.JsonReader;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import bieren.nmct.howest.be.belgischebieren2014.R;

/**
 * Created by Tomas on 12/10/14.
 */
public abstract class JsonLoader extends AsyncTaskLoader<Cursor> {
    private final String[] mColumnNames;
    private final String mPropertyName;
    private final boolean mIsJson;
    private final int mRawResourcesId;

    private Cursor mCursor;
    private Object lock = new Object();

    public JsonLoader(Context context, String propertyName, String[] columnNames, int rawResourceId, boolean isJson) {
        super(context);
        mPropertyName = propertyName;
        mColumnNames = columnNames;
        mRawResourcesId = rawResourceId;
        mIsJson = isJson;
    }

    @Override
    protected void onStartLoading() {
        if(mCursor != null) {
            deliverResult(mCursor);
        }

        if(takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if(mCursor == null) {
            loadCursor();
        }

        return mCursor;
    }

    private void loadCursor() {
        synchronized (lock) {
            if(mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream in;
            JsonReader reader;

            if(mIsJson) {
                in = getContext().getResources().openRawResource(mRawResourcesId);
                reader = new JsonReader(new InputStreamReader(in));
            } else {
                in = getContext().getResources().openRawResource(R.raw.bierenjson);
                reader = new JsonReader(new InputStreamReader(in));
            }


            try {
                reader.beginArray();
                while(reader.hasNext()) {
                    reader.beginObject();
                    parse(reader, cursor);
                    reader.endObject();
                }

                reader.endArray();

                mCursor = cursor;

            } catch (IOException e) {
                Log.e("ClassName", e.getMessage());
            } finally {
                try { reader.close(); } catch (IOException e) {Log.e("Catch Jsonloader", e.getMessage());}
                try { in.close(); } catch (IOException e) {}
            }
        }
    }

    private JSONArray convertToJson() {
        JSONArray arrJson = new JSONArray();
        InputStream in = getContext().getResources().openRawResource(R.raw.bieren);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            // skip first line
            String line = reader.readLine();

            line = reader.readLine();

            while(line != null) {
                JSONObject obj = createJsonObject(line);

                if(obj != null)
                    arrJson.put(obj);

                line = reader.readLine();
            }


        } catch (IOException e) {
            Log.e("convertToJson", e.getMessage());
        }

        return arrJson;
    }

    private JSONObject createJsonObject(String line) {
        String[] splitLine = line.split(";");

        if(splitLine.length != mColumnNames.length) {
            return null;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", splitLine[1]);
            obj.put("brewery", splitLine[2]);
            obj.put("color", splitLine[3]);
            obj.put("alcohol", splitLine[4]);
        } catch (Exception e) {
            Log.e("createJsonObject", e.getMessage());
        }

        return obj;
    }

    protected abstract void parse(JsonReader reader, MatrixCursor cursor) throws  IOException;
}
