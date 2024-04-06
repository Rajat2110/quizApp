package com.rajat.quizApp.service;

import com.rajat.quizApp.dao.QuestionDao;
import com.rajat.quizApp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            questionDao.deleteById(id);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Question does not exist", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateQuestion(Integer id, Question question) {
        try{
            Question questionInDB = questionDao.findById(id).get();

            questionInDB.setQuestionTitle(question.getQuestionTitle());
            questionInDB.setOption1(question.getOption1());
            questionInDB.setOption2(question.getOption2());
            questionInDB.setOption3(question.getOption3());
            questionInDB.setOption4(question.getOption4());
            questionInDB.setRightAnswer(question.getRightAnswer());
            questionInDB.setDifficultyLevel(question.getDifficultyLevel());
            questionInDB.setCategory(question.getCategory());

            questionDao.save(questionInDB);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Could not update", HttpStatus.BAD_REQUEST);
    }
}
