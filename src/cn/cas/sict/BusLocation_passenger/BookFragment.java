package cn.cas.sict.BusLocation_passenger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookFragment extends Fragment {
	TextView tv_show;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_book, container,
				false);
		// tv_show = (TextView) rootView.findViewById(R.id.tv_book_show);

		// Spinner spinner=(Spinner)rootView.findViewById(R.id.spinner);
		// //spinner.getSelectedItem(); //final TextView
		// remiannum=(TextView)getActivity().findViewById(R.id.remainnum);
		// //final TextView
		// drivername=(TextView)getActivity().findViewById(R.id.drivername);
		// //final TextView
		// phonenum=(TextView)getActivity().findViewById(R.id.phonenum);
		// //spinner.findFocus(); spinner.setOnItemSelectedListener(new
		// OnItemSelectedListener(){
		//
		// @Override public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) { // TODO Auto-generated method stub String
		// result=arg0.getItemAtPosition(arg2).toString();
		// editor.putString("routename",
		// result).commit();//只实现了传到设置，没有实现从设置传到spinner
		// Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		// editor.putInt("remainnum", 0).commit(); }
		//
		// @Override public void onNothingSelected(AdapterView<?> arg0) { //
		// TODO Auto-generated method stub
		//
		// }
		//
		// }); final Button dill=(Button)rootView.findViewById(R.id.dill); final
		// Button cancel=(Button)rootView.findViewById(R.id.cancel);
		// cancel.setEnabled(false); dill.setOnClickListener(new
		// OnClickListener(){
		//
		// @Override public void onClick(View v) { // TODO Auto-generated method
		// stub
		//
		// AlertDialog alert=new AlertDialog.Builder(getActivity()).create();
		// alert.setIcon(R.drawable.icon_wo1); alert.setTitle("系统提示：");
		// alert.setMessage("您是否要预订班车");
		// //因为导入的包不同，应该为import。view。DialogInterface。OnClickListener
		// alert.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", new
		// DialogInterface.OnClickListener() {
		//
		// @Override public void onClick(DialogInterface dialog, int which) {
		// Toast.makeText(getActivity(), "您单击了取消按钮", Toast.LENGTH_SHORT).show();
		//
		// } }); alert.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new
		// DialogInterface.OnClickListener() {
		//
		// @Override public void onClick(DialogInterface dialog, int which) {
		// Toast.makeText(getActivity(), "您单击了确定按钮", Toast.LENGTH_SHORT).show();
		// //editor.putInt("remiannum", -1).commit();//座位-1
		// cancel.setEnabled(true);
		//
		// } }); alert.show();
		//
		// }
		//
		// }); if(cancel.isEnabled()){ cancel.setOnClickListener(new
		// OnClickListener(){
		//
		// @Override public void onClick(View v) { // TODO Auto-generated method
		// stub //editor.putInt("remiannum", +1).commit();//座位+1
		// Toast.makeText(getActivity(), "您取消了预订，欢迎再次使用",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		//
		// }); }
		//

		return rootView;
	}
}
