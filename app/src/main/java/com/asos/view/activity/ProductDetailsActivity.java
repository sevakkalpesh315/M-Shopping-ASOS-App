package com.asos.view.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asos.R;
import com.asos.model.database.Database;
import com.asos.model.model.Product;
import com.asos.model.model.ProductDetail;
import com.asos.model.utilities.ServerURL;
import com.asos.model.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class ProductDetailsActivity extends Activity {

	private TextView brandName = null;
	private Product product = null;
	private ViewPager viewPager = null;
	private ProgressDialog progressDialog = null;
	private List<String> imgURLS = new ArrayList<String>();

	private Database database = null;

	private Button addToBag = null;
	private TextView t1, t2, t3, t4;

	private AlertDialog.Builder alertDialog = null;

	private MenuItem menuItem = null;
	private View myCustomMenuView = null;
	private TextView bagCounter = null;
	private int counter = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.center_action_bar);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeAsUpIndicator(R.drawable.back);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		product = (Product) getIntent().getSerializableExtra("selectedProduct");

		initFields();

		brandName.setText(product.getBrand());

		new LoadJsonTask().execute(ServerURL.productDetailsURL
				+ product.getProductID());

		addToBag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(),
						"Current Product: " + product.getProductID(),
						Toast.LENGTH_SHORT).show();

				if (database.shoppingCart(product)) {
					Toast.makeText(ProductDetailsActivity.this,
							"Added to Shopping Cart List!", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ProductDetailsActivity.this,
							"Removed from Shopping Cart List!",
							Toast.LENGTH_SHORT).show();
				}

				notifyCart();

			}
		});

	}

	protected void notifyCart() {
		counter = database.getTotalShoppingCart();
		bagCounter.setText("" + counter);
	}

	private void initFields() {

		database = new Database(ProductDetailsActivity.this);

		brandName = (TextView) this.findViewById(R.id.productBrand);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		addToBag = (Button) this.findViewById(R.id.addToBag);
		t1 = (TextView) this.findViewById(R.id.textView1);
		t2 = (TextView) this.findViewById(R.id.textView2);
		t3 = (TextView) this.findViewById(R.id.textView3);
		t4 = (TextView) this.findViewById(R.id.textView4);
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private ProductDetail result = null;

		public ImagePagerAdapter(ProductDetail result) {
			this.result = result;
		}

		@Override
		public int getCount() {
			return result.getImgURLS().size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = ProductDetailsActivity.this;
			ImageView imageView = new ImageView(context);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			new ImageLoadTask(imageView).execute(result.getImgURLS().get(
					position));
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
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
		switch (item.getItemId()) {

		case R.id.action_star:
			showFavourite();
			return true;
		case android.R.id.home:
			ProductDetailsActivity.super.onBackPressed();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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

	private class LoadJsonTask extends AsyncTask<String, Void, ProductDetail> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ProductDetailsActivity.this);
			progressDialog.setTitle("Please Wait...");
			progressDialog.setMessage("Getting Product #"
					+ product.getProductID() + " details");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected ProductDetail doInBackground(String... params) {

			ProductDetail pd = new ProductDetail();

			try {

				String response = Utils.downloadFileFromInternet(params[0]);

				JSONObject root = new JSONObject(response);

				pd.setBasePrice(root.getString("BasePrice"));

				pd.setCurrentPrice(root.getString("CurrentPrice"));

				pd.setInStock(root.getString("InStock"));
				pd.setTitle(root.getString("Title"));
				pd.setAdditionalInfo(root.getString("AdditionalInfo"));
				pd.setIsInSet(root.getString("IsInSet"));

				JSONArray arr = root.getJSONArray("ProductImageUrls");

				for (int i = 0; i < arr.length(); i++) {

					imgURLS.add(arr.getString(i));

				}

				pd.setImgURLS(imgURLS);

				return pd;

			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ProductDetail result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();

			ImagePagerAdapter adapter = new ImagePagerAdapter(result);
			viewPager.setAdapter(adapter);

			addToBag.setText("Add to bag (" + result.getCurrentPrice() + ")");
			t1.setText("Product Title: " + result.getTitle());
			t2.setText("Available in Stock: " + result.getInStock());
			t3.setText("Is in Set: " + result.getIsInSet());
			t4.setText("Additional Info: "
					+ result.getAdditionalInfo().replaceAll("\\s+", ""));
		}
	}

	private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		private Bitmap bitmap;

		ImageView bmImage;

		public ImageLoadTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				InputStream input = Utils.getInputStreamFromUrl(params[0]);

				bitmap = BitmapFactory.decodeStream(input);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			bmImage.setImageBitmap(result);
		}

	}

	private void setUPDialog(List<String> list, String title) {

		alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
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