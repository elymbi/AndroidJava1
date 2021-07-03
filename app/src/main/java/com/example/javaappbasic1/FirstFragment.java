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
import java.util.ArrayList;
import java.util.List;

import static com.example.javaappbasic1.MainActivity.packageName;

public class FirstFragment extends Fragment {

    TextView showCountTextView;
    Side currentSide = Side.START;
    int currentCard = 0;
    List<String> cardsList = null;


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

        view.findViewById(R.id.next_with_keyword).setOnClickListener(new View.OnClickListener() {
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

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentSide) {
                    case START:
                        parseFile("fisi_grundlagen");
                        currentSide = Side.FRONT;
                    case BACK:
                        currentCard += 1;
                        currentSide = Side.FRONT;
                        break;
                    case FRONT:
                        currentSide = Side.BACK;
                        break;
                }
                displayCard();
            }
        });
    }

    private void parseFile(String fileName) {
        Resources resources = getResources();

        int number_for_file = getResources().getIdentifier("fisi_grundlagen",
                "raw", packageName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resources.openRawResource(number_for_file)))) {
            String line;
            cardsList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                cardsList.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayCard() {

        String[] card_sides = cardsList.get(currentCard).split("\\t", 2);
        if(card_sides.length == 2){
            switch (currentSide) {
                case BACK:
                    Spanned back_side = Html.fromHtml(card_sides[1]);
                    showCountTextView.setText(back_side);
                    break;
                case FRONT:
                    Spanned front_side = Html.fromHtml(card_sides[0]);
                    showCountTextView.setText(front_side);
                    break;
            }
        }
    }

    private void countMe() {
        String countString = showCountTextView.getText().toString();
        int count = Integer.parseInt(countString);
        count++;
        showCountTextView.setText(Integer.toString(count));
    }

    private enum Side{
        FRONT, BACK, START
    }
}