package com.example.javaappbasic1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import static com.example.javaappbasic1.MainActivity.packageName;

@RequiresApi(api = Build.VERSION_CODES.N)
public class FirstFragment extends Fragment {

    TextView showCountTextView;
    View fragmentFirstLayout;
    Side currentSide = Side.START;
    int currentCard = 0;
    List<String> cardsList = null;
    View the_user_input;
    String string_user_input;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);
        showCountTextView.setMovementMethod(new ScrollingMovementMethod());
        string_user_input = ((EditText) fragmentFirstLayout.findViewById(R.id.edit_text_id)).getText().toString();
        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.next_with_keyword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                string_user_input = ((EditText) fragmentFirstLayout.findViewById(R.id.edit_text_id)).getText().toString();
//                System.out.println(string_user_input);
                displayNextCardWithKeyword(string_user_input);
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
                uponButtonClick(current -> current+1);
                displayCard();
            }
        });
    }

    private void uponButtonClick(IntFunction<Integer> functionUponButtonClick) {
        switch (currentSide) {
            case START:
                parseFile("fisi_grundlagen");
                currentCard = functionUponButtonClick.apply(-1);
                currentSide = Side.FRONT;
            case BACK:
                currentCard = functionUponButtonClick.apply(currentCard);
                currentSide = Side.FRONT;
                break;
            case FRONT:
                currentSide = Side.BACK;
                break;
        }
    }

    private void displayNextCardWithKeyword(String keyword) {
        uponButtonClick(current -> findNextCardWithKeyword(keyword, current+1));
        if(currentCard == -1){
            displayNotFound();
        }
        else {
            displayCard();
        }
    }

    private void displayNotFound() {
        showCountTextView.setText("No more cards with this keyword");
    }

    private int findNextCardWithKeyword(String keyword, int startAt) {
        if (keyword == null) {
            return 1;
        }
        for (int i = 0; i < cardsList.size(); i++) {
            int index = (i + startAt) % cardsList.size();
            String s = cardsList.get(index);
            if(s.contains(keyword)){
                return index;
            }
        }
        return -1;
    }

    private void switchToSecondFragment() {
        int currentCount = Integer.parseInt(showCountTextView.getText().toString());
        FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount);
        NavHostFragment.findNavController(FirstFragment.this).navigate(action);
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

    public class SharedPreferencesActivity extends Activity {
        private void saveInput() {
            SharedPreferences sharedPref;
            sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            string_user_input = ((EditText) findViewById(R.id.edit_text_id)).getText().toString();
            editor.putString(getString(R.string.user_input), string_user_input);
            editor.apply();
        }

//        private String getInput(){
//            return editor.
//        }
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