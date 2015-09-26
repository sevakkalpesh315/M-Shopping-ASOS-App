package com.asos.model.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.asos.R;
import com.asos.model.listeners.CommunicationInterface;
import com.asos.model.model.Category;
import com.asos.model.utilities.ServerURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class ViewPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater = null;

	private View parentView = null;
	private ListView categoryList = null;
	private List<Category> maleCategoryList = new ArrayList<Category>();
	private List<Category> femaleCategoryList = new ArrayList<Category>();
	private ArrayAdapter<Category> femaleAdapter = null;
	private ArrayAdapter<Category> maleAdapter = null;
	private CommunicationInterface nDrawerInterface = null;

	public void setnDrawerInterface(CommunicationInterface nDrawerInterface) {
		this.nDrawerInterface = nDrawerInterface;
	}

	public Object instantiateItem(final View collection, int position) {

		inflater = (LayoutInflater) collection.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		parentView = inflater.inflate(R.layout.category_list, null);
		categoryList = (ListView) parentView.findViewById(R.id.categoryList);

		switch (position) {
		case 0:

			maleAdapter = new CategoryListAdapter(collection.getContext(),
					maleCategoryList);

			categoryList.setAdapter(maleAdapter);

			new LoadJsonTask("MEN").execute(ServerURL.maleCategoryURL);

			break;
		case 1:

			femaleAdapter = new CategoryListAdapter(collection.getContext(),
					femaleCategoryList);

			categoryList.setAdapter(femaleAdapter);

			new LoadJsonTask("WOMEN").execute(ServerURL.femaleCategoryURL);

			break;
		}

		categoryList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Category temp = (Category) parent.getAdapter()
						.getItem(position);

				if (nDrawerInterface != null) {
					nDrawerInterface.closeDrawer();
					nDrawerInterface.showProducts(temp);
				}

			}
		});

		((ViewPager) collection).addView(parentView, 0);

		return parentView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	public int getCount() {
		return 2;
	}

	private class LoadJsonTask extends AsyncTask<String, Void, List<Category>> {

		private String currentCategory = "";

		public LoadJsonTask(String currentCategory) {
			this.currentCategory = currentCategory;
		}

		@Override
		protected List<Category> doInBackground(String... params) {

			List<Category> result = new ArrayList<Category>();

			try {
				URL u = new URL(params[0]);

				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setRequestMethod("GET");

				conn.connect();
				InputStream is = conn.getInputStream();

				byte[] b = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				while (is.read(b) != -1)
					baos.write(b);

				String JSONResp = new String(baos.toByteArray());

				JSONObject menCat = new JSONObject(JSONResp);

				JSONArray arr = menCat.getJSONArray("Listing");

				for (int i = 0; i < arr.length(); i++) {
					result.add(getCategory(arr.getJSONObject(i)));
				}

				return result;
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return null;
		}

		private Category getCategory(JSONObject obj) throws JSONException {

			Category category = new Category();

			category.setCategoryID(obj.getString("CategoryId"));
			category.setName(obj.getString("Name"));
			category.setProductCount(obj.getString("ProductCount"));
			category.setDescription(currentCategory);

			return category;
		}

		@Override
		protected void onPostExecute(List<Category> result) {

			if (currentCategory.equalsIgnoreCase("WOMEN")) {

				for (Category categoy : result) {
					femaleCategoryList.add(categoy);
					femaleAdapter.notifyDataSetChanged();
				}
			} else {
				for (Category categoy : result) {
					maleCategoryList.add(categoy);
					maleAdapter.notifyDataSetChanged();
				}
			}
		}
	}

}
