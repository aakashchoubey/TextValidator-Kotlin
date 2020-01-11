# TextValidator-Kotlin

### Example Usage
```
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playground.utils.validator
import com.example.playground.utils.clearValidator
import kotlinx.android.synthetic.main.fragment_validations.*

class ValidationsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ValidationsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        et_name.validator()
            .isRequired()
            .validateName()
            .addErrorCallback {
                for (error in it){
                    Log.d("VALIDATION_ERROR", "${error.name} (${error.response})")
                }
            }
        et_full_name.validator()
            .isRequired()
            .validateFullName()
            .addErrorCallback {
                for (error in it){
                    Log.d("VALIDATION_ERROR", "${error.name} (${error.response})")
                }
            }
        btn_submit.setOnClickListener {
            et_name.validator().check()
            et_full_name.validator().check()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        et_name.clearValidator()
        et_full_name.clearValidator()
    }
}
```
