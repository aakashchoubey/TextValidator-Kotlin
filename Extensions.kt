import android.widget.EditText

fun EditText.validator(): TextValidator = TextValidator.getValidator(this)
fun EditText.clearValidator(): TextValidator? = TextValidator.clearValidator(this)

fun String?.isEmpty(): Boolean = (this == null) || (this.length == 0) || (this.trim().length == 0)