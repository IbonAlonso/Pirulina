package com.ibgo.pirulina.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ibgo.pirulina.R;
import com.ibgo.pirulina.model.pojo.User;

public class MyUserFragment extends Fragment {

    private static final String ARG_USER = "user";

    private OnFragmentInteractionListener mListener;
    private Button mEditButton, mCancelButton, mOkButton;
    private EditText mLogin, mName, mLast, mPhone, mPass, mRPass;
    private User mUser;

    public MyUserFragment() {
        // Required empty public constructor
    }

    public static MyUserFragment newIntent(User user) {
        MyUserFragment fragment = new MyUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getArguments().getSerializable(ARG_USER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_user, container, false);

        mLogin = v.findViewById(R.id.edtMyUsername);
        mLogin.setText(mUser.getLogin());
        mName = v.findViewById(R.id.edtMyName);
        mName.setText(mUser.getName());
        mLast = v.findViewById(R.id.edtMyLastname);
        mLast.setText(mUser.getLast());
        mPhone = v.findViewById(R.id.edtMyPhone);
        mPhone.setText(mUser.getPhone());
        mPass = v.findViewById(R.id.edtMyPass);
        mRPass = v.findViewById(R.id.edtMyRPass);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
