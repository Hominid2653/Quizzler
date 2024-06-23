package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val rgUserType: RadioGroup = findViewById(R.id.rgUserType)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnCreateAccount: Button = findViewById(R.id.btnCreateAccount)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val selectedUserTypeId = rgUserType.checkedRadioButtonId

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedUserTypeId == -1) {
                Toast.makeText(this, "Please select a user type", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userType = when (selectedUserTypeId) {
                R.id.rbTeacher -> "Teacher"
                R.id.rbStudent -> "Student"
                else -> ""
            }

            // Authenticate user using Firebase Authentication
            signInWithEmailAndPassword(email, password, userType)
        }

        btnCreateAccount.setOnClickListener {
            // Navigate to CreateAccountActivity when Create Account button is clicked
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String, userType: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        // Check if user is a teacher or student and navigate accordingly
                        if (userType == "Teacher") {
                            startActivity(Intent(this, TeacherDashboardActivity::class.java))
                        } else if (userType == "Student") {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        finish()
                    } else {
                        // User is null, handle this scenario if needed
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
