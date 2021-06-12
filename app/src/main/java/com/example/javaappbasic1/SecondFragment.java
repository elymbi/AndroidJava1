package com.example.javaappbasic1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Random;

public class SecondFragment extends Fragment {
    TextView showRandomNumberView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);
        showRandomNumberView = fragmentSecondLayout.findViewById(R.id.textview_header);
        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int count = SecondFragmentArgs.fromBundle(getArguments()).getMyArg();
        String countText = getString(R.string.random_heading, count);
        TextView headerView = view.getRootView().findViewById(R.id.textview_header);
        headerView.setText(countText);

        Random random = new Random();
        int randomNumber = 0;
        if (count > 0) {
            randomNumber = random.nextInt(count + 1);
        }

        TextView centerView = view.getRootView().findViewById(R.id.attributes_random);
        centerView.setText(getString(R.string.random_number, randomNumber));

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });



    }

}