package bieren.nmct.howest.be.data;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;

import java.io.IOException;

import bieren.nmct.howest.be.belgischebieren2014.R;

/**
 * Created by Tomas on 12/10/14.
 */
public class BeerLoader extends JsonLoader{
    public BeerLoader(Context context) {
        super(context, "beers", new String[] {"name", "brewery", "color", "alcohol"}, R.raw.bieren, false);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {
        int id = 1;
        while(reader.hasNext()) {
            String beerValue = reader.nextString();
            MatrixCursor.RowBuilder row = cursor.newRow();
            row.add(id);
            row.add(beerValue);
            id++;
        }

    }
}
