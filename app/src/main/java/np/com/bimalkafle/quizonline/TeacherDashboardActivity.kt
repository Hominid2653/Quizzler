package np.com.bimalkafle.quizonline

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TeacherDashboardActivity : AppCompatActivity() {

    private lateinit var btnCreateQuiz: Button
    private lateinit var btnViewPerformance: Button
    private lateinit var listQuizzes: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        // Initialize views
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz)
        btnViewPerformance = findViewById(R.id.btnViewPerformance)
        listQuizzes = findViewById(R.id.listQuizzes)

        // Example: Set up a list adapter for quizzes
        val quizzes = arrayOf("Quiz 1", "Quiz 2", "Quiz 3") // Replace with actual quiz data
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, quizzes)
        listQuizzes.adapter = adapter

        // Example: Button click listener for creating quizzes
        btnCreateQuiz.setOnClickListener {
            // Start intent to navigate to CreateQuizActivity
            val intent = Intent(this, CreateQuizActivity::class.java)
            startActivity(intent)
        }

        // Example: Button click listener for viewing performance
        btnViewPerformance.setOnClickListener {
            // Start intent to navigate to ViewPerformanceActivity
            val intent = Intent(this, ViewPerformanceActivity::class.java)
            startActivity(intent)
        }
    }
}
