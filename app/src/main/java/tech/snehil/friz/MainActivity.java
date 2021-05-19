package tech.snehil.friz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ToggleButton button;
    private LinearLayout celsiusContainer;
    private LinearLayout fahrenheitContainer;
    private EditText celsius;
    private EditText fahrenheit;

    private boolean isCelsiusToFahrenheit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celsiusContainer = findViewById(R.id.topInputContainer);
        fahrenheitContainer = findViewById(R.id.bottomInputContainer);
        button = findViewById(R.id.toggleButton);
        celsius = findViewById(R.id.topInput);
        fahrenheit = findViewById(R.id.bottomInput);
        celsius.requestFocus();

        RelativeLayout root = findViewById(R.id.root);
        root.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING); //  Enable autoanimate on layout changes

        TextWatcher CelsiusToFahrenheit = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence input, int i, int i1, int i2) {
                if(input.toString().equals("")){
                    fahrenheit.setText("");
                } else if(input.toString().equals("-")){
                    // Do nothing. Wait for more input
                    fahrenheit.setText("...");
                } else {
                    float C = Float.parseFloat(input.toString());
                    float F = (C * 9.0f/5.0f) + 32;
                    fahrenheit.setText(String.format(Locale.ENGLISH,"%.2f", F));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        TextWatcher FahrenheitToCelsius = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence input, int i, int i1, int i2) {
                if(input.toString().equals("")){
                    celsius.setText("");
                } else if(input.toString().equals("-")){
                    // Do nothing. Wait for more input
                    celsius.setText("...");
                } else {
                    float F = Float.parseFloat(input.toString());
                    float C = (F - 32) * 5.0f/9.0f;
                    celsius.setText(String.format(Locale.ENGLISH,"%.2f", C));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RelativeLayout.LayoutParams fParams = (RelativeLayout.LayoutParams) fahrenheitContainer.getLayoutParams();
                RelativeLayout.LayoutParams cParams = (RelativeLayout.LayoutParams) celsiusContainer.getLayoutParams();
                float deg = compoundButton.getRotation() + 180F;
                compoundButton.animate().rotation(deg).setInterpolator(new AccelerateInterpolator());
                if(compoundButton.isChecked()){ // Fahrenheit to Celsius
                    isCelsiusToFahrenheit = false;
                    celsius.setEnabled(false);
                    fahrenheit.setEnabled(true);

                    fParams.removeRule(RelativeLayout.BELOW);
                    cParams.removeRule(RelativeLayout.ABOVE);

                    fParams.addRule(RelativeLayout.ABOVE, R.id.divider);
                    cParams.addRule(RelativeLayout.BELOW, R.id.divider);

                    celsius.removeTextChangedListener(CelsiusToFahrenheit);
                    fahrenheit.addTextChangedListener(FahrenheitToCelsius);
                } else {    // Celsius to Fahrenheit
                    isCelsiusToFahrenheit = true;
                    celsius.setEnabled(true);
                    fahrenheit.setEnabled(false);

                    fParams.removeRule(RelativeLayout.ABOVE);
                    cParams.removeRule(RelativeLayout.BELOW);

                    fParams.addRule(RelativeLayout.BELOW, R.id.divider);
                    cParams.addRule(RelativeLayout.ABOVE, R.id.divider);

                    fahrenheit.removeTextChangedListener(FahrenheitToCelsius);
                    celsius.addTextChangedListener(CelsiusToFahrenheit);
                }
            }
        });

        celsius.addTextChangedListener(CelsiusToFahrenheit);    //  Start C to F conversion
    }
}