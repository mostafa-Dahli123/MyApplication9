package com.example.myapplication.fragments;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.fragments.CreateAccuntFragment.EMAIL_REGEX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.FragmentReplaceActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private EditText emailEt , passwordEt;
    private TextView singUpTv , forgotPasswordTv;
    private Button loginBtn , googleSingInBtn;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    public LoginFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        auth=FirebaseAuth.getInstance();




        clickListener();

    }
    private void init(View view){
        emailEt=view.findViewById(R.id.emailET);
        passwordEt=view.findViewById(R.id.passwordET);
        loginBtn=view.findViewById(R.id.loginBtn);
        googleSingInBtn=view.findViewById(R.id.googleloginBtn);
        singUpTv=view.findViewById(R.id.signUpBtn);
        forgotPasswordTv=view.findViewById(R.id.forgotTV);
        progressBar=view.findViewById(R.id.progressBar);

    }

    private void clickListener() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                    emailEt.setError("Input valid email");
                    return;
                }

                if (password.isEmpty() || password.length() < 6) {
                    passwordEt.setError("Input 6 digt valid password");
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();

                                    if (!user.isEmailVerified()) {
                                        Toast.makeText(getContext(), "Please verify ypur email", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                    sendUserToMainActivity();

                                } else {
                                    String exeption = " Error; " + task.getException().getMessage();
                                    Toast.makeText(getContext(), exeption, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });
        singUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplaceActivity)getActivity()).setFragment(new CreateAccuntFragment());
            }
        });
    }
    private void sendUserToMainActivity(){

        if (getActivity()==null)
            return;
        progressBar.setVisibility(View.VISIBLE);

        startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
        getActivity();
    }
}