package com.ibgo.pirulina.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;

import com.ibgo.pirulina.R;
import com.ibgo.pirulina.activity.SignInActivity;
import com.ibgo.pirulina.model.Util;
import com.ibgo.pirulina.model.pojo.User;

public class MyUserFragment extends Fragment {

    private static final String ARG_USER = "user";

    private OnFragmentInteractionListener mListener;
    private Button mEditButton, mCancelButton, mOkButton;
    private EditText mLogin, mName, mLast, mPhone, mPass, mRPass;
    private User mUser;
    private TableRow mTRPass, mTRRPass;

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
        mTRPass = v.findViewById(R.id.trPass);
        mTRRPass = v.findViewById(R.id.trRPass);

        mCancelButton = v.findViewById(R.id.btnMyCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.dialog_goback))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Fragment fragment = new PurpleButtonFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                transaction.replace(R.id.fragment_container, fragment).commit();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        mOkButton = v.findViewById(R.id.btnMyAcept);

        mEditButton = v.findViewById(R.id.btnEdit);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName.setEnabled(true);
                mLast.setEnabled(true);
                mPhone.setEnabled(true);
                mTRPass.setVisibility(View.VISIBLE);
                mPass.setEnabled(true);
                mTRRPass.setVisibility(View.VISIBLE);
                mRPass.setEnabled(true);
                mEditButton.setVisibility(View.INVISIBLE);
                mOkButton.setVisibility(View.VISIBLE);
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPass.getText().toString().equals(mRPass.getText().toString())) {
                    Toast.makeText(getContext(), getString(R.string.toast_password_not_validated), Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences mPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mPreferences.edit();
                    mEditor.putString("user", mLogin.getText().toString());
                    mEditor.putString("name", mName.getText().toString());
                    mEditor.putString("last", mLast.getText().toString());
                    mEditor.putString("phone", mPhone.getText().toString());
                    mEditor.putString("password", Util.md5(mPass.getText().toString()));
                    mEditor.commit();
                    mName.setEnabled(false);
                    mLast.setEnabled(false);
                    mPhone.setEnabled(false);
                    mTRPass.setVisibility(View.INVISIBLE);
                    mPass.setEnabled(false);
                    mTRRPass.setVisibility(View.INVISIBLE);
                    mRPass.setEnabled(false);
                    mEditButton.setVisibility(View.VISIBLE);
                    mOkButton.setVisibility(View.INVISIBLE);
                }
            }
        });

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
