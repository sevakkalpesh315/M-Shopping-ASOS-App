package com.asos.view.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.asos.R;
import com.asos.model.adapter.ProductAdapter;
import com.asos.model.model.Category;
import com.asos.model.model.Product;
import com.asos.model.utilities.ServerURL;
import com.asos.model.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class DisplayFragment extends Fragment {

	private View parentView;

	private TextView myText = null;
	private Category category = null;

	private ProgressDialog progressDialog = null;

	private GridView gridView = null;
	private List<Product> list = new ArrayList<Product>();
	private BaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.category_display, container,
				false);

		myText = (TextView) parentView.findViewById(R.id.fragmentText);
		myText.setText(getCategory().getName());

		gridView = (GridView) parentView.findViewById(R.id.gridView1);

		adapter = new ProductAdapter(getActivity(), list);

		gridView.setAdapter(adapter);

		new LoadJsonTask().execute(ServerURL.productByCategoryURL
				+ getCategory().getCategoryID());

		return parentView;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	private class LoadJsonTask extends AsyncTask<String, Product, Void> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setTitle("Please Wait");
			progressDialog.setMessage("Getting Product Informations ...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {

			try {

				String response = Utils.downloadFileFromInternet(params[0]);

				JSONObject root = new JSONObject(response);

				JSONArray arr = root.getJSONArray("Listings");

				for (int i = 0; i < arr.length(); i++) {

					JSONObject obj = arr.getJSONObject(i);

					publishProgress(getProducts(obj));

				}

				return null;

			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}

		private Product getProducts(JSONObject obj) throws JSONException {

			Product product = new Product();

			product.setBasePrice(obj.getString("BasePrice"));
			product.setBrand(obj.getString("Brand"));
			product.setCurrentPrice(obj.getString("CurrentPrice"));
			product.setHasMoreColors(obj.getString("HasMoreColours"));
			product.setIsInSet(obj.getString("IsInSet"));
			product.setPreviousPrice(obj.getString("PreviousPrice"));
			product.setProductID(obj.getString("ProductId"));
			product.setProductImgURL(obj.getJSONArray("ProductImageUrl")
					.getString(0));
			product.setRpp(obj.getString("RRP"));
			product.setTitle(obj.getString("Title"));

			return product;
		}

		@Override
		protected void onProgressUpdate(Product... values) {
			list.add(values[0]);
			adapter.notifyDataSetChanged();

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

	}

}