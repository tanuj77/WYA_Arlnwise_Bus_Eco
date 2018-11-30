package com.ccs.ccs81.wya_arlnwise_bus_eco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ContentProviderUser extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	TextView resultView=null;
    CursorLoader cursorLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contentprovider_user);
		resultView= (TextView) findViewById(R.id.res);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onClickDisplayNames(View view) {
		getSupportLoaderManager().initLoader(1, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		cursorLoader= new CursorLoader(this, Uri.parse("content://com.example.ccs81.myprovider/cte/1"), null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		cursor.moveToFirst();
		String res = "";
		//StringBuilder res=new StringBuilder();
        while (!cursor.isAfterLast()) {
        	//res.append("\n"+cursor.getString(cursor.getColumnIndex("id"))+ "-"+ cursor.getString(cursor.getColumnIndex("name")));
            res = cursor.getString(cursor.getColumnIndex("name"));
			cursor.moveToNext();
        }
        resultView.setText(res);

		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("ContentProviderCompanyName", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("CompanyName",res.trim());
		editor.commit();

		Intent i = new Intent(ContentProviderUser.this, MainActivity.class);
		startActivity(i);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDestroy() {
        super.onDestroy();
    }

}
