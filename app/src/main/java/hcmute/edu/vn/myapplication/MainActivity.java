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
        try {
            onEqual();
            binding.numInput.setText(binding.ans.getText().toString().substring(1));
        } catch (Exception ex) {
            Log.e("Equal error", ex.toString());
            onAllclearClick(binding.ans);
        }
    }
    public void onDigitClick(View view) {

//        mark lastDot true if last character is dot
        if (binding.numInput.getText().toString().length() > 0) {
            if (binding.numInput.getText().toString().charAt(binding.numInput.getText().toString().length() - 1) == '.') {
                lastDot = true;
            }
            else {
                lastDot = false;
            }
        }

//        if last character is already a dot and user click dot again
//        do nothing
        if (lastDot && ((Button)view).getText().toString().equals(".")) {
            return;
        }


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

//        return if no input
        if (binding.numInput.getText().toString().length() == 0) {
            return;
        }

//        remove the last character
        binding.numInput.setText(binding.numInput.getText().toString().substring(0, binding.numInput.getText().toString().length() - 1));
        try {
            char lastChar = binding.numInput.getText().toString().charAt(binding.numInput.getText().toString().length() - 1);
            if (isDigit(lastChar)) {
                onEqual();
            }
        } catch (Exception ex) {
            binding.ans.setText("");
            Log.e("Last character error", ex.toString());
        }
    }

    public void onEqual() {
        if (lastNumberic && !stateError) {
            String txt = binding.numInput.getText().toString();
            try {
                Expression expression = new ExpressionBuilder(txt).build();
                try {
                    double result = expression.evaluate();
                    Integer resultInt = null;

                    try {
                        resultInt = (int) result;
                    } catch (Exception ex) {
                        Log.e("Cast error", ex.toString());
                    }


                    // check if result is integer and resultInt is not null
                    if (resultInt != null && result == resultInt) {
                        binding.ans.setText("=" + String.valueOf(resultInt));
                    } else {
                        binding.ans.setText("=" + String.valueOf(result));
                    }


                } catch (ArithmeticException ex) {
                    Log.e("Evaluate error: ", ex.toString());
                    onAllclearClick(binding.ans);
//                    binding.ans.setText("");
//                    binding.numInput.setText("");
//                    stateError = true;
//                    lastNumberic = false;
                }
            } catch (Exception ex) {
                Log.e("Expression error: ", ex.toString());
//                binding.ans.setText("");
//                binding.numInput.setText("");
//                stateError = true;
//                lastNumberic = false;
                onAllclearClick(binding.ans);
            }


        }
    }


}