import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class TextValidator(private val editText: EditText): TextWatcher {
    private lateinit var errorListener: (ArrayList<ValidationErrors>)-> Unit

    private val entities: ArrayList<ValidationEntity> = ArrayList()
    private val errors: ArrayList<ValidationErrors> = ArrayList()

    private var isError: Boolean = false

    init {
        editText.addTextChangedListener(this)
    }

    companion object {
        private val validators: HashMap<Int, TextValidator> = HashMap()
        fun getValidator(editText: EditText): TextValidator {
            var validator = validators[editText.hashCode()]
            if(validator == null) {
                validator = TextValidator(editText)
                validators[editText.hashCode()] = validator
            }
            return validator
        }
        fun clearValidator(editText: EditText) : TextValidator?
                = validators.remove(editText.hashCode())
    }

    fun addErrorCallback(errorListener: (ArrayList<ValidationErrors>)-> Unit) {
        this.errorListener = errorListener
    }

    fun isRequired(): TextValidator {
        entities.add(ValidationEntity.IS_REQUIRED)
        return this
    }

    fun validateName(): TextValidator {
        entities.add(ValidationEntity.NAME)
        return this
    }

    fun validateFullName(): TextValidator {
        entities.add(ValidationEntity.FULL_NAME)
        return this
    }

    fun check() {
        check(editText.text.toString())
    }

    private fun check(text: String?) {
        isError = false
        errors.clear()
        for (entity in entities) {
            when(entity){
                ValidationEntity.IS_REQUIRED -> {
                    _isRequired(text)
                }
                ValidationEntity.NAME -> {
                    _validateName(text)
                }
                ValidationEntity.FULL_NAME -> {
                    _validateFullName(text)
                }
            }
        }

        if(isError && ::errorListener.isInitialized){
            errorListener(errors)
        }
    }

    private fun _isRequired(text: String?) {
        if(text == null || text.isEmpty()){
            editText.error = ValidationErrors.REQUIRED.response
            errors.add(ValidationErrors.REQUIRED)
            isError = true
        }
    }

    private fun _validateName(text: String?) {
        if(text == null || text.isEmpty() || isError) return
        if(text.trim().length < 3) {
            editText.error = ValidationErrors.NAME_TOO_SHORT.response
            errors.add(ValidationErrors.NAME_TOO_SHORT)
            isError = true
        }
    }

    private fun _validateFullName(text: String?) {
        if(text == null || text.isEmpty() || isError) return

        val fullName = text.split(" ")
        if(fullName.size < 2) {
            editText.error = ValidationErrors.NOT_FULL_NAME.response
            errors.add(ValidationErrors.NOT_FULL_NAME)
            isError = true
            return
        }
        if(fullName[0].trim().length < 3) {
            editText.error = ValidationErrors.FIRST_NAME_TOO_SHORT.response
            errors.add(ValidationErrors.FIRST_NAME_TOO_SHORT)
            isError = true
            return
        }
        if(fullName[fullName.size-1].trim().length < 3) {
            editText.error = ValidationErrors.LAST_NAME_TOO_SHORT.response
            errors.add(ValidationErrors.LAST_NAME_TOO_SHORT)
            isError = true
            return
        }

    }

    override fun afterTextChanged(editable: Editable?) {
        if(editable != null){
            val text = editable.toString()
            check(text)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    enum class ValidationErrors(val response: String){
        REQUIRED("Field cannot be empty"),
        NAME_TOO_SHORT("Please enter a valid name"),
        NOT_FULL_NAME("Please enter your full name"),
        FIRST_NAME_TOO_SHORT("Please enter a valid name"),
        LAST_NAME_TOO_SHORT("Please enter a valid name"),
    }

    enum class ValidationEntity {
        IS_REQUIRED,
        NAME,
        FULL_NAME
    }
}
