package np.com.bimalkafle.quizonline

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import np.com.bimalkafle.quizonline.R
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import np.com.bimalkafle.quizonline.QuestionModel

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var etQuizTitle: EditText
    private lateinit var etQuizSubtitle: EditText
    private lateinit var etQuestionTime: EditText
    private lateinit var llQuestionsContainer: LinearLayout
    private lateinit var btnAddQuestion: Button
    private lateinit var btnSaveQuiz: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val quizzesCollection = firestore.collection("quizzes")
    private val questionList = mutableListOf<QuestionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        etQuizTitle = findViewById(R.id.etQuizTitle)
        etQuizSubtitle = findViewById(R.id.etQuizSubtitle)
        etQuestionTime = findViewById(R.id.etQuestionTime)
        llQuestionsContainer = findViewById(R.id.llQuestionsContainer)
        btnAddQuestion = findViewById(R.id.btnAddQuestion)
        btnSaveQuiz = findViewById(R.id.btnSaveQuiz)

        btnAddQuestion.setOnClickListener { addQuestionField() }
        btnSaveQuiz.setOnClickListener { saveQuiz() }
    }

    private fun addQuestionField() {
        val questionView = LayoutInflater.from(this).inflate(R.layout.item_question, llQuestionsContainer, false)
        llQuestionsContainer.addView(questionView)

        // Add functionality to handle adding question fields dynamically
        // Example: Add delete button or functionality for each question view
    }

    private fun saveQuiz() {
        val quizTitle = etQuizTitle.text.toString().trim()
        val quizSubtitle = etQuizSubtitle.text.toString().trim()
        val questionTime = etQuestionTime.text.toString().trim()

        if (quizTitle.isEmpty() || quizSubtitle.isEmpty() || questionTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val timePerQuestion = questionTime.toIntOrNull()
        if (timePerQuestion == null || timePerQuestion <= 0) {
            Toast.makeText(this, "Invalid time per question", Toast.LENGTH_SHORT).show()
            return
        }

        // Clear existing questionList to avoid duplicate entries
        questionList.clear()

        // Iterate through llQuestionsContainer to retrieve questions and options
        for (i in 0 until llQuestionsContainer.childCount) {
            val questionView = llQuestionsContainer.getChildAt(i)
            val etQuestion = questionView.findViewById<EditText>(R.id.etQuestion)
            val etOption1 = questionView.findViewById<EditText>(R.id.etOption1)
            val etOption2 = questionView.findViewById<EditText>(R.id.etOption2)
            val etOption3 = questionView.findViewById<EditText>(R.id.etOption3)
            val etOption4 = questionView.findViewById<EditText>(R.id.etOption4)

            val question = etQuestion.text.toString().trim()
            val options = listOf(
                etOption1.text.toString().trim(),
                etOption2.text.toString().trim(),
                etOption3.text.toString().trim(),
                etOption4.text.toString().trim()
            )

            // Validate question and options
            if (question.isEmpty() || options.any { it.isEmpty() }) {
                Toast.makeText(this, "Please fill in all fields for each question", Toast.LENGTH_SHORT).show()
                return
            }

            // Add question to questionList
            val correctAnswer = options[0] // Assuming first option is correct by default
            questionList.add(QuestionModel(question, options, correctAnswer))
        }

        // Create a map representing the quiz data
        val quizData = hashMapOf(
            "title" to quizTitle,
            "subtitle" to quizSubtitle,
            "time" to timePerQuestion.toString(),
            "questions" to questionList.map { it.toMap() } // Convert QuestionModel to Map
        )

        // Add quizData to Firestore
        quizzesCollection.add(quizData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Quiz saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving quiz: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
