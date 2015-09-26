package com.asos.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.asos.R;
import com.asos.model.adapter.ViewPagerAdapter;
import com.asos.model.listeners.CommunicationInterface;
import com.asos.model.utilities.Utils;
/**
 * @author Kalpesh Sevak
 * @version 2015.0606
 * @since 1.0
 */
public class NavigationDrawerMenuFragment extends Fragment {

	private View parentView;

	private Button men, women;
	private ViewPager myPager = null;
	private ViewPagerAdapter adapter = null;
	private CommunicationInterface ndi = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ndi = (CommunicationInterface) getActivity();

		parentView = inflater.inflate(R.layout.drawer_pager, container, false);

		if (Utils.isNetworkAvailable(getActivity())) {

			adapter = new ViewPagerAdapter();
			adapter.setnDrawerInterface(ndi);

			myPager = (ViewPager) parentView.findViewById(R.id.pager);
			myPager.setAdapter(adapter);

			men = (Button) parentView.findViewById(R.id.men);
			women = (Button) parentView.findViewById(R.id.women);

			men.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					myPager.setCurrentItem(0);
				}
			});

			women.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					myPager.setCurrentItem(1);
				}
			});
		} else {
			Toast.makeText(getActivity(),
					"Sorry, There is No Internet Connection!",
					Toast.LENGTH_LONG).show();
		}

		return parentView;
	}
}