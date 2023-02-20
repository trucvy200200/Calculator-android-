package hcmute.edu.vn.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import hcmute.edu.vn.myapplication.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    public boolean lastNumberic = false;
    public boolean stateError = false;
    public boolean lastDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    public void sendMessage(View view)
    {
        Log.i("Event Handling", "Welcome to my handling exercises");
    }

    public void onAllclearClick(View view) {
        binding.numInput.setText("");
        binding.ans.setText("");
        stateError = false;
        lastDot = false;
        lastNumberic = false;

    }
    public void onEqualClick(View view) {
        onEqual();
        binding.numInput.setText(binding.ans.getText().toString().substring(1));
    }
    public void onDigitClick(View view) {
        if (stateError) {
            binding.numInput.setText(((Button)view).toString());
            stateError = false;
        } else {
            binding.numInput.append(((Button)view).getText().toString());
        }

        lastNumberic = true;
        onEqual();
    }

    public void onOperatorClick(View view) {
        if(!stateError && lastNumberic) {
            binding.numInput.append(((Button)view).getText().toString());
            lastDot = false;
            lastNumberic = false;
            onEqual();
        }
    }

    private boolean isDigit(char c) {

        if (Character.isDigit(c) == true) {
            // return position of digit
            return true;
        }
        // return 0 if digit not present
        return false;
    }

    public void onBackClick(View view) {
//        remove the last character
        binding.numInput.setText(binding.numInput.getText().toString().substring(0, binding.numInput.getText().toString().length() - 1));
        try {
            char lastChar = binding.numInput.getText().toString().charAt(binding.numInput.getText().toString().length() - 1);
            if (isDigit(lastChar)) {
                onEqual();
            }
        } catch (Exception ex) {
            binding.ans.setText("");
//            binding.ans.setVisibility(View.GONE);
            Log.e("Last character error", ex.toString());
        }
    }

    public void onEqual() {
        if (lastNumberic && !stateError) {
            String txt = binding.numInput.getText().toString();
            Expression expression = new ExpressionBuilder(txt).build();

            try {
                double result = expression.evaluate();
                Log.i("onEqual", String.valueOf(result));

                // Cast result to int if possible
                if (result == (int) result) {
                    binding.ans.setText("=" + String.valueOf((int)result));
                };

                binding.ans.setText("=" + String.valueOf(result));


            } catch (ArithmeticException ex) {
                Log.e("Evaluate error: ", ex.toString());
                binding.ans.setText("Error");
                stateError = true;
                lastNumberic = false;
            }

        }
    }


}