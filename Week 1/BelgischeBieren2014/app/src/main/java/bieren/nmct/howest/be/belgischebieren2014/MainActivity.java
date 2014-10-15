package bieren.nmct.howest.be.belgischebieren2014;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import bieren.nmct.howest.be.data.BeerLoader;


public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CursorAdapter mCursorAdapter;
    private String[] mColumnNames;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    public interface OnOrderSelectionListener {
        public void OnOrderSelected(String order);
    }

    private OnOrderSelectionListener mListener;
    private int mCurrCheckedPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


        getLoaderManager().initLoader(0, null, this);

        mColumnNames = new String[] {"brewery"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        int[] viewIds = new int[] {R.id.breweryName};

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.row_drawer, null, mColumnNames, viewIds, 0);
        mDrawerList.setAdapter(mCursorAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new BeerLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursorAdapter.swapCursor(null);
    }

}
