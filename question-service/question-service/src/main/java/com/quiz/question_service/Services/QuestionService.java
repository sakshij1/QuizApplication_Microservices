package com.quiz.question_service.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.quiz.question_service.Entity.Question;
import com.quiz.question_service.Entity.QuiestionWrapper;
import com.quiz.question_service.Entity.Response;
import com.quiz.question_service.Repository.QuestionRepo;

import jakarta.persistence.EntityNotFoundException;


@Service
public class QuestionService {
	
    @Autowired
    private QuestionRepo questionRepo;

    public List<com.quiz.question_service.Entity.Question> getAll(){
    	return questionRepo.findAll();
    }

    public List<Question> getQeustionByCategory(String category) {
        return questionRepo.findByCategory(category);
    }

    public Question addQuestion(Question question) {
        
        return questionRepo.save(question);
    }

    public Optional<Question> findById(Integer id) {
        return questionRepo.findById(id);
    }

    

    public void updateQuestion(Question updatedQuestion, Integer id) {
    Optional<Question> optionalQuestion = questionRepo.findById(id);
    
    if (optionalQuestion.isPresent()) {
        Question existingQuestion = optionalQuestion.get();
        
        existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
        existingQuestion.setOption1(updatedQuestion.getOption1());
        existingQuestion.setOption2(updatedQuestion.getOption2());
        existingQuestion.setOption3(updatedQuestion.getOption3());
        existingQuestion.setOption4(updatedQuestion.getOption4());
        existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
        existingQuestion.setDifficultylevel(updatedQuestion.getDifficultylevel());
        existingQuestion.setCategory(updatedQuestion.getCategory());
        
        questionRepo.save(existingQuestion);
    } else {
        throw new EntityNotFoundException("Question with ID " + id + " not found.");
    }
}

    public void DeleteQuestion(Integer id) {
        questionRepo.deleteById(id);
    }

   

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> question = questionRepo.findRandomQuestionsByCategory(categoryName, numQuestions);
        
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    public ResponseEntity<List<QuiestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
       List<QuiestionWrapper> qList = new ArrayList<>();
       List<Question> questions = new ArrayList<>();

       for (Integer id : questionIds) {
        questions.add(questionRepo.findById(id).get());  
       }

       for(Question question : questions){
            QuiestionWrapper wrapper = new QuiestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            qList.add(wrapper);
        }
       return new ResponseEntity<>(qList,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {


        int right = 0;

        for(Response response : responses){
            Question question = questionRepo.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                right++;
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }


    

    
	
}
