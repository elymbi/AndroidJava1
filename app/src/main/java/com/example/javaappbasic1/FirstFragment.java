package com.example.javaappbasic1;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.javaappbasic1.MainActivity.packageName;

public class FirstFragment extends Fragment {

    TextView showCountTextView;
    Side currentSide = Side.FRONT;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);
        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.random_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = Integer.parseInt(showCountTextView.getText().toString());
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);
                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
            }
        });

        view.findViewById(R.id.toast_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity(), getString(R.string.toast), Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        view.findViewById(R.id.count_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentSide) {
                    case BACK:
                        currentSide = Side.FRONT;
                        break;
                    case FRONT:
                        currentSide = Side.BACK;
                        break;
                }
                displayFile();
            }
        });
    }

    private void displayFile() {
        Resources resources = getResources();

        int number_for_file = getResources().getIdentifier("small_card",
                "raw", packageName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resources.openRawResource(number_for_file)))) {
            Html.fromHtml("<h2>Title</h2><br><p>Description here</p>");
            String line;

            while((line = br.readLine()) != null) {
                String[] card_sides = line.split("\\t", 2);
                if(card_sides.length == 2){
                    Spanned front_side = Html.fromHtml(card_sides[0]);
                    Spanned back_side = Html.fromHtml(card_sides[1]);
                    if(currentSide == Side.FRONT){
                        showCountTextView.setText(front_side);
                    }
                    else{
                        showCountTextView.setText(back_side);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void countMe() {
        String countString = showCountTextView.getText().toString();
        int count = Integer.parseInt(countString);
        count++;
        showCountTextView.setText(Integer.toString(count));
    }

    private enum Side{
        FRONT, BACK
    }
}