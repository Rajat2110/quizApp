package com.rajat.quizApp.service;

import com.rajat.quizApp.dao.QuestionDao;
import com.rajat.quizApp.dao.QuizDao;
import com.rajat.quizApp.model.Question;
import com.rajat.quizApp.model.QuestionWrapper;
import com.rajat.quizApp.model.Quiz;
import com.rajat.quizApp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numOfQues, String title) {
        List<Question> questions = questionDao.findRandomQuestionByCategory(category, numOfQues);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        List<QuestionWrapper> quesForUser = new ArrayList<>();
        for(Question q: questions) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4(), q.getDifficultyLevel(), q.getCategory());
            quesForUser.add(qw);
        }

        return new ResponseEntity<>(quesForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int score=0;
        int i=0;

        for(Response response: responses) {
            if(response.getResponse().equals(questions.get(i).getRightAnswer())){
                score++;
            }
            i++;
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
