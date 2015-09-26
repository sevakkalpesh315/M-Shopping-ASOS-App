package com.asos.view.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asos.R;
import com.asos.model.database.Database;
import com.asos.model.listeners.CommunicationInterface;
import com.asos.model.model.Category;
import com.asos.model.model.Product;
import com.asos.view.fragment.DisplayFragment;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class MainActivity extends FragmentActivity implements
		CommunicationInterface {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private FragmentTransaction transaction = null;
	private DisplayFragment displayFragment = null;
	private Database database = null;
	private AlertDialog.Builder alertDialog = null;
	private MenuItem menuItem = null;
	private View myCustomMenuView = null;
	private TextView bagCounter = null;
	private int counter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		database = new Database(MainActivity.this);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getActionBar().setTitle("ASOS");
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("ASOS");
			}
		};

		drawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.center_action_bar);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		menuItem = menu.findItem(R.id.action_bag);

		myCustomMenuView = menuItem.getActionView();
		bagCounter = (TextView) myCustomMenuView
				.findViewById(R.id.bagCounterTxt);

		counter = database.getTotalShoppingCart();
		bagCounter.setText("" + counter);

		menuItem.getActionView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showShoppingCart();
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_star:
			showFavourite();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		counter = database.getTotalShoppingCart();
		bagCounter.setText("" + counter);
	}

	private void showShoppingCart() {

		ArrayList<Product> products = database.getProducts();

		if (products.size() == 0) {
			Toast.makeText(getApplicationContext(), "No Shopping Cart Found!",
					Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<String> favouriteProducts = new ArrayList<String>();

		for (Product current : products) {

			if (current.getIsAddedToShoppingCart().equalsIgnoreCase("YES")) {
				favouriteProducts.add(current.getProductID() + " - "
						+ current.getTitle());
			}
		}

		setUPDialog(favouriteProducts, "Shopping Cart List");

	}

	private void showFavourite() {

		ArrayList<Product> products = database.getProducts();

		if (products.size() == 0) {
			Toast.makeText(getApplicationContext(),
					"No Favourite Product Found!", Toast.LENGTH_SHORT).show();
			return;
		}

		ArrayList<String> favouriteProducts = new ArrayList<String>();

		for (Product current : products) {

			if (current.getIsFavourite().equalsIgnoreCase("YES")) {
				favouriteProducts.add(current.getProductID() + " - "
						+ current.getTitle());
			}
		}

		setUPDialog(favouriteProducts, "Favourite List");

	}

	@Override
	public void closeDrawer() {
		drawerLayout.closeDrawers();
	}

	@Override
	public void showProducts(Category category) {

		displayFragment = new DisplayFragment();
		displayFragment.setCategory(category);

		transaction = getFragmentManager().beginTransaction();

		transaction.setCustomAnimations(R.animator.enter, R.animator.exit);

		transaction.replace(R.id.content_frame, displayFragment);
		transaction
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();

	}

	private void setUPDialog(List<String> list, String title) {

		alertDialog = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.custom, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle(title);
		ListView lv = (ListView) convertView.findViewById(R.id.favouriteList);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);

		alertDialog.setPositiveButton("Close",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

}