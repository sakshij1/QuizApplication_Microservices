package com.quiz.question_service.Controllers;

import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.question_service.Entity.Question;
import com.quiz.question_service.Entity.QuiestionWrapper;
import com.quiz.question_service.Entity.Response;
import com.quiz.question_service.Services.QuestionService;

// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	Environment environment;

	@RequestMapping("/test")
	public ResponseEntity<String> test(){
		return new ResponseEntity<>("Testing",HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<com.quiz.question_service.Entity.Question>> getAllQuestions() {
        List<com.quiz.question_service.Entity.Question> questions = questionService.getAll();
            if (questions.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        return ResponseEntity.ok(questions);
    }

	@GetMapping("/category/{category}")
	public ResponseEntity<List<Question>> getMethodName(@PathVariable String category) {
		List<Question> questions = questionService.getQeustionByCategory(category);
		return ResponseEntity.ok(questions);
	}

	@PostMapping("/add")
	public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
		Question questionadded = questionService.addQuestion(question);
		
		return new ResponseEntity<Question>(questionadded,HttpStatus.OK);
	}
	
	@PutMapping("id/{id}")
	public ResponseEntity<Question> update(@RequestBody Question question, @PathVariable Integer id) {
		//  Question existingQuestion = questionService.findById(id);
        // if (existingQuestion == null) {
        //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // }

        // Update the fields from the request
        // existingQuestion.setQuestionTitle(question.getQuestionTitle());
        // existingQuestion.setOption1(question.getOption1());
        // existingQuestion.setOption2(question.getOption2());
        // existingQuestion.setOption3(question.getOption3());
        // existingQuestion.setOption4(question.getOption4());
        // existingQuestion.setRightAnswer(question.getRightAnswer());
        // existingQuestion.setDifficultylevel(question.getDifficultylevel());
        // existingQuestion.setCategory(question.getCategory());

        // Question updatedQuestion = questionService.updateQuestion(existingQuestion);
        // return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
		try{
            this.questionService.updateQuestion(question,id);
            // return ResponseEntity.ok().body(question);
			return new ResponseEntity<Question>(question,HttpStatus.OK);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@DeleteMapping("id/{id}")
	public ResponseEntity<String> DeleteData(@PathVariable Integer id){
		questionService.DeleteQuestion(id);
		return new ResponseEntity<String>("Data Deleted", HttpStatus.OK);
	}


	@GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz
            (@RequestParam String categoryName, @RequestParam Integer numQuestions ){
        return questionService.getQuestionsForQuiz(categoryName, numQuestions);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuiestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds){
        System.out.println(environment.getProperty("local.server.port"));
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }


    // generate
    // getQuestions (questionid)
    // getScore

	
	
}
