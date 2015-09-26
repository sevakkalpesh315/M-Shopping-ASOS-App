package com.asos.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.asos.R;
import com.asos.model.model.Category;

import java.util.List;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class CategoryListAdapter extends ArrayAdapter<Category> {

	private List<Category> list = null;
	private Context context = null;
	private TextView categoryName = null;

	public CategoryListAdapter(Context context, List<Category> list) {
		super(context, R.layout.list_row, list);
		this.list = list;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_row, null);
		}

		categoryName = (TextView) convertView.findViewById(R.id.categoryName);

		Category currentCategory = list.get(position);

		categoryName.setText(currentCategory.getName());

		return convertView;
	}

}
