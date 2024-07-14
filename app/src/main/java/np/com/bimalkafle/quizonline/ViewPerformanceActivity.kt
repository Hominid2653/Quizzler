package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.FirebaseFirestore

class ViewPerformanceActivity : AppCompatActivity() {

    private lateinit var scoreTitle: TextView
    private lateinit var scoreProgressIndicator: CircularProgressIndicator
    private lateinit var scoreProgressText: TextView
    private lateinit var scoreSubtitle: TextView
    private lateinit var finishButton: Button

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.performance_view_layout)

        scoreTitle = findViewById(R.id.score_title)
        scoreProgressIndicator = findViewById(R.id.score_progress_indicator)
        scoreProgressText = findViewById(R.id.score_progress_text)
        scoreSubtitle = findViewById(R.id.score_subtitle)
        finishButton = findViewById(R.id.finish_btn)

        firestore = FirebaseFirestore.getInstance()

        // Replace "quizId" with the actual quiz ID you want to fetch
        val quizId = intent.getStringExtra("quizId") ?: return

        fetchPerformanceData(quizId)

        finishButton.setOnClickListener {
            navigateToAdminDashboard()
        }
    }

    private fun fetchPerformanceData(quizId: String) {
        // Assuming "performance" collection exists and documents are stored with quizId
        firestore.collection("performance").document(quizId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val correctAnswers = document.getLong("correctAnswers")?.toInt() ?: 0
                    val totalQuestions = document.getLong("totalQuestions")?.toInt() ?: 1 // Avoid division by zero
                    val scorePercentage = (correctAnswers.toFloat() / totalQuestions.toFloat()) * 100

                    // Set the values in the UI
                    scoreProgressText.text = "${scorePercentage.toInt()}%"
                    scoreProgressIndicator.setProgress(scorePercentage.toInt(), true)
                    scoreSubtitle.text = "$correctAnswers out of $totalQuestions questions are correct"
                } else {
                    Toast.makeText(this, "No performance data found for this quiz", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToAdminDashboard() {
        startActivity(Intent(this, TeacherDashboardActivity::class.java))
        finish()
    }
}
