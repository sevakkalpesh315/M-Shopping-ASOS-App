package com.asos.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asos.R;
import com.asos.model.database.Database;
import com.asos.model.model.Product;
import com.asos.model.utilities.Utils;
import com.asos.view.activity.ProductDetailsActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class ProductAdapter extends BaseAdapter {

	private List<Product> list = null;
	private Context context = null;
	private Holder holder = null;
	private View rowView = null;
	private Database database = null;
	private Product currentProduct = null;

	public ProductAdapter(Context context, List<Product> list) {
		this.context = context;
		this.list = list;
		this.database = new Database(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
		LinearLayout linLay;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		rowView = convertView;

		if (rowView == null) {
			rowView = LayoutInflater.from(this.context).inflate(
					R.layout.program_row, parent, false);

			holder = new Holder();

			holder.tv = (TextView) rowView.findViewById(R.id.gridProductPrice);
			holder.img = (ImageView) rowView.findViewById(R.id.gridProductIcon);
			holder.linLay = (LinearLayout) rowView
					.findViewById(R.id.itemLayout);

			rowView.setTag(holder);
			rowView.setTag(R.id.gridProductPrice, holder.tv);
			rowView.setTag(R.id.gridProductIcon, holder.img);
			rowView.setTag(R.id.itemLayout, holder.linLay);

		} else {
			holder = (Holder) rowView.getTag();
		}

		currentProduct = list.get(position);

		holder.tv.setText(currentProduct.getCurrentPrice());

		new ImageLoadTask(holder.img)
				.execute(currentProduct.getProductImgURL());

		holder.img.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				compute(position);

				return true;
			}
		});

		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent productDetailsIntent = new Intent(context,
						ProductDetailsActivity.class);
				productDetailsIntent.putExtra("selectedProduct",
						list.get(position));
				context.startActivity(productDetailsIntent);

			}
		});

		rowView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				compute(position);

				return true;
			}
		});

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent productDetailsIntent = new Intent(context,
						ProductDetailsActivity.class);
				productDetailsIntent.putExtra("selectedProduct",
						list.get(position));
				context.startActivity(productDetailsIntent);

			}
		});

		return rowView;
	}

	private void compute(int position) {

		if (database.favourite(list.get(position))) {
			Toast.makeText(context, "Added to Favourite List!",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Removed from Favourite List!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		private Bitmap bitmap;

		private WeakReference<ImageView> viewReference;

		public ImageLoadTask(ImageView bmImage) {
			viewReference = new WeakReference<ImageView>(bmImage);
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
			super.onPostExecute(result);

			ImageView imageView = viewReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(result);
			}
		}

	}

}