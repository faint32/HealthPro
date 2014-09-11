package com.wyy.myhealth.ui.baseactivity;

import com.wyy.myhealth.R;

import android.os.Bundle;
import android.widget.ListView;

public abstract class BaseListActivity extends BaseActivity{
	
	protected ListView baseListV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity_list);
		initView();
	}
	

	protected void initView(){
		baseListV=(ListView)findViewById(R.id.list_V);
		onInitView();
	}
	
	protected abstract void onInitView();
	
}
