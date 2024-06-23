package np.com.bimalkafle.quizonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import np.com.bimalkafle.quizonline.LoginActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.FirebaseApp
import np.com.bimalkafle.quizonline.R.*
import np.com.bimalkafle.quizonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList : MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()


    }
    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(layout.activity_login)

            // Check if the user is logged in
            if (!isUserLoggedIn()) {
                // Redirect to login activity if not logged in
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            // Find the logout button and set click listener
            val btnLogout: Button = findViewById(R.id.btnLogout)
            btnLogout.setOnClickListener {
                // Call the logout function
                logoutUser()

                // Redirect to login page
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Function to check if the user is logged in
        private fun isUserLoggedIn(): Boolean {
            // This is a placeholder. Replace with your actual login status check.
            // For example, you might check SharedPreferences or a session token.
            return getSharedPreferences("user_prefs", MODE_PRIVATE).getBoolean("isLoggedIn", false)
        }

        // Function to clear the login state and log out the user
        private fun logoutUser() {
            // Clear the login state from SharedPreferences
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
        }
    }
    private fun setupRecyclerView(){
        binding.progressBar.visibility = View.GONE
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase(){
        binding.progressBar.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot->
                if(dataSnapshot.exists()){
                    for (snapshot in dataSnapshot.children){
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setupRecyclerView()
            }


    }
}














