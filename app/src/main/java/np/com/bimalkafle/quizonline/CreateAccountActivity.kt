package np.com.bimalkafle.quizonline

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var rgAccountType: RadioGroup
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        rgAccountType = findViewById(R.id.rgAccountType)

        val btnCreateAccount: Button = findViewById(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val selectedAccountType = when (rgAccountType.checkedRadioButtonId) {
                R.id.rbTeacher -> "Teacher"
                R.id.rbStudent -> "Student"
                else -> ""
            }

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || selectedAccountType.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user with email and password using Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        if (user != null) {
                            // Update user's display name
                            val profileUpdates = userProfileChangeRequest {
                                displayName = fullName
                            }
                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        // Profile updated successfully
                                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                                        // Navigate to appropriate dashboard based on account type
                                        if (selectedAccountType == "Teacher") {
                                            // Example: startActivity(Intent(this, TeacherDashboardActivity::class.java))
                                            // Replace with your actual navigation logic
                                        } else if (selectedAccountType == "Student") {
                                            // Example: startActivity(Intent(this, MainActivity::class.java))
                                            // Replace with your actual navigation logic
                                        }
                                        finish()
                                    } else {
                                        // Failed to update profile
                                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        val exception = task.exception
                        if (exception != null) {
                            Toast.makeText(this, "Account creation failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }
}
